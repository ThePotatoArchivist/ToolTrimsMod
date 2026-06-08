package archives.tater.tooltrims.client;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.sources.PalettedPermutations;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;

import static java.util.Map.entry;
import static net.minecraft.util.Util.toMap;

public record TrimPermutationsSpriteSource(Identifier paletteKey) implements SpriteSource {
    public static final MapCodec<TrimPermutationsSpriteSource> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Identifier.CODEC.fieldOf("palette_key").forGetter(TrimPermutationsSpriteSource::paletteKey)
    ).apply(instance, TrimPermutationsSpriteSource::new));

    @Override
    public void run(ResourceManager resourceManager, Output output) {
        new PalettedPermutations(
                ToolTrimsClient.TRIM_OVERLAYS.trimModels().stream()
                        .flatMap(model -> ToolTrimsClient.TRIM_PATTERNS.entries().values().stream()
                                .map(pattern -> model.basePath().withSuffix("_" + pattern.suffix()))
                        ).toList(),
                paletteKey,
                ToolTrimsClient.TRIM_MATERIALS.entries().values().stream()
                        .map(material -> entry(material.suffix(), material.assetName()))
                        .collect(toMap())
        ).run(resourceManager, output);
    }

    @Override
    public MapCodec<? extends SpriteSource> codec() {
        return CODEC;
    }
}
