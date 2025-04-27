package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.ToolTrimsTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ItemTagGenerator extends FabricTagProvider.ItemTagProvider {

    public ItemTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture, null);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(ToolTrimsTags.TRIMMABLE_TOOLS).add(
                Items.WOODEN_SWORD,
                Items.WOODEN_SHOVEL,
                Items.WOODEN_PICKAXE,
                Items.WOODEN_AXE,
                Items.WOODEN_HOE,
                Items.STONE_SWORD,
                Items.STONE_SHOVEL,
                Items.STONE_PICKAXE,
                Items.STONE_AXE,
                Items.STONE_HOE,
                Items.GOLDEN_SWORD,
                Items.GOLDEN_SHOVEL,
                Items.GOLDEN_PICKAXE,
                Items.GOLDEN_AXE,
                Items.GOLDEN_HOE,
                Items.IRON_SWORD,
                Items.IRON_SHOVEL,
                Items.IRON_PICKAXE,
                Items.IRON_AXE,
                Items.IRON_HOE,
                Items.DIAMOND_SWORD,
                Items.DIAMOND_SHOVEL,
                Items.DIAMOND_PICKAXE,
                Items.DIAMOND_AXE,
                Items.DIAMOND_HOE,
                Items.NETHERITE_SWORD,
                Items.NETHERITE_SHOVEL,
                Items.NETHERITE_PICKAXE,
                Items.NETHERITE_AXE,
                Items.NETHERITE_HOE,
                Items.BOW,
                Items.CROSSBOW,
                Items.TRIDENT
        );
        getOrCreateTagBuilder(ToolTrimsTags.TOOL_TRIM_MATERIALS).add(
                Items.IRON_INGOT,
                Items.COPPER_INGOT,
                Items.GOLD_INGOT,
                Items.LAPIS_LAZULI,
                Items.EMERALD,
                Items.DIAMOND,
                Items.NETHERITE_INGOT,
                Items.REDSTONE,
                Items.QUARTZ,
                Items.AMETHYST_SHARD
        );
    }
}
