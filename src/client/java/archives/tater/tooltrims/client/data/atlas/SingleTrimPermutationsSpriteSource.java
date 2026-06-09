package archives.tater.tooltrims.client.data.atlas;

import archives.tater.tooltrims.client.ToolTrimsClient;

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
        var patterns = ToolTrimsClient.TRIM_PATTERNS.joinEntries().values();
        new PalettedPermutations(
                patterns.stream()
                        .map(pattern -> basePath.withSuffix("_" + pattern.suffix()))
                        .toList(),
                paletteKey,
                ToolTrimsClient.TRIM_MATERIALS.joinEntries().values().stream()
                        .map(material -> entry(material.suffix(), material.assetName()))
                        .collect(toMap())
        ).run(resourceManager, output);
    }

    @Override
    public MapCodec<? extends SpriteSource> codec() {
        return CODEC;
    }
}
