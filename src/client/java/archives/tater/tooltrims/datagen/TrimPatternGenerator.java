package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.ToolTrimsPatterns;
import archives.tater.tooltrims.item.ToolTrimsItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimPattern;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class TrimPatternGenerator extends FabricCodecDataProvider<TrimPattern> {
    public TrimPatternGenerator(FabricDataOutput output, CompletableFuture<Provider> registriesFuture) {
        super(output, registriesFuture, PackOutput.Target.DATA_PACK, "trim_pattern", TrimPattern.DIRECT_CODEC);
    }

    private static TrimPattern of(ResourceLocation id, Item templateItem) {
        return new TrimPattern(
                ResourceLocation.fromNamespaceAndPath("c", "n"),
                BuiltInRegistries.ITEM.wrapAsHolder(templateItem),
                Component.translatable(Util.makeDescriptionId("tool_trim_pattern", id)),
                false
        );
    }

    public static void boostrap(BootstrapContext<TrimPattern> registry) {
        ToolTrimsItems.SMITHING_TEMPLATES.forEach((pattern, template) -> {
            registry.register(pattern, of(pattern.location(), template));
        });
    }

    @Override
    protected void configure(BiConsumer<ResourceLocation, TrimPattern> biConsumer, Provider wrapperLookup) {
        ToolTrimsPatterns.PATTERNS.forEach(pattern ->
                biConsumer.accept(pattern.location(), wrapperLookup.lookupOrThrow(Registries.TRIM_PATTERN).getOrThrow(pattern).value())
        );
    }

    @Override
    public String getName() {
        return "Trim Pattern Definitions";
    }

}
