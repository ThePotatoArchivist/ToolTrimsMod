package archives.tater.tooltrims.loot;

import archives.tater.tooltrims.ToolTrims;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;

public class ToolTrimsLoot {
    private static ResourceKey<LootTable> idInject(ResourceKey<LootTable> lootTable) {
        return ResourceKey.create(lootTable.registryKey(), ToolTrims.id("inject/" + lootTable.location().getNamespace() + "/" + lootTable.location().getPath()));
    }

    public static final ResourceKey<LootTable> TRAIL_RUINS_INJECT = idInject(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_RARE);
    public static final ResourceKey<LootTable> PILLAGER_OUTPOST_INJECT = idInject(BuiltInLootTables.PILLAGER_OUTPOST);
    public static final ResourceKey<LootTable> ANCIENCT_CITY_INJECT = idInject(BuiltInLootTables.ANCIENT_CITY);
    public static final ResourceKey<LootTable> IGLOO_INJECT = idInject(BuiltInLootTables.IGLOO_CHEST);
    public static final ResourceKey<LootTable> MINESHAFT_INJECT = idInject(BuiltInLootTables.ABANDONED_MINESHAFT);
    public static final ResourceKey<LootTable> MANSION_INJECT = idInject(BuiltInLootTables.WOODLAND_MANSION);

    sealed interface LootModifyEntry {
        ResourceKey<LootTable> targetTable();
    }

    record AddPoolEntry(
        ResourceKey<LootTable> targetTable,
        ResourceKey<LootTable> injectTableId
    ) implements LootModifyEntry {}

    sealed interface PoolModifyEntry extends LootModifyEntry {
        int poolIndex();
    }

    record ChangeWeightEntry(
            ResourceKey<LootTable> targetTable,
            int poolIndex,
            int entryIndex,
            int weightChange
    ) implements PoolModifyEntry {}

    record AddEntry(
            ResourceKey<LootTable> targetTable,
            int poolIndex,
            ResourceKey<LootTable> injectTable,
            int weight
    ) implements PoolModifyEntry {}

    record DeleteEntry( // Note: Operation is not atomic, so deleting items 1 and 2 will actually delete 1 and 3
            ResourceKey<LootTable> targetTable,
            int poolIndex,
            int entryIndex
    ) implements PoolModifyEntry {}

    private static final LootModifyEntry[] MODIFY_ENTRIES = {
            new AddEntry(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_RARE, 0, TRAIL_RUINS_INJECT, 1),

            new ChangeWeightEntry(BuiltInLootTables.PILLAGER_OUTPOST, 5, 1, 1),
            new AddEntry(BuiltInLootTables.PILLAGER_OUTPOST, 5, PILLAGER_OUTPOST_INJECT, 3),

            new ChangeWeightEntry(BuiltInLootTables.ANCIENT_CITY, 1, 0, -4),
            new AddEntry(BuiltInLootTables.ANCIENT_CITY, 1, ANCIENCT_CITY_INJECT, 4),

            new AddPoolEntry(BuiltInLootTables.IGLOO_CHEST, IGLOO_INJECT),

            new DeleteEntry(BuiltInLootTables.ABANDONED_MINESHAFT, 0, 5),
            new AddEntry(BuiltInLootTables.ABANDONED_MINESHAFT, 0, MINESHAFT_INJECT, 5),

            new DeleteEntry(BuiltInLootTables.WOODLAND_MANSION, 3, 0),
            new AddEntry(BuiltInLootTables.WOODLAND_MANSION, 3, MANSION_INJECT, 1)
    };

    public static void register() {

        LootTableEvents.MODIFY.register((key, builder, source, wrapperLookup) -> {
            if (!source.isBuiltin()) return;

            var needsModification = false;

            for (var entry : MODIFY_ENTRIES) {
                if (!(entry.targetTable().location().equals(key.location()))) continue;

                if (entry instanceof AddPoolEntry addPoolEntry)
                    builder.withPool(LootPool.lootPool()
                            .add(NestedLootTable.lootTableReference(addPoolEntry.injectTableId)));
                else if (entry instanceof PoolModifyEntry)
                    needsModification = true;
            }

            if (!needsModification) return;

            ((ReplaceablePools) builder).tooltrims$modifyPoolEntries((entries, index) -> {
                for (var entry : MODIFY_ENTRIES) {
                    if (!(entry.targetTable().location().equals(key.location()))) continue;
                    if (!(entry instanceof PoolModifyEntry poolModifyEntry) || poolModifyEntry.poolIndex() != index) continue;

                    switch (entry) {
                        case AddEntry addEntry -> {
                            var lootEntry = NestedLootTable.lootTableReference(addEntry.injectTable);
                            if (addEntry.weight != 1)
                                lootEntry.setWeight(addEntry.weight);
                            entries.add(lootEntry.build());
                        }
                        case DeleteEntry deleteEntry -> entries.remove(deleteEntry.entryIndex);
                        case ChangeWeightEntry changeWeightEntry -> {
                            if (changeWeightEntry.entryIndex < entries.size() && entries.get(changeWeightEntry.entryIndex) instanceof CopyWithWeight<?> copyableEntry)
                                entries.set(changeWeightEntry.entryIndex, (LootPoolEntryContainer) copyableEntry.tooltrims$copy(changeWeightEntry.weightChange));
                        }
                        default -> {
                        }
                    }
                }
            });
        });
    }
}
