package archives.tater.tooltrims.loot;

import archives.tater.tooltrims.ToolTrims;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.util.Identifier;

public class ToolTrimsLoot {
    private static Identifier idInject(Identifier identifier) {
        return  ToolTrims.id("inject/" + identifier.getNamespace() + "/" + identifier.getPath());
    }

    public static final Identifier TRAIL_RUINS_INJECT = idInject(LootTables.TRAIL_RUINS_RARE_ARCHAEOLOGY);
    public static final Identifier PILLAGER_OUTPOST_INJECT = idInject(LootTables.PILLAGER_OUTPOST_CHEST);
    public static final Identifier ANCIENCT_CITY_INJECT = idInject(LootTables.ANCIENT_CITY_CHEST);
    public static final Identifier IGLOO_INJECT = idInject(LootTables.IGLOO_CHEST_CHEST);
    public static final Identifier MINESHAFT_INJECT = idInject(LootTables.ABANDONED_MINESHAFT_CHEST);
    public static final Identifier MANSION_INJECT = idInject(LootTables.WOODLAND_MANSION_CHEST);

    sealed interface LootModifyEntry {
        Identifier targetTableId();
    }

    record AddPoolEntry(
        Identifier targetTableId,
        Identifier injectTableId
    ) implements LootModifyEntry {}

    sealed interface PoolModifyEntry extends LootModifyEntry {
        int poolIndex();
    }

    record ChangeWeightEntry(
            Identifier targetTableId,
            int poolIndex,
            int entryIndex,
            int weightChange
    ) implements PoolModifyEntry {}

    record AddEntry(
            Identifier targetTableId,
            int poolIndex,
            Identifier injectTableId,
            int weight
    ) implements PoolModifyEntry {}

    record DeleteEntry( // Note: Operation is not atomic, so deleting items 1 and 2 will actually delete 1 and 3
            Identifier targetTableId,
            int poolIndex,
            int entryIndex
    ) implements PoolModifyEntry {}

    private static final LootModifyEntry[] MODIFY_ENTRIES = {
            new AddEntry(LootTables.TRAIL_RUINS_RARE_ARCHAEOLOGY, 0, TRAIL_RUINS_INJECT, 1),

            new ChangeWeightEntry(LootTables.PILLAGER_OUTPOST_CHEST, 5, 1, 1),
            new AddEntry(LootTables.PILLAGER_OUTPOST_CHEST, 5, PILLAGER_OUTPOST_INJECT, 1),

            new ChangeWeightEntry(LootTables.ANCIENT_CITY_CHEST, 1, 0, -4),
            new AddEntry(LootTables.ANCIENT_CITY_CHEST, 1, ANCIENCT_CITY_INJECT, 4),

            new AddPoolEntry(LootTables.IGLOO_CHEST_CHEST, IGLOO_INJECT),

            new DeleteEntry(LootTables.ABANDONED_MINESHAFT_CHEST, 0, 5),
            new AddEntry(LootTables.ABANDONED_MINESHAFT_CHEST, 0, MINESHAFT_INJECT, 5),

            new DeleteEntry(LootTables.WOODLAND_MANSION_CHEST, 3, 0),
            new AddEntry(LootTables.WOODLAND_MANSION_CHEST, 3, MANSION_INJECT, 1)
    };

    public static void register() {

        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, builder, source) -> {
            if (!source.isBuiltin()) return;

            var needsModification = false;

            for (var entry : MODIFY_ENTRIES) {
                if (!(entry.targetTableId().equals(id))) continue;

                if (entry instanceof AddPoolEntry addPoolEntry)
                    builder.pool(LootPool.builder()
                            .with(LootTableEntry.builder(addPoolEntry.injectTableId)));
                else if (entry instanceof PoolModifyEntry)
                    needsModification = true;
            }

            if (!needsModification) return;

            ((ReplaceablePools) builder).tooltrims$modifyPoolEntries((entries, index) -> {
                for (var entry : MODIFY_ENTRIES) {
                    if (!(entry.targetTableId().equals(id))) continue;
                    if (!(entry instanceof PoolModifyEntry poolModifyEntry) || poolModifyEntry.poolIndex() != index) continue;

                    if (entry instanceof AddEntry addEntry) {
                        var lootEntry = LootTableEntry.builder(addEntry.injectTableId);
                        if (addEntry.weight != 1)
                            lootEntry.weight(addEntry.weight);
                        entries.add(lootEntry.build());
                    } else if (entry instanceof DeleteEntry deleteEntry) {
                        entries.remove(deleteEntry.entryIndex);
                    } else if (entry instanceof ChangeWeightEntry changeWeightEntry) {
                        if (entries.get(changeWeightEntry.entryIndex) instanceof CopyWithWeight<?> copyableEntry) {
                            entries.set(changeWeightEntry.entryIndex, (LootPoolEntry) copyableEntry.tooltrims$copy(changeWeightEntry.weightChange));
                        }
                    }
                }
            });
        });
    }
}
