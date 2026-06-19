package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.ToolTrims;
import archives.tater.tooltrims.client.resource.ClientTrimMaterial;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;

import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions.allModsLoaded;

public class ClientTrimMaterialGenerator extends ConditionalCodecDataProvider<ClientTrimMaterial> {
    protected ClientTrimMaterialGenerator(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(packOutput, registriesFuture, PackOutput.Target.RESOURCE_PACK, ClientTrimMaterial.Loader.PATH, ClientTrimMaterial.CODEC);
    }

    @Override
    protected void configureC(BiConsumer<Identifier, ClientTrimMaterial> provider, HolderLookup.Provider registryLookup) {
        for (var trimMaterialModel : ItemModelGenerators.TRIM_MATERIAL_MODELS)
            material(
                    provider,
                    trimMaterialModel.materialKey().identifier(),
                    ToolTrims.id(trimMaterialModel.materialKey().identifier().getPath()),
                    trimMaterialModel.assets().base().suffix()
            );

        materials(provider, "end_reborn", "crystalline", "featherzeal", "remnant");
        materials(provider, "progression_reborn", "rose");
        materials(provider, "enderite", "enderite");
    }

    private void materials(BiConsumer<Identifier, ClientTrimMaterial> provider, String namespace, String... paths) {
        var conditioned = withConditions(provider, allModsLoaded(namespace));
        for (var path : paths)
            material(conditioned, Identifier.fromNamespaceAndPath(namespace, path));
    }

    private static void material(BiConsumer<Identifier, ClientTrimMaterial> provider, Identifier id) {
        material(provider, id, id, id.getNamespace() + "_" + id.getPath());
    }

    private static void material(BiConsumer<Identifier, ClientTrimMaterial> provider, Identifier id, Identifier texture, String suffix) {
        provider.accept(id, new ClientTrimMaterial(texture.withPrefix("trims/color_palettes/"), suffix));
    }

    @Override
    public String getName() {
        return "Client Trim Materials";
    }
}
