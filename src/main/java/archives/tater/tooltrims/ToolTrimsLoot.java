package archives.tater.tooltrims;

import archives.tater.tooltrims.item.ToolTrimsItems;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.EmptyEntry;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class ToolTrimsLoot {
    private static void modifyPool(LootTable.Builder tableBuilder, int index, Consumer<LootPool.Builder> modify) {
        AtomicInteger i = new AtomicInteger();
        tableBuilder.modifyPools(pool -> {
            if (index == i.get()) modify.accept(pool);
            i.getAndIncrement();
        });
    }

    record InjectEntry(
            Identifier lootTableId,
            int poolIndex,
            Item item,
            @Nullable LootNumberProvider count,
            int weight
    ) {}

    private static final LootNumberProvider ONE_OR_TWO = UniformLootNumberProvider.create(1, 2);
    private static final LootNumberProvider TWO = ConstantLootNumberProvider.create(2);

    private static final InjectEntry[] injects = {
            new InjectEntry(LootTables.TRAIL_RUINS_RARE_ARCHAEOLOGY, 0, ToolTrimsItems.LINEAR_TOOL_TRIM_SMITHING_TEMPLATE, ONE_OR_TWO, 1),
            new InjectEntry(LootTables.PILLAGER_OUTPOST_CHEST, 5, ToolTrimsItems.TRACKS_TOOL_TRIM_SMITHING_TEMPLATE, TWO, 3),
            new InjectEntry(LootTables.ANCIENT_CITY_CHEST, 1, ToolTrimsItems.CHARGE_TOOL_TRIM_SMITHING_TEMPLATE, ONE_OR_TWO, 3),
    };

    public static void register() {

        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, builder, source) -> {
            if (!source.isBuiltin()) return;

            for (var inject : injects) {
                if (!(inject.lootTableId.equals(id))) continue;

                var entry = ItemEntry.builder(inject.item);

                if (inject.count != null)
                    entry.apply(SetCountLootFunction.builder(inject.count));

                if (inject.weight != 1)
                    entry.weight(inject.weight);

                modifyPool(builder, inject.poolIndex, pool -> pool.with(entry));
            }

            // Special case for igloo
            if (LootTables.IGLOO_CHEST_CHEST.equals(id))
                builder.pool(LootPool.builder()
                    .with(ItemEntry.builder(ToolTrimsItems.FROST_TOOL_TRIM_SMITHING_TEMPLATE).weight(2))
                    .with(EmptyEntry.builder().weight(3))
                );
        });
    }
}
