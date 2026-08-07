package archives.tater.tooltrims.client.resource;

import archives.tater.tooltrims.ToolTrims;
import archives.tater.tooltrims.client.item.UnbakedTrimsModel;
import archives.tater.tooltrims.client.resource.util.PreparationJsonResourceReloadListener;

import net.fabricmc.fabric.api.tag.client.v1.ClientTags;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.item.*;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.Pair;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;
import static net.minecraft.client.data.models.model.ItemModelUtils.composite;

public record ClientTrimOverlay(ItemModel.Unbaked model, List<Identifier> items) {
    public static final Codec<ClientTrimOverlay> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemModels.CODEC.fieldOf("model").forGetter(ClientTrimOverlay::model),
            Identifier.CODEC.listOf().fieldOf("items").forGetter(ClientTrimOverlay::items)
    ).apply(instance, ClientTrimOverlay::new));

    public static class Loader extends PreparationJsonResourceReloadListener<ClientTrimOverlay> {
        public static final String PATH = "tooltrims/trim_overlay";
        private static final FileToIdConverter LISTER = FileToIdConverter.json(PATH);

        public static final Identifier FALLBACK_SWORD = ToolTrims.id("fallback_sword");
        public static final Identifier FALLBACK_PICKAXE = ToolTrims.id("fallback_pickaxe");
        public static final Identifier FALLBACK_AXE = ToolTrims.id("fallback_axe");
        public static final Identifier FALLBACK_SHOVEL = ToolTrims.id("fallback_shovel");
        public static final Identifier FALLBACK_HOE = ToolTrims.id("fallback_hoe");
        public static final Identifier FALLBACK_SPEAR = ToolTrims.id("fallback_spear");

        private static final Map<Identifier, Identifier> FALLBACKS = Stream.of(
                Pair.of(FALLBACK_SPEAR, ItemTags.SPEARS),
                Pair.of(FALLBACK_AXE, ItemTags.AXES),
                Pair.of(FALLBACK_PICKAXE, ItemTags.PICKAXES),
                Pair.of(FALLBACK_HOE, ItemTags.HOES),
                Pair.of(FALLBACK_SHOVEL, ItemTags.SHOVELS),
                Pair.of(FALLBACK_SWORD, ItemTags.SWORDS)
        )
                .flatMap(entry -> ClientTags.getOrCreateLocalTag(entry.right()).stream()
                        .map(item -> Pair.of(item, entry.left())))
                .collect(toMap(Pair::key, Pair::value, (first, _) -> first));

        private CompletableFuture<List<UnbakedTrimsModel>> trimModels = CompletableFuture.completedFuture(List.of());

        private CompletableFuture<Map<Identifier, ItemModel.Unbaked>> overlays = CompletableFuture.completedFuture(Map.of());

        public Loader() {
            super(CODEC, LISTER);
        }

        @Override
        public void apply(CompletableFuture<Map<Identifier, ClientTrimOverlay>> entriesFuture) {
            overlays = entriesFuture.thenApply(entries -> {
                var overlays = new ImmutableMap.Builder<Identifier, ItemModel.Unbaked>();

                FALLBACKS.forEach((item, overlayId) -> {
                    var overlay = entries.get(overlayId);
                    if (overlay == null) return;
                    overlays.put(item, overlay.model);
                });

                for (var overlay : entries.values())
                    for (var item : overlay.items)
                        overlays.put(item, overlay.model);

                return overlays.buildKeepingLast();
            });
            trimModels = entriesFuture.thenApply(entries -> entries.values().stream()
                    .flatMap(overlay -> extractTrimModels(overlay.model))
                    .distinct()
                    .toList());
        }

        private static Stream<UnbakedTrimsModel> extractTrimModels(ItemModel.@Nullable Unbaked model) {
            return switch (model) {
                case UnbakedTrimsModel unbaked -> Stream.of(unbaked);
                case CompositeModel.Unbaked unbaked -> unbaked.models().stream().flatMap(Loader::extractTrimModels);
                case ConditionalItemModel.Unbaked unbaked -> Stream.concat(extractTrimModels(unbaked.onFalse()), extractTrimModels(unbaked.onTrue()));
                case SelectItemModel.Unbaked unbaked -> Stream.concat(extractTrimModels(unbaked.fallback().orElse(null)), unbaked.unbakedSwitch().cases().stream().flatMap(switchCase -> extractTrimModels(switchCase.model())));
                case RangeSelectItemModel.Unbaked unbaked -> Stream.concat(extractTrimModels(unbaked.fallback().orElse(null)), unbaked.entries().stream().flatMap(entry -> extractTrimModels(entry.model())));
                case null, default -> Stream.empty();
            };
        }

        public static ItemModel.Unbaked modifyModel(Map<Identifier, ItemModel.Unbaked> overlays, ItemModel.Unbaked model, Identifier itemId) {
            var trimModel = overlays.get(itemId);
            if (trimModel == null || trimModel instanceof EmptyModel.Unbaked) return model;
            return composite(
                    model,
                    trimModel
            );
        }

        public CompletableFuture<Map<Identifier, ItemModel.Unbaked>> overlays() {
            return overlays;
        }

        public CompletableFuture<List<UnbakedTrimsModel>> trimModels() {
            return trimModels;
        }
    }
}
