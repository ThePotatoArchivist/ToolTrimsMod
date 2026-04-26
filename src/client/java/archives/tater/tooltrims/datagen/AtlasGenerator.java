package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.ToolTrims;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;

import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.SpriteSources;
import net.minecraft.client.renderer.texture.atlas.sources.PalettedPermutations;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.AtlasIds;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;


public class AtlasGenerator extends FabricCodecDataProvider<List<SpriteSource>> {
    protected AtlasGenerator(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(packOutput, registriesFuture, PackOutput.Target.RESOURCE_PACK, "atlases", SpriteSources.FILE_CODEC);
    }

    public static final List<Identifier> TEMPLATE_TEXTURES = TrimAssets.TEMPLATES.stream()
            .map(id -> id.withPrefix("trims/item/"))
            .toList();

    @Override
    protected void configure(BiConsumer<Identifier, List<SpriteSource>> provider, HolderLookup.Provider registryLookup) {
        provider.accept(AtlasIds.ITEMS, List.of(
                new PalettedPermutations(
                        TEMPLATE_TEXTURES,
                        ToolTrims.id("trims/color_palettes/key"),
                        TrimAssets.TRIM_MATERIALS.stream().map(trimMaterialData -> trimMaterialData.materialKey().identifier().getPath()).collect(toMap(
                                Function.identity(),
                                path -> ToolTrims.id("trims/color_palettes/" + path)
                        ))
                )
        ));
    }

    @Override
    public String getName() {
        return "Atlas";
    }
}
