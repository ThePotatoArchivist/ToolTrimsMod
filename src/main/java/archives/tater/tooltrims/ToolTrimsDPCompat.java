package archives.tater.tooltrims;

import archives.tater.tooltrims.item.ToolTrimsItems;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderGetter.Provider;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimMaterials;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("UnstableApiUsage")
public class ToolTrimsDPCompat {
    public static final List<ResourceKey<TrimMaterial>> legacyMaterialOrder = List.of(
            TrimMaterials.AMETHYST,
            TrimMaterials.COPPER,
            TrimMaterials.DIAMOND,
            TrimMaterials.EMERALD,
            TrimMaterials.GOLD,
            TrimMaterials.IRON,
            TrimMaterials.LAPIS,
            TrimMaterials.NETHERITE,
            TrimMaterials.QUARTZ,
            TrimMaterials.REDSTONE
    );
    // Resin patterns is after all of the normal order of patterns

    public static final List<ResourceKey<TrimPattern>> legacyPatternOrder = List.of(
            ToolTrimsPatterns.LINEAR,
            ToolTrimsPatterns.TRACKS,
            ToolTrimsPatterns.CHARGE,
            ToolTrimsPatterns.FROST
    );

    private static ResourceKey<LootTable> templateLootTable(String trim) {
        return ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath("tooltrims", "items/" + trim + "_smithing_template"));
    }

    public static final AttachmentType<Unit> CHECKED_FOR_DP = AttachmentRegistry.createPersistent(ToolTrims.id("checked_for_dp"), Unit.CODEC);

    private static final String disableGamerule = "/gamerule " + requireNonNull(BuiltInRegistries.GAME_RULE.getKey(ToolTrimsGamerules.DELETE_TOOLSMITHING_TABLES)) + " false";

    private static boolean gameruleWasEnabled = false;

    public static void register() {
        //noinspection OptionalGetWithoutIsPresent
        ResourceLoader.registerBuiltinPack(
                ToolTrims.id("legacy"),
                FabricLoader.getInstance().getModContainer(ToolTrims.MOD_ID).get(),
                Component.literal("Tool Trims Legacy"),
                PackActivationType.NORMAL
        );

        gameruleWasEnabled = false;

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            if (!server.overworld().hasAttached(CHECKED_FOR_DP)) {
                if (wasDatapackUsed(server)) {
                    server.getGameRules().set(ToolTrimsGamerules.DELETE_TOOLSMITHING_TABLES, true, server);
                    gameruleWasEnabled = true;
                    ToolTrims.LOGGER.warn(Component.translatable("tooltrims.warning.auto_enable", disableGamerule).getString());
                }
                server.overworld().setAttached(CHECKED_FOR_DP, Unit.INSTANCE);
            }
            if (isDatapackRunning(server)) {
                ToolTrims.LOGGER.warn(Component.translatable("tooltrims.warning.datapack_running").getString());
            }
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            if (!server.isPublished()) {
                if (gameruleWasEnabled) {
                    handler.player.sendSystemMessage(Component.translatable("tooltrims.warning.auto_enable", disableGamerule));
                }
                if (isDatapackRunning(server)) {
                    handler.player.sendSystemMessage(Component.translatable("tooltrims.warning.datapack_running").withStyle(ChatFormatting.GOLD));
                }
            }
        });
    }

    public static boolean wasDatapackUsed(MinecraftServer server) {
        return server.getScoreboard().getObjectiveNames().contains("310_recipe");
    }

    public static boolean isDatapackRunning(MinecraftServer server) {
        return server.getFunctions()
                .getTag(Identifier.withDefaultNamespace("load"))
                .stream().anyMatch(function -> function.id().equals(Identifier.fromNamespaceAndPath("tooltrims", "load")));
    }

    public static boolean shouldDeleteToolsmithingTable(ServerLevel world, ArmorStand armorStand) {
        return !armorStand.level().isClientSide() && world.getGameRules().get(ToolTrimsGamerules.DELETE_TOOLSMITHING_TABLES) &&
                (armorStand.entityTags().contains("310_toolsmithing_table") || armorStand.entityTags().contains("310_place_toolsmithing_table")) &&
                        armorStand.level().getNearestPlayer(armorStand, 6) != null;
    }

    public static void deleteToolsmithingTable(ArmorStand armorStand) {
        if (armorStand.entityTags().contains("310_toolsmithing_table") && armorStand.level().getBlockState(armorStand.blockPosition()).is(Blocks.BARREL)) {
            armorStand.level().destroyBlock(armorStand.blockPosition(), false);
        }
        armorStand.spawnAtLocation((ServerLevel) armorStand.level(), new ItemStack(Items.OAK_PLANKS, 4));
        armorStand.spawnAtLocation((ServerLevel) armorStand.level(), new ItemStack(Items.COPPER_INGOT, 2));
        armorStand.discard();
    }

    public static int getCustomModelData(ItemStack itemStack, int defaultValue) {
        var customModelData = itemStack.get(DataComponents.CUSTOM_MODEL_DATA);
        if (customModelData == null) return defaultValue;
        var floatValue = customModelData.getFloat(0);
        if (floatValue == null) return defaultValue;
        return floatValue.intValue();
    }

    public static int getCustomModelData(ResourceKey<TrimMaterial> material, ResourceKey<TrimPattern> pattern) {
        if (material.equals(TrimMaterials.RESIN)) // For legacy reasons
            return 311041 + ToolTrimsDPCompat.legacyPatternOrder.indexOf(pattern);
        return 311001 + ToolTrimsDPCompat.legacyPatternOrder.indexOf(pattern) * ToolTrimsDPCompat.legacyMaterialOrder.size() + ToolTrimsDPCompat.legacyMaterialOrder.indexOf(material);
    }

    public static ArmorTrim getTrim(Provider registryLookup, int customModelData) {
        ResourceKey<TrimPattern> pattern;
        ResourceKey<TrimMaterial> material;
        if (customModelData >= 311041) { // Resin case
            var value = customModelData - 311041;
            pattern = legacyPatternOrder.get(value);
            material = TrimMaterials.RESIN;
        } else {
            var value = customModelData - 311001;
            pattern = legacyPatternOrder.get(value / legacyMaterialOrder.size());
            material = legacyMaterialOrder.get(value % legacyMaterialOrder.size());
        }
        return new ArmorTrim(
                registryLookup.lookupOrThrow(Registries.TRIM_MATERIAL).getOrThrow(material),
                registryLookup.lookupOrThrow(Registries.TRIM_PATTERN).getOrThrow(pattern)
        );
    }

    public static ArmorTrim getTrim(Level world, int customModelData) {
        return getTrim(world.registryAccess(), customModelData);
    }

    public static boolean shouldDeleteItem(ItemStack itemStack, @Nullable ServerLevel world) {
        if (world != null && !world.getGameRules().get(ToolTrimsGamerules.DELETE_TOOLSMITHING_TABLES)) return false;
        if (!itemStack.is(Items.STRUCTURE_BLOCK)) return false;
        var customModelData = getCustomModelData(itemStack, 0);
        return 312001 <= customModelData && customModelData <= 312021;
    }

    public static @Nullable ItemStack migrateItem(Level world, ItemStack itemStack) {
        if (itemStack.is(Items.STRUCTURE_BLOCK)) {
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
        if (itemStack.is(ToolTrimsTags.TRIMMABLE_TOOLS)) {
            var customModelData = getCustomModelData(itemStack, 0);
            if (customModelData == 0) return null;
            itemStack.set(DataComponents.LORE, ItemLore.EMPTY);
            itemStack.remove(DataComponents.CUSTOM_MODEL_DATA);
            var customDataComponent = itemStack.get(DataComponents.CUSTOM_DATA);
            if (customDataComponent != null) {
                var customData = customDataComponent.copyTag();
                customData.remove("combination");
                customData.remove("trimmed_tool");
                if (customData.isEmpty())
                    itemStack.remove(DataComponents.CUSTOM_DATA);
                else
                    itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(customData));
            }
            if (itemStack.get(DataComponents.TRIM) == null) {
                itemStack.set(DataComponents.TRIM, getTrim(world, customModelData));
            }
            return itemStack;
        }
        return null;
    }

    public static @Nullable ItemStack demigrateItem(ServerLevel world, net.minecraft.core.HolderLookup.Provider registries, ItemStack stack) {
        if (stack.is(ToolTrimsTags.TRIMMABLE_TOOLS)) {
            var trim = stack.get(DataComponents.TRIM);
            if (trim == null) return null;
            var id = Identifier.fromNamespaceAndPath("tooltrims", "trims/" + trim.pattern().unwrapKey().orElseThrow().identifier().getPath() + "_" + trim.material().unwrapKey().orElseThrow().identifier().getPath());
            var modifier = registries.get(ResourceKey.create(Registries.ITEM_MODIFIER, id));
            if (modifier.isEmpty()) return null;
            modifier.get().value().apply(stack, new LootContext.Builder(new LootParams.Builder(world).create(LootContextParamSets.EMPTY)).create(Optional.empty()));
            stack.remove(DataComponents.TRIM);
            return stack;
        }
        if (ToolTrimsItems.SMITHING_TEMPLATES.containsValue(stack.getItem())) {
            var stacks = world.getServer().reloadableRegistries().getLootTable(ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath("tooltrims", "items/" + stack.typeHolder().unwrapKey().orElseThrow().identifier().getPath())))
                    .getRandomItems(new LootParams.Builder(world).create(LootContextParamSets.EMPTY));
            if (stacks.isEmpty()) return null;
            var newStack = stacks.getFirst();
            newStack.setCount(stack.getCount());
            newStack.applyComponentsAndValidate(stack.getComponentsPatch());
            return newStack;
        }
        return null;
    }
}
