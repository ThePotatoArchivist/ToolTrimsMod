package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.ToolTrims;
import archives.tater.tooltrims.client.data.ClientTrimMaterial;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;

import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class ClientTrimMaterialGenerator extends FabricCodecDataProvider<ClientTrimMaterial> {
    protected ClientTrimMaterialGenerator(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(packOutput, registriesFuture, PackOutput.Target.RESOURCE_PACK, ClientTrimMaterial.Loader.PATH, ClientTrimMaterial.CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, ClientTrimMaterial> provider, HolderLookup.Provider registryLookup) {
        for (var trimMaterialModel : ItemModelGenerators.TRIM_MATERIAL_MODELS)
            provider.accept(trimMaterialModel.materialKey().identifier(), new ClientTrimMaterial(ToolTrims.id("trims/color_palettes/" + trimMaterialModel.assets().base().suffix()), trimMaterialModel.assets().base().suffix()));
    }

    @Override
    public String getName() {
        return "Client Trim Materials";
    }
}
