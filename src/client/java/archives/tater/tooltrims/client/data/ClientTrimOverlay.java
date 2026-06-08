package archives.tater.tooltrims.client.data;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.item.*;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.Map.entry;
import static net.minecraft.client.data.models.model.ItemModelUtils.composite;
import static net.minecraft.util.Util.toMap;

public record ClientTrimOverlay(ItemModel.Unbaked model, List<Identifier> items) {
    public static final Codec<ClientTrimOverlay> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemModels.CODEC.fieldOf("model").forGetter(ClientTrimOverlay::model),
            Identifier.CODEC.listOf().fieldOf("items").forGetter(ClientTrimOverlay::items)
    ).apply(instance, ClientTrimOverlay::new));

    public static class Loader extends SimpleJsonResourceReloadListener<ClientTrimOverlay> implements ModelLoadingPlugin, ModelModifier.BeforeBakeItem {
        public static final String PATH = "tooltrims/trim_overlay";
        private static final FileToIdConverter LISTER = FileToIdConverter.json(PATH);
        private List<Identifier> models = List.of();
        private Map<Identifier, ItemModel.Unbaked> overlays = Map.of();

        public Loader() {
            super(CODEC, LISTER);
        }

        @Override
        protected void apply(Map<Identifier, ClientTrimOverlay> preparations, ResourceManager manager, ProfilerFiller profiler) {
            overlays = preparations.values().stream()
                    .flatMap(overlay -> overlay.items().stream()
                            .map(item -> entry(item, overlay.model()))
                    )
                    .collect(toMap());
            models = preparations.values().stream()
                    .flatMap(overlay -> flat(overlay.model))
                    .distinct()
                    .toList();
        }

        private static Stream<Identifier> flat(ItemModel.@Nullable Unbaked model) {
            return switch (model) {
                case CuboidItemModelWrapper.Unbaked unbaked -> Stream.of(unbaked.model());
                case CompositeModel.Unbaked unbaked -> unbaked.models().stream().flatMap(Loader::flat);
                case ConditionalItemModel.Unbaked unbaked -> Stream.concat(flat(unbaked.onFalse()), flat(unbaked.onTrue()));
                case SelectItemModel.Unbaked unbaked -> Stream.concat(flat(unbaked.fallback().orElse(null)), unbaked.unbakedSwitch().cases().stream().flatMap(switchCase -> flat(switchCase.model())));
                case RangeSelectItemModel.Unbaked unbaked -> Stream.concat(flat(unbaked.fallback().orElse(null)), unbaked.entries().stream().flatMap(entry -> flat(entry.model())));
                case null, default -> Stream.empty();
            };
        }

        @Override
        public void initialize(ModelLoadingPlugin.Context pluginContext) {
            pluginContext.modifyItemModelBeforeBake().register(this);
        }

        @Override
        public ItemModel.Unbaked modifyModelBeforeBake(ItemModel.Unbaked model, ModelModifier.BeforeBakeItem.Context context) {
            var trimModel = overlays.get(context.itemId());
            if (trimModel instanceof EmptyModel.Unbaked) return model;
            return composite(
                    model,
                    trimModel
            );
        }

        public List<Identifier> getModels() {
            return models;
        }
    }
}
