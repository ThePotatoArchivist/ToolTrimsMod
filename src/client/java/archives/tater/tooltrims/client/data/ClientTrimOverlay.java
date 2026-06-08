package archives.tater.tooltrims.client.data;

import archives.tater.tooltrims.client.data.models.item.UnbakedTrimsModel;
import archives.tater.tooltrims.client.data.util.PreparationJsonResourceReloadListener;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.item.*;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;

import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static java.util.Map.entry;
import static net.minecraft.client.data.models.model.ItemModelUtils.composite;
import static net.minecraft.util.Util.toMap;

public record ClientTrimOverlay(ItemModel.Unbaked model, List<Identifier> items) {
    public static final Codec<ClientTrimOverlay> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemModels.CODEC.fieldOf("model").forGetter(ClientTrimOverlay::model),
            Identifier.CODEC.listOf().fieldOf("items").forGetter(ClientTrimOverlay::items)
    ).apply(instance, ClientTrimOverlay::new));

    public static class Loader extends PreparationJsonResourceReloadListener<ClientTrimOverlay> {
        public static final String PATH = "tooltrims/trim_overlay";
        private static final FileToIdConverter LISTER = FileToIdConverter.json(PATH);
        private CompletableFuture<List<UnbakedTrimsModel>> trimModels = CompletableFuture.completedFuture(List.of());

        private CompletableFuture<Map<Identifier, ItemModel.Unbaked>> overlays = CompletableFuture.completedFuture(Map.of());

        public Loader() {
            super(CODEC, LISTER);
        }

        @Override
        public void apply(CompletableFuture<Map<Identifier, ClientTrimOverlay>> entriesFuture) {
            overlays = entriesFuture.thenApply(entries -> entries.values().stream()
                    .flatMap(overlay -> overlay.items().stream()
                            .map(item -> entry(item, overlay.model()))
                    )
                    .collect(toMap()));
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
