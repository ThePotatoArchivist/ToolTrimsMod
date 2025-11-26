package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.ToolTrimsPatterns;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.item.equipment.trim.TrimPattern;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class TrimPatternGenerator extends FabricCodecDataProvider<TrimPattern> {
    public TrimPatternGenerator(FabricDataOutput output, CompletableFuture<Provider> registriesFuture) {
        super(output, registriesFuture, PackOutput.Target.DATA_PACK, "trim_pattern", TrimPattern.DIRECT_CODEC);
    }

    private static TrimPattern of(Identifier id) {
        return new TrimPattern(
                Identifier.fromNamespaceAndPath("c", "n"),
                Component.translatable(Util.makeDescriptionId("tool_trim_pattern", id)),
                false
        );
    }

    public static void boostrap(BootstrapContext<TrimPattern> registry) {
        ToolTrimsPatterns.PATTERNS.forEach(pattern -> {
            registry.register(pattern, of(pattern.identifier()));
        });
    }

    @Override
    protected void configure(BiConsumer<Identifier, TrimPattern> biConsumer, Provider wrapperLookup) {
        ToolTrimsPatterns.PATTERNS.forEach(pattern ->
                biConsumer.accept(pattern.identifier(), wrapperLookup.lookupOrThrow(Registries.TRIM_PATTERN).getOrThrow(pattern).value())
        );
    }

    @Override
    public String getName() {
        return "Trim Pattern Definitions";
    }

}
