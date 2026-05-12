package archives.tater.tooltrims.client;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModels;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.List;
import java.util.Map;

import static net.minecraft.client.data.models.model.ItemModelUtils.composite;
import static net.minecraft.util.Util.toMap;

public class TrimOverlayLoader extends SimpleJsonResourceReloadListener<TrimOverlayLoader.Entry> implements ModelLoadingPlugin, ModelModifier.BeforeBakeItem {

    public static final String PATH = "tooltrims/trim_overlays";
    private static final FileToIdConverter ASSET_LISTER = FileToIdConverter.json(PATH);
    private Map<Identifier, ItemModel.Unbaked> entries = Map.of();

    protected TrimOverlayLoader() {
        super(Entry.CODEC, ASSET_LISTER);
    }

//    public ItemModel wrapItem(Item itemId, ItemModel original) {
//
//    }

    @Override
    protected void apply(Map<Identifier, Entry> preparations, ResourceManager manager, ProfilerFiller profiler) {
        entries = preparations.values().stream()
                .flatMap(entry -> {
                    var model = entry.expandModel();
                    return entry.itemIds.stream().map(itemId -> Map.entry(itemId, model));
                })
                .collect(toMap());
    }

    @Override
    public void initialize(ModelLoadingPlugin.Context pluginContext) {
        pluginContext.modifyItemModelBeforeBake().register(this);
    }

    @Override
    public ItemModel.Unbaked modifyModelBeforeBake(ItemModel.Unbaked model, ModelModifier.BeforeBakeItem.Context context) {
        var trimModel = entries.get(context.itemId());
        if (trimModel == null) return model;
        return composite(
                model,
                trimModel
        );
    }

    public record Entry(List<Identifier> itemIds, ItemModel.Unbaked model) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Identifier.CODEC.listOf().fieldOf("items").forGetter(Entry::itemIds),
                ItemModels.CODEC.fieldOf("model").forGetter(Entry::model)
        ).apply(instance, Entry::new));

        public ItemModel.Unbaked expandModel() {
            return model; // TODO
        }
    }
}
