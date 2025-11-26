package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.item.ToolTrimsItems;
import archives.tater.tooltrims.loot.ToolTrimsLoot;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class LootTableGenerator extends SimpleFabricLootTableProvider {

    public LootTableGenerator(FabricDataOutput output, CompletableFuture<Provider> registryLookup) {
        super(output, registryLookup, LootContextParamSets.CHEST);
    }

    private static final NumberProvider TWO = ConstantValue.exactly(2);

    private static LootTable.Builder singleton(Item item, @Nullable NumberProvider count) {
        var entry = LootItem.lootTableItem(item);
        if (count != null)
            entry.apply(SetItemCountFunction.setCount(count));
        return LootTable.lootTable().withPool(LootPool.lootPool().add(entry));
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> exporter) {
        exporter.accept(ToolTrimsLoot.TRAIL_RUINS_INJECT, singleton(ToolTrimsItems.LINEAR_TOOL_TRIM_SMITHING_TEMPLATE, null));
        exporter.accept(ToolTrimsLoot.PILLAGER_OUTPOST_INJECT, singleton(ToolTrimsItems.TRACKS_TOOL_TRIM_SMITHING_TEMPLATE, TWO));
        exporter.accept(ToolTrimsLoot.ANCIENCT_CITY_INJECT, singleton(ToolTrimsItems.CHARGE_TOOL_TRIM_SMITHING_TEMPLATE, null));
        exporter.accept(ToolTrimsLoot.IGLOO_INJECT, LootTable.lootTable().withPool(
                LootPool.lootPool()
                    .add(LootItem.lootTableItem(ToolTrimsItems.FROST_TOOL_TRIM_SMITHING_TEMPLATE).setWeight(2))
                    .add(EmptyLootItem.emptyItem().setWeight(3))
        ));
        exporter.accept(ToolTrimsLoot.MINESHAFT_INJECT, singleton(ToolTrimsItems.LINEAR_TOOL_TRIM_SMITHING_TEMPLATE, null));
        exporter.accept(ToolTrimsLoot.MANSION_INJECT, singleton(ToolTrimsItems.TRACKS_TOOL_TRIM_SMITHING_TEMPLATE, null));
    }
}
