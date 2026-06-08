package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.ToolTrims;
import archives.tater.tooltrims.client.UnbakedTrimsModel;
import archives.tater.tooltrims.client.data.ClientTrimOverlay;
import archives.tater.tooltrims.mixin.client.ModelTemplateAccessor;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;

import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class ClientTrimOverlayGenerator extends FabricCodecDataProvider<ClientTrimOverlay> {
    protected ClientTrimOverlayGenerator(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(packOutput, registriesFuture, PackOutput.Target.RESOURCE_PACK, ClientTrimOverlay.Loader.PATH, ClientTrimOverlay.CODEC);
    }

    private static final Identifier HANDHELD = ((ModelTemplateAccessor) ModelTemplates.FLAT_HANDHELD_ITEM).getModel().orElseThrow();

    @Override
    protected void configure(BiConsumer<Identifier, ClientTrimOverlay> provider, HolderLookup.Provider registryLookup) {
        provider.accept(ToolTrims.id("sword"), new ClientTrimOverlay(
                new UnbakedTrimsModel(ToolTrims.id("trims/item/diamond_sword"), HANDHELD),
                List.of(Items.DIAMOND_SWORD.builtInRegistryHolder().key().identifier())
        ));
    }

    @Override
    public String getName() {
        return "Trim Overlays";
    }
}
