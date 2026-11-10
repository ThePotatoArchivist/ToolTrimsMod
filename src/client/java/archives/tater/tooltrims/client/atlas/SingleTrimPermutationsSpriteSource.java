package archives.tater.tooltrims.client.atlas;

import archives.tater.tooltrims.client.resource.ClientTrimLoader;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.sources.PalettedPermutations;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;

import static java.util.Map.entry;
import static net.minecraft.util.Util.toMap;

public record SingleTrimPermutationsSpriteSource(Identifier basePath, Identifier paletteKey) implements SpriteSource {
    public static final MapCodec<SingleTrimPermutationsSpriteSource> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Identifier.CODEC.fieldOf("base_path").forGetter(SingleTrimPermutationsSpriteSource::basePath),
            Identifier.CODEC.fieldOf("palette_key").forGetter(SingleTrimPermutationsSpriteSource::paletteKey)
    ).apply(instance, SingleTrimPermutationsSpriteSource::new));

    @Override
    public void run(ResourceManager resourceManager, Output output) {
        var result = ClientTrimLoader.getPendingResult().join();
        new PalettedPermutations(
                result.patterns().values().stream()
                        .map(pattern -> basePath.withSuffix("_" + pattern.suffix()))
                        .toList(),
                paletteKey,
                result.materials().values().stream()
                        .map(material -> entry(material.suffix(), material.assetName()))
                        .collect(toMap())
        ).run(resourceManager, output);
    }

    @Override
    public MapCodec<? extends SpriteSource> codec() {
        return CODEC;
    }
}
