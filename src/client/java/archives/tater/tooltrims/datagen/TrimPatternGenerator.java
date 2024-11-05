package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.ToolTrimsPatterns;
import archives.tater.tooltrims.item.ToolTrimsItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.item.Item;
import net.minecraft.item.equipment.trim.ArmorTrimPattern;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class TrimPatternGenerator extends FabricCodecDataProvider<ArmorTrimPattern> {
    public TrimPatternGenerator(FabricDataOutput output, CompletableFuture<WrapperLookup> registriesFuture) {
        super(output, registriesFuture, DataOutput.OutputType.DATA_PACK, "trim_pattern", ArmorTrimPattern.CODEC);
    }

    private static ArmorTrimPattern of(Identifier id, Item templateItem) {
        return new ArmorTrimPattern(
                Identifier.of("c", "n"),
                Registries.ITEM.getEntry(templateItem),
                Text.translatable(Util.createTranslationKey("tool_trim_pattern", id)),
                false
        );
    }

    public static void boostrap(Registerable<ArmorTrimPattern> registry) {
        ToolTrimsItems.SMITHING_TEMPLATES.forEach((pattern, template) -> {
            registry.register(pattern, of(pattern.getValue(), template));
        });
    }

    @Override
    protected void configure(BiConsumer<Identifier, ArmorTrimPattern> biConsumer, WrapperLookup wrapperLookup) {
        ToolTrimsPatterns.PATTERNS.forEach(pattern ->
                biConsumer.accept(pattern.getValue(), wrapperLookup.getOrThrow(RegistryKeys.TRIM_PATTERN).getOrThrow(pattern).value())
        );
    }

    @Override
    public String getName() {
        return "Trim Pattern Definitions";
    }

}
