package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.registry.ToolTrimsPatterns;
import archives.tater.tooltrims.client.resource.ClientTrimPattern;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.trim.TrimPattern;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class ClientTrimPatternGenerator extends FabricCodecDataProvider<ClientTrimPattern> {
    protected ClientTrimPatternGenerator(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(packOutput, registriesFuture, PackOutput.Target.RESOURCE_PACK, ClientTrimPattern.Loader.PATH, ClientTrimPattern.CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, ClientTrimPattern> provider, HolderLookup.Provider registryLookup) {
        for (ResourceKey<TrimPattern> pattern : ToolTrimsPatterns.PATTERNS)
            provider.accept(pattern.identifier(), new ClientTrimPattern(pattern.identifier().getPath()));
    }

    @Override
    public String getName() {
        return "Client Trim Patterns";
    }
}
