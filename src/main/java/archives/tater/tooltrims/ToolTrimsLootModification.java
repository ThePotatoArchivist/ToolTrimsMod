package archives.tater.tooltrims;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;

import static net.minecraft.world.level.storage.loot.LootPool.lootPool;
import static net.minecraft.world.level.storage.loot.entries.NestedLootTable.lootTableReference;

public class ToolTrimsLootModification {
    private ToolTrimsLootModification() {}

    public static final ResourceKey<LootTable> ABANDONED_MINESHAFT = createInject(BuiltInLootTables.ABANDONED_MINESHAFT);
    public static final ResourceKey<LootTable> ANCIENT_CITY = createInject(BuiltInLootTables.ANCIENT_CITY);
    public static final ResourceKey<LootTable> IGLOO_CHEST = createInject(BuiltInLootTables.IGLOO_CHEST);
    public static final ResourceKey<LootTable> PILLAGER_OUTPOST = createInject(BuiltInLootTables.PILLAGER_OUTPOST);
    public static final ResourceKey<LootTable> WOODLAND_MANSION = createInject(BuiltInLootTables.WOODLAND_MANSION);
    public static final ResourceKey<LootTable> TRAIL_RUINS_ARCHAEOLOGY_RARE = createInject(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_RARE);

    private static ResourceKey<LootTable> createInject(ResourceKey<LootTable> key) {
        return ResourceKey.create(Registries.LOOT_TABLE, ToolTrims.id("inject/" + key.identifier().getPath()));
    }

    public static void init() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, holder) -> {
            if (!source.isBuiltin()) return;

            if (key == BuiltInLootTables.ABANDONED_MINESHAFT) addTablePool(tableBuilder, holder.getOrThrow(ABANDONED_MINESHAFT));
            if (key == BuiltInLootTables.ANCIENT_CITY) addTablePool(tableBuilder, holder.getOrThrow(ANCIENT_CITY));
            if (key == BuiltInLootTables.IGLOO_CHEST) addTablePool(tableBuilder, holder.getOrThrow(IGLOO_CHEST));
            if (key == BuiltInLootTables.PILLAGER_OUTPOST) addTablePool(tableBuilder, holder.getOrThrow(PILLAGER_OUTPOST));
            if (key == BuiltInLootTables.WOODLAND_MANSION) addTablePool(tableBuilder, holder.getOrThrow(WOODLAND_MANSION));
            if (key == BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_RARE) tableBuilder.modifyPools(builder -> builder.add(lootTableReference(holder.getOrThrow(TRAIL_RUINS_ARCHAEOLOGY_RARE))));
        });
    }

    private static void addTablePool(LootTable.Builder tableBuilder, Holder.Reference<LootTable> table) {
        tableBuilder.withPool(lootPool().add(lootTableReference(table)));
    }
}
