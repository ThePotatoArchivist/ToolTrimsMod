package archives.tater.tooltrims.client;

import archives.tater.tooltrims.client.data.ClientTrimMaterial;
import archives.tater.tooltrims.client.data.ClientTrimPattern;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.item.EmptyModel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.properties.select.TrimMaterialProperty;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import org.joml.Matrix4fc;

import static net.minecraft.client.data.models.model.ItemModelUtils.*;

public record UnbakedTrimsModel(Identifier basePath) implements ItemModel.Unbaked {
    public static final MapCodec<UnbakedTrimsModel> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Identifier.CODEC.fieldOf("base_path").forGetter(UnbakedTrimsModel::basePath)
    ).apply(instance, UnbakedTrimsModel::new));

    @Override
    public MapCodec<? extends ItemModel.Unbaked> type() {
        return CODEC;
    }

    @Override
    public ItemModel bake(ItemModel.BakingContext context, Matrix4fc transformation) {
        return select(
                TrimPatternProperty.INSTANCE,
                new EmptyModel.Unbaked(),
                ToolTrimsClient.TRIM_PATTERNS.entries().entrySet().stream().map(pattern -> 
                        when(ResourceKey.create(Registries.TRIM_PATTERN, pattern.getKey()), select(
                                new TrimMaterialProperty(),
                                new EmptyModel.Unbaked(),
                                ToolTrimsClient.TRIM_MATERIALS.entries().entrySet().stream().map(material -> 
                                        when(ResourceKey.create(Registries.TRIM_MATERIAL, material.getKey()), plainModel(
                                                createModelId(pattern.getValue(), material.getValue())
                                        ))
                                ).toList()
                        ))
                ).toList()
        ).bake(context, transformation);
    }

    public static Identifier createModelId(Identifier basePath, ClientTrimPattern pattern, ClientTrimMaterial material) {
        return basePath.withSuffix(pattern.suffix() + "_" + material.suffix());
    }

    private Identifier createModelId(ClientTrimPattern pattern, ClientTrimMaterial material) {
        return createModelId(basePath, pattern, material);
    }

    @Override
    public void resolveDependencies(Resolver resolver) {
        for (var pattern : ToolTrimsClient.TRIM_PATTERNS.entries().values())
            for (var material : ToolTrimsClient.TRIM_MATERIALS.entries().values())
                resolver.markDependency(createModelId(pattern, material));
    }
}
