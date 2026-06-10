package archives.tater.tooltrims.client.atlas;

import archives.tater.tooltrims.client.ToolTrimsClient;

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
        var patterns = ToolTrimsClient.TRIM_PATTERNS.joinEntries().values();
        new PalettedPermutations(
                ToolTrimsClient.TRIM_OVERLAYS.trimModels().join().stream()
                        .flatMap(model -> patterns.stream()
                                .map(pattern -> model.basePath().withSuffix("_" + pattern.suffix()))
                        ).toList(),
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
