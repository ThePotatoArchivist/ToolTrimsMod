package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.ToolTrims;
import archives.tater.tooltrims.client.data.ClientTrimOverlay;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;

import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class TrimOverlayGenerator extends FabricCodecDataProvider<ClientTrimOverlay> {
    protected TrimOverlayGenerator(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(packOutput, registriesFuture, PackOutput.Target.RESOURCE_PACK, ClientTrimOverlay.Loader.PATH, ClientTrimOverlay.CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, ClientTrimOverlay> provider, HolderLookup.Provider registryLookup) {
        provider.accept(ToolTrims.id("sword"), new ClientTrimOverlay(ItemModelUtils.plainModel(Items.EGG.builtInRegistryHolder().key().identifier()), List.of(Items.DIAMOND_SWORD.builtInRegistryHolder().key().identifier())));
    }

    @Override
    public String getName() {
        return "Trim Overlays";
    }
}
