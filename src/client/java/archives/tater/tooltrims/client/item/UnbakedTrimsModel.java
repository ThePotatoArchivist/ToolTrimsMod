package archives.tater.tooltrims.client.item;

import archives.tater.tooltrims.client.ToolTrimsClient;
import archives.tater.tooltrims.client.resource.ClientTrimMaterial;
import archives.tater.tooltrims.client.resource.ClientTrimPattern;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.renderer.item.EmptyModel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.properties.select.TrimMaterialProperty;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import org.joml.Matrix4fc;

import static net.minecraft.client.data.models.model.ItemModelUtils.*;

public record UnbakedTrimsModel(Identifier basePath, Identifier parent) implements ItemModel.Unbaked {
    public static final MapCodec<UnbakedTrimsModel> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Identifier.CODEC.fieldOf("base_path").forGetter(UnbakedTrimsModel::basePath),
            Identifier.CODEC.fieldOf("parent").forGetter(UnbakedTrimsModel::parent)
    ).apply(instance, UnbakedTrimsModel::new));

    @Override
    public MapCodec<? extends ItemModel.Unbaked> type() {
        return CODEC;
    }

    @Override
    public ItemModel bake(ItemModel.BakingContext context, Matrix4fc transformation) {
        return ItemModelUtils.select(
                TrimPatternProperty.INSTANCE,
                new EmptyModel.Unbaked(),
                ToolTrimsClient.TRIM_PATTERNS.joinEntries().entrySet().stream().map(pattern ->
                        when(ResourceKey.create(Registries.TRIM_PATTERN, pattern.getKey()), select(
                                new TrimMaterialProperty(),
                                new EmptyModel.Unbaked(),
                                ToolTrimsClient.TRIM_MATERIALS.joinEntries().entrySet().stream().map(material ->
                                        when(ResourceKey.create(Registries.TRIM_MATERIAL, material.getKey()), plainModel(
                                                createModelId(pattern.getValue(), material.getValue())
                                        ))
                                ).toList()
                        ))
                ).toList()
        ).bake(context, transformation);
    }

    public static Identifier createModelId(Identifier basePath, ClientTrimPattern pattern, ClientTrimMaterial material) {
        return basePath.withSuffix("_" + pattern.suffix() + "_" + material.suffix());
    }

    private Identifier createModelId(ClientTrimPattern pattern, ClientTrimMaterial material) {
        return createModelId(basePath, pattern, material);
    }

    @Override
    public void resolveDependencies(Resolver resolver) {
        var materials = ToolTrimsClient.TRIM_MATERIALS.joinEntries().values();
        for (var pattern : ToolTrimsClient.TRIM_PATTERNS.joinEntries().values()) {
            for (var material : materials)
                resolver.markDependency(createModelId(pattern, material));
        }
    }
}
