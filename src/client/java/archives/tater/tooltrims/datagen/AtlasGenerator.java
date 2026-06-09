package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.ToolTrims;
import archives.tater.tooltrims.client.data.atlas.SingleTrimPermutationsSpriteSource;
import archives.tater.tooltrims.client.ToolTrimsClient;
import archives.tater.tooltrims.client.data.atlas.TrimPermutationsSpriteSource;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;

import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.SpriteSources;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.AtlasIds;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;


public class AtlasGenerator extends FabricCodecDataProvider<List<SpriteSource>> {
    protected AtlasGenerator(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(packOutput, registriesFuture, PackOutput.Target.RESOURCE_PACK, "atlases", SpriteSources.FILE_CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, List<SpriteSource>> provider, HolderLookup.Provider registryLookup) {
        provider.accept(AtlasIds.ITEMS, List.of(
                new TrimPermutationsSpriteSource(ToolTrims.id("trims/color_palettes/key"))
        ));
        provider.accept(ToolTrimsClient.TRIDENT_TRIMS_ATLAS, List.of(
                new SingleTrimPermutationsSpriteSource(ToolTrims.id("trims/tridents/trident_entity"), ToolTrims.id("trims/color_palettes/key"))
        ));
    }

    @Override
    public String getName() {
        return "Atlas";
    }
}
