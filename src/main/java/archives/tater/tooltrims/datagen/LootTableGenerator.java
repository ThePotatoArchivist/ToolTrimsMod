package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.item.ToolTrimsItems;
import archives.tater.tooltrims.loot.ToolTrimsLoot;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.EmptyEntry;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class LootTableGenerator extends SimpleFabricLootTableProvider {

    public LootTableGenerator(FabricDataOutput output, CompletableFuture<WrapperLookup> registryLookup) {
        super(output, registryLookup, LootContextTypes.CHEST);
    }

    private static final LootNumberProvider TWO = ConstantLootNumberProvider.create(2);

    private static LootTable.Builder singleton(Item item, @Nullable LootNumberProvider count) {
        var entry = ItemEntry.builder(item);
        if (count != null)
            entry.apply(SetCountLootFunction.builder(count));
        return LootTable.builder().pool(LootPool.builder().with(entry));
    }

    @Override
    public void accept(BiConsumer<RegistryKey<LootTable>, LootTable.Builder> exporter) {
        exporter.accept(ToolTrimsLoot.TRAIL_RUINS_INJECT, singleton(ToolTrimsItems.LINEAR_TOOL_TRIM_SMITHING_TEMPLATE, null));
        exporter.accept(ToolTrimsLoot.PILLAGER_OUTPOST_INJECT, singleton(ToolTrimsItems.TRACKS_TOOL_TRIM_SMITHING_TEMPLATE, TWO));
        exporter.accept(ToolTrimsLoot.ANCIENCT_CITY_INJECT, singleton(ToolTrimsItems.CHARGE_TOOL_TRIM_SMITHING_TEMPLATE, null));
        exporter.accept(ToolTrimsLoot.IGLOO_INJECT, LootTable.builder().pool(
                LootPool.builder()
                    .with(ItemEntry.builder(ToolTrimsItems.FROST_TOOL_TRIM_SMITHING_TEMPLATE).weight(2))
                    .with(EmptyEntry.builder().weight(3))
        ));
        exporter.accept(ToolTrimsLoot.MINESHAFT_INJECT, singleton(ToolTrimsItems.LINEAR_TOOL_TRIM_SMITHING_TEMPLATE, null));
        exporter.accept(ToolTrimsLoot.MANSION_INJECT, singleton(ToolTrimsItems.TRACKS_TOOL_TRIM_SMITHING_TEMPLATE, null));
    }
}
