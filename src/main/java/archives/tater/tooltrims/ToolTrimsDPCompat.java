package archives.tater.tooltrims;

import archives.tater.tooltrims.item.ToolTrimsItems;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Blocks;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.item.trim.ArmorTrimMaterials;
import net.minecraft.item.trim.ArmorTrimPattern;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ToolTrimsDPCompat {
    public static final List<RegistryKey<ArmorTrimMaterial>> legacyMaterialOrder = List.of(
            ArmorTrimMaterials.AMETHYST,
            ArmorTrimMaterials.COPPER,
            ArmorTrimMaterials.DIAMOND,
            ArmorTrimMaterials.EMERALD,
            ArmorTrimMaterials.GOLD,
            ArmorTrimMaterials.IRON,
            ArmorTrimMaterials.LAPIS,
            ArmorTrimMaterials.NETHERITE,
            ArmorTrimMaterials.QUARTZ,
            ArmorTrimMaterials.REDSTONE
    );

    public static final List<RegistryKey<ArmorTrimPattern>> legacyPatternOrder = List.of(
            ToolTrimsPatterns.LINEAR,
            ToolTrimsPatterns.TRACKS,
            ToolTrimsPatterns.CHARGE,
            ToolTrimsPatterns.FROST
    );

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

    public static int getCustomModelData(RegistryKey<ArmorTrimMaterial> material, RegistryKey<ArmorTrimPattern> pattern) {
        return 311001 + ToolTrimsDPCompat.legacyPatternOrder.indexOf(pattern) * ToolTrimsDPCompat.legacyMaterialOrder.size() + ToolTrimsDPCompat.legacyMaterialOrder.indexOf(material);
    }

    public static ArmorTrim getTrim(DynamicRegistryManager dynamicRegistryManager, int customModelData) {
        var value = customModelData - 311001;
        var pattern = legacyPatternOrder.get(value / legacyMaterialOrder.size());
        var material = legacyMaterialOrder.get(value % legacyMaterialOrder.size());
        return new ArmorTrim(
                dynamicRegistryManager.get(RegistryKeys.TRIM_MATERIAL).getEntry(material).orElseThrow(),
                dynamicRegistryManager.get(RegistryKeys.TRIM_PATTERN).getEntry(pattern).orElseThrow()
        );
    }

    public static ArmorTrim getTrim(World world, int customModelData) {
        return getTrim(world.getRegistryManager(), customModelData);
    }

    public static boolean shouldDeleteItem(ItemStack itemStack, @Nullable World world) {
        if (world != null && !world.getGameRules().getBoolean(ToolTrimsGamerules.DELETE_LEGACY_ITEMS)) return false;
        if (!itemStack.isOf(Items.STRUCTURE_BLOCK)) return false;
        var customModelData = getCustomModelData(itemStack, 0);
        return 312001 <= customModelData && customModelData <= 312021;
    }

    public static @Nullable ItemStack upgradeItem(World world, ItemStack itemStack) {
        if (itemStack.isOf(Items.STRUCTURE_BLOCK)) {
            var customModelData = getCustomModelData(itemStack, 0);
            if (312001 <= customModelData && customModelData <= 312021) return ItemStack.EMPTY;
            var item = switch (customModelData) {
                case 313001 -> ToolTrimsItems.LINEAR_TOOL_TRIM_SMITHING_TEMPLATE;
                case 313002 -> ToolTrimsItems.TRACKS_TOOL_TRIM_SMITHING_TEMPLATE;
                case 313003 -> ToolTrimsItems.CHARGE_TOOL_TRIM_SMITHING_TEMPLATE;
                case 313004 -> ToolTrimsItems.FROST_TOOL_TRIM_SMITHING_TEMPLATE;
                default -> null;
            };
            if (item == null) return null;
            return new ItemStack(item, itemStack.getCount());
        }
        if (itemStack.isIn(ToolTrimsTags.TRIMMABLE_TOOLS)) {
            var customModelData = getCustomModelData(itemStack, 0);
            if (customModelData == 0) return null;
            var itemNbt = itemStack.getOrCreateNbt();
            var display = itemNbt.getCompound("display");
            display.remove("Lore");
            if (display.getSize() == 0) itemNbt.remove("display");
            itemNbt.remove("trimmed_tool");
            itemNbt.remove("combination");
            itemNbt.remove("CustomModelData");
            ArmorTrim.apply(world.getRegistryManager(), itemStack, getTrim(world, customModelData));
            return itemStack;
        }
        return null;
    }
}
