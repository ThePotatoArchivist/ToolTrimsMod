package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.ToolTrims;
import archives.tater.tooltrims.item.ToolTrimsItems;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;

import dev.worldgen.datapatched.api.DatapatchedRegistries;
import dev.worldgen.datapatched.api.loot.LootModifier;
import dev.worldgen.datapatched.impl.loot.modifier.AddEntries;
import dev.worldgen.datapatched.impl.loot.modifier.CommonData;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static net.minecraft.world.level.storage.loot.entries.LootItem.lootTableItem;

public class LootModifierGenerator extends FabricDynamicRegistryProvider {
    public LootModifierGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    private static ResourceKey<LootModifier> createInject(ResourceKey<LootTable> table) {
        return ResourceKey.create(DatapatchedRegistries.LOOT_MODIFIER, ToolTrims.id("inject/" + table.identifier().getNamespace() + "/" + table.identifier().getPath()));
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        entries.add(createInject(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_RARE), new AddEntries(
                new CommonData(id -> BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_RARE.identifier().equals(id), 0),
                List.of(lootTableItem(ToolTrimsItems.LINEAR_TOOL_TRIM_SMITHING_TEMPLATE).build())
        ));
    }

    @Override
    public String getName() {
        return "Loot Modifiers";
    }
}
