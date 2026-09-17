package archives.tater.tooltrims.datagen;

import archives.tater.lootinj.api.LootInj;
import archives.tater.lootinj.api.LootModification;
import archives.tater.tooltrims.ToolTrims;
import archives.tater.tooltrims.registry.ToolTrimsItems;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static net.minecraft.world.level.storage.loot.LootPool.lootPool;
import static net.minecraft.world.level.storage.loot.entries.EmptyLootItem.emptyItem;
import static net.minecraft.world.level.storage.loot.entries.LootItem.lootTableItem;
import static net.minecraft.world.level.storage.loot.functions.SetItemCountFunction.setCount;
import static net.minecraft.world.level.storage.loot.providers.number.ConstantValue.exactly;

public class LootModificationGenerator extends FabricCodecDataProvider<LootModification> {

    protected LootModificationGenerator(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(packOutput, registriesFuture, LootInj.LOOT_MODIFICATION, LootModification.CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, LootModification> entries, HolderLookup.Provider registryLookup) {
        registerModification(entries, BuiltInLootTables.ABANDONED_MINESHAFT, LootModification.builder()
                .pool(lootPool()
                        .add(lootTableItem(ToolTrimsItems.LINEAR_TEMPLATE)
                                .setWeight(5))
                        .add(emptyItem()
                                .setWeight(66)))
        );
        registerModification(entries, BuiltInLootTables.ANCIENT_CITY, LootModification.builder()
                .pool(lootPool()
                        .add(lootTableItem(ToolTrimsItems.CHARGE_TEMPLATE)
                                .setWeight(4))
                        .add(emptyItem()
                                .setWeight(76)))
        );
        registerModification(entries, BuiltInLootTables.IGLOO_CHEST, LootModification.builder()
                .pool(lootPool()
                        .add(lootTableItem(ToolTrimsItems.FROST_TEMPLATE)
                                .setWeight(2))
                        .add(emptyItem()
                                .setWeight(3)))
        );
        registerModification(entries, BuiltInLootTables.PILLAGER_OUTPOST, LootModification.builder()
                .pool(lootPool()
                        .add(lootTableItem(ToolTrimsItems.TRACKS_TEMPLATE)
                                .apply(setCount(exactly(2)))
                                .setWeight(3))
                        .add(emptyItem()
                                .setWeight(5)))
        );
        registerModification(entries, BuiltInLootTables.WOODLAND_MANSION, LootModification.builder()
                .pool(lootPool()
                        .add(lootTableItem(ToolTrimsItems.TRACKS_TEMPLATE)
                                .setWeight(1))
                        .add(emptyItem()
                                .setWeight(1)))
        );
        registerModification(entries, BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_RARE, LootModification.builder()
                .modifyPools(builder -> builder
                        .add(lootTableItem(ToolTrimsItems.LINEAR_TEMPLATE)))
        );
    }

    private static void registerModification(BiConsumer<Identifier, LootModification> entries, ResourceKey<LootTable> target, LootModification.Builder builder) {
        entries.accept(ToolTrims.id(target.identifier().getPath()), builder.target(target).build());
    }

    @Override
    public String getName() {
        return "Loot Modifications";
    }
}
