package archives.tater.tooltrims;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.util.Identifier;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class ToolTrimsLoot {
    private static Identifier idInject(Identifier identifier) {
        return  ToolTrims.id("inject/" + identifier.getNamespace() + "/" + identifier.getPath());
    }

    public static final Identifier TRAIL_RUINS_INJECT = idInject(LootTables.TRAIL_RUINS_RARE_ARCHAEOLOGY);
    public static final Identifier PILLAGER_OUTPOST_INJECT = idInject(LootTables.PILLAGER_OUTPOST_CHEST);
    public static final Identifier ANCIENCT_CITY_INJECT = idInject(LootTables.ANCIENT_CITY_CHEST);
    public static final Identifier IGLOO_INJECT = idInject(LootTables.IGLOO_CHEST_CHEST);

    private static void modifyPool(LootTable.Builder tableBuilder, int index, Consumer<LootPool.Builder> modify) {
        AtomicInteger i = new AtomicInteger();
        tableBuilder.modifyPools(pool -> {
            if (index == i.get()) modify.accept(pool);
            i.getAndIncrement();
        });
    }

    record InjectEntry(
            Identifier targetTableId,
            int poolIndex,
            Identifier injectTableId,
            int weight
    ) {}

    private static final InjectEntry[] injects = {
            new InjectEntry(LootTables.TRAIL_RUINS_RARE_ARCHAEOLOGY, 0, TRAIL_RUINS_INJECT, 1),
            new InjectEntry(LootTables.PILLAGER_OUTPOST_CHEST, 5, PILLAGER_OUTPOST_INJECT, 3),
            new InjectEntry(LootTables.ANCIENT_CITY_CHEST, 1, ANCIENCT_CITY_INJECT, 3),
    };

    public static void register() {

        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, builder, source) -> {
            if (!source.isBuiltin()) return;

            for (var inject : injects) {
                if (!(inject.targetTableId.equals(id))) continue;

                var entry = LootTableEntry.builder(inject.injectTableId);

                if (inject.weight != 1)
                    entry.weight(inject.weight);

                modifyPool(builder, inject.poolIndex, pool -> pool.with(entry));
            }

            // Special case for igloo
            if (LootTables.IGLOO_CHEST_CHEST.equals(id))
                builder.pool(LootPool.builder()
                    .with(LootTableEntry.builder(IGLOO_INJECT)));
        });
    }
}
