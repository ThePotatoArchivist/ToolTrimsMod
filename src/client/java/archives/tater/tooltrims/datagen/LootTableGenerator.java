package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.ToolTrimsLootModification;
import archives.tater.tooltrims.registry.ToolTrimsItems;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableSubProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static net.minecraft.world.level.storage.loot.LootPool.lootPool;
import static net.minecraft.world.level.storage.loot.LootTable.lootTable;
import static net.minecraft.world.level.storage.loot.entries.EmptyLootItem.emptyItem;
import static net.minecraft.world.level.storage.loot.entries.LootItem.lootTableItem;
import static net.minecraft.world.level.storage.loot.functions.SetItemCountFunction.setCount;
import static net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders.exactly;

public class LootTableGenerator extends SimpleFabricLootTableSubProvider {

    public LootTableGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture, LootContextParamSets.CHEST);
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> biConsumer) {
        biConsumer.accept(ToolTrimsLootModification.ABANDONED_MINESHAFT, lootTable()
                .withPool(lootPool()
                        .add(lootTableItem(ToolTrimsItems.LINEAR_TEMPLATE)
                                .setWeight(5))
                        .add(emptyItem()
                                .setWeight(66)))
        );
        biConsumer.accept(ToolTrimsLootModification.ANCIENT_CITY, lootTable()
                .withPool(lootPool()
                        .add(lootTableItem(ToolTrimsItems.CHARGE_TEMPLATE)
                                .setWeight(4))
                        .add(emptyItem()
                                .setWeight(76)))
        );
        biConsumer.accept(ToolTrimsLootModification.IGLOO_CHEST, lootTable()
                .withPool(lootPool()
                        .add(lootTableItem(ToolTrimsItems.FROST_TEMPLATE)
                                .setWeight(2))
                        .add(emptyItem()
                                .setWeight(3)))
        );
        biConsumer.accept(ToolTrimsLootModification.PILLAGER_OUTPOST, lootTable()
                .withPool(lootPool()
                        .add(lootTableItem(ToolTrimsItems.TRACKS_TEMPLATE)
                                .apply(setCount(exactly(2)))
                                .setWeight(3))
                        .add(emptyItem()
                                .setWeight(5)))
        );
        biConsumer.accept(ToolTrimsLootModification.WOODLAND_MANSION, lootTable()
                .withPool(lootPool()
                        .add(lootTableItem(ToolTrimsItems.TRACKS_TEMPLATE)
                                .setWeight(1))
                        .add(emptyItem()
                                .setWeight(1)))
        );
        biConsumer.accept(ToolTrimsLootModification.TRAIL_RUINS_ARCHAEOLOGY_RARE, lootTable()
                .withPool(lootPool()
                        .add(lootTableItem(ToolTrimsItems.LINEAR_TEMPLATE)))
        );
    }

    @Override
    public void run() {

    }
}
