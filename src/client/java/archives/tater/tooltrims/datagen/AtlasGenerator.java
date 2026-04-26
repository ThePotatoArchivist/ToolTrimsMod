package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.ToolTrims;
import archives.tater.tooltrims.ToolTrimsPatterns;

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
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;


public class AtlasGenerator extends FabricCodecDataProvider<List<SpriteSource>> {
    protected AtlasGenerator(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(packOutput, registriesFuture, PackOutput.Target.RESOURCE_PACK, "atlases", SpriteSources.FILE_CODEC);
    }

    private static final List<String> MATERIALS = List.of("wooden", "stone", "copper", "iron", "golden", "diamond", "netherite");
    private static final List<String> MATERIAL_TOOLS = List.of("sword", "spear", "spear_in_hand", "pickaxe", "axe", "shovel", "hoe");
    private static final List<String> UNIQUE_TOOLS = List.of(
            "mace",
            "trident",
            // TODO: trident entity
            "bow",
            "bow_pulling_0",
            "bow_pulling_1",
            "bow_pulling_2",
            "crossbow_standby",
            "crossbow_pulling_0",
            "crossbow_pulling_1",
            "crossbow_pulling_2",
            "crossbow_arrow",
            "crossbow_firework"
    );
    private static final List<Identifier> TOOLS = Stream.concat(
            MATERIALS.stream().flatMap(material ->
                    MATERIAL_TOOLS.stream().map(tool ->
                            material + "_" + tool
                    )
            ),
            UNIQUE_TOOLS.stream()
    ).flatMap(tool ->
            ToolTrimsPatterns.PATTERNS.stream().map(pattern ->
                    ToolTrims.id("trims/item/" + tool + "_" + pattern.identifier().getPath())
            )
    ).toList();

    @Override
    protected void configure(BiConsumer<Identifier, List<SpriteSource>> provider, HolderLookup.Provider registryLookup) {
        provider.accept(AtlasIds.ITEMS, List.of(
                new PalettedPermutations(
                        TOOLS,
                        ToolTrims.id("trims/color_palettes/key"),
                        ModelGenerator.MATERIALS.stream().map(trimMaterialData -> trimMaterialData.materialKey().identifier().getPath()).collect(toMap(
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
