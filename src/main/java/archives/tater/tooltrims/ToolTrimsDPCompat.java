package archives.tater.tooltrims;

import archives.tater.tooltrims.item.ToolTrimsItems;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Blocks;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ToolTrimsDPCompat {
    public static void register() {
        //noinspection OptionalGetWithoutIsPresent
        ResourceManagerHelper.registerBuiltinResourcePack(new Identifier(ToolTrims.MOD_ID, "legacy"), FabricLoader.getInstance().getModContainer(ToolTrims.MOD_ID).get(), ResourcePackActivationType.NORMAL);
    }

    public static boolean shouldDeleteToolsmithingTable(ArmorStandEntity armorStand) {
        return armorStand.getWorld().getGameRules().getBoolean(ToolTrimsGamerules.DELETE_LEGACY_ITEMS) &&
                (armorStand.getCommandTags().contains("310_toolsmithing_table") || armorStand.getCommandTags().contains("310_place_toolsmithing_table")) &&
                        armorStand.getWorld().getClosestPlayer(armorStand, 6) != null;
    }

    public static void deleteToolsmithingTable(ArmorStandEntity armorStand) {
        if (armorStand.getCommandTags().contains("310_toolsmithing_table") && armorStand.getWorld().getBlockState(armorStand.getBlockPos()).isOf(Blocks.BARREL)) {
            armorStand.getWorld().breakBlock(armorStand.getBlockPos(), false);
        }
        armorStand.dropStack(new ItemStack(Items.OAK_PLANKS, 4));
        armorStand.dropStack(new ItemStack(Items.COPPER_INGOT, 2));
        armorStand.discard();
    }

    public static int getCustomModelData(ItemStack itemStack, int defaultValue) {
        var nbt = itemStack.getNbt();
        if (nbt == null) return defaultValue;
        return nbt.getInt("CustomModelData");
    }

    public static boolean shouldDeleteItem(ItemStack itemStack, @Nullable World world) {
        if (world != null && !world.getGameRules().getBoolean(ToolTrimsGamerules.DELETE_LEGACY_ITEMS)) return false;
        if (!itemStack.isOf(Items.STRUCTURE_BLOCK)) return false;
        var customModelData = getCustomModelData(itemStack, 0);
        return 312001 <= customModelData && customModelData <= 312021;
    }

    public static void upgradeItem(StackReference stackReference) {
        var itemStack = stackReference.get();
        if (itemStack.isOf(Items.STRUCTURE_BLOCK)) {
            var customModelData = getCustomModelData(itemStack, 0);
            if (312001 <= customModelData && customModelData <= 312021) {
                stackReference.set(ItemStack.EMPTY);
                return;
            }
            var item = switch (customModelData) {
                case 313001 -> ToolTrimsItems.LINEAR_TOOL_TRIM_SMITHING_TEMPLATE;
                case 313002 -> ToolTrimsItems.TRACKS_TOOL_TRIM_SMITHING_TEMPLATE;
                case 313003 -> ToolTrimsItems.CHARGE_TOOL_TRIM_SMITHING_TEMPLATE;
                case 313004 -> ToolTrimsItems.FROST_TOOL_TRIM_SMITHING_TEMPLATE;
                default -> null;
            };
            if (item == null) return;
            stackReference.set(new ItemStack(item, itemStack.getCount()));
            return;
        }
//        if (itemStack.isIn(ToolTrimsTags.TRIMMABLE_TOOLS)) {
//
//        }
    }
}
