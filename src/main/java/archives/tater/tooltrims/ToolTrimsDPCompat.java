package archives.tater.tooltrims;

import archives.tater.tooltrims.item.ToolTrimsItems;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.equipment.trim.ArmorTrim;
import net.minecraft.item.equipment.trim.ArmorTrimMaterial;
import net.minecraft.item.equipment.trim.ArmorTrimMaterials;
import net.minecraft.item.equipment.trim.ArmorTrimPattern;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.registry.RegistryEntryLookup.RegistryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;
import net.minecraft.world.World;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    // Resin patterns is after all of the normal order of patterns

    public static final List<RegistryKey<ArmorTrimPattern>> legacyPatternOrder = List.of(
            ToolTrimsPatterns.LINEAR,
            ToolTrimsPatterns.TRACKS,
            ToolTrimsPatterns.CHARGE,
            ToolTrimsPatterns.FROST
    );

    private static RegistryKey<LootTable> templateLootTable(String trim) {
        return RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("tooltrims", "items/" + trim + "_smithing_template"));
    }

    private static final String disableGamerule = "/gamerule " + ToolTrimsGamerules.DELETE_TOOLSMITHING_TABLES.getName() + " false";

    private static boolean gameruleWasEnabled = false;

    public static void register() {
        //noinspection OptionalGetWithoutIsPresent
        ResourceManagerHelper.registerBuiltinResourcePack(
                ToolTrims.id("legacy"),
                FabricLoader.getInstance().getModContainer(ToolTrims.MOD_ID).get(),
                Text.literal("Tool Trims Legacy"),
                ResourcePackActivationType.NORMAL
        );

        gameruleWasEnabled = false;

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            var state = State.ofServer(server);
            if (!state.hasCheckedForDP()) {
                if (wasDatapackUsed(server)) {
                    server.getGameRules().get(ToolTrimsGamerules.DELETE_TOOLSMITHING_TABLES).set(true, server);
                    gameruleWasEnabled = true;
                    ToolTrims.LOGGER.warn(Text.translatable("tooltrims.warning.auto_enable", disableGamerule).getString());
                }
                state.setCheckedForDP();
            }
            if (isDatapackRunning(server)) {
                ToolTrims.LOGGER.warn(Text.translatable("tooltrims.warning.datapack_running").getString());
            }
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            if (!server.isRemote()) {
                if (gameruleWasEnabled) {
                    handler.player.sendMessage(Text.translatable("tooltrims.warning.auto_enable", disableGamerule));
                }
                if (isDatapackRunning(server)) {
                    handler.player.sendMessage(Text.translatable("tooltrims.warning.datapack_running").formatted(Formatting.GOLD));
                }
            }
        });
    }

    public static boolean wasDatapackUsed(MinecraftServer server) {
        return server.getScoreboard().getObjectiveNames().contains("310_recipe");
    }

    public static boolean isDatapackRunning(MinecraftServer server) {
        return server.getCommandFunctionManager()
                .getTag(Identifier.ofVanilla("load"))
                .stream().anyMatch(function -> function.id().equals(Identifier.of("tooltrims", "load")));
    }

    public static boolean shouldDeleteToolsmithingTable(ArmorStandEntity armorStand) {
        return !armorStand.getWorld().isClient && Objects.requireNonNull(armorStand.getServer()).getGameRules().getBoolean(ToolTrimsGamerules.DELETE_TOOLSMITHING_TABLES) &&
                (armorStand.getCommandTags().contains("310_toolsmithing_table") || armorStand.getCommandTags().contains("310_place_toolsmithing_table")) &&
                        armorStand.getWorld().getClosestPlayer(armorStand, 6) != null;
    }

    public static void deleteToolsmithingTable(ArmorStandEntity armorStand) {
        if (armorStand.getCommandTags().contains("310_toolsmithing_table") && armorStand.getWorld().getBlockState(armorStand.getBlockPos()).isOf(Blocks.BARREL)) {
            armorStand.getWorld().breakBlock(armorStand.getBlockPos(), false);
        }
        armorStand.dropStack((ServerWorld) armorStand.getWorld(), new ItemStack(Items.OAK_PLANKS, 4));
        armorStand.dropStack((ServerWorld) armorStand.getWorld(), new ItemStack(Items.COPPER_INGOT, 2));
        armorStand.discard();
    }

    public static int getCustomModelData(ItemStack itemStack, int defaultValue) {
        var customModelData = itemStack.get(DataComponentTypes.CUSTOM_MODEL_DATA);
        if (customModelData == null) return defaultValue;
        var floatValue = customModelData.getFloat(0);
        if (floatValue == null) return defaultValue;
        return floatValue.intValue();
    }

    public static int getCustomModelData(RegistryKey<ArmorTrimMaterial> material, RegistryKey<ArmorTrimPattern> pattern) {
        if (material.equals(ArmorTrimMaterials.RESIN)) // For legacy reasons
            return 311041 + ToolTrimsDPCompat.legacyPatternOrder.indexOf(pattern);
        return 311001 + ToolTrimsDPCompat.legacyPatternOrder.indexOf(pattern) * ToolTrimsDPCompat.legacyMaterialOrder.size() + ToolTrimsDPCompat.legacyMaterialOrder.indexOf(material);
    }

    public static ArmorTrim getTrim(RegistryLookup registryLookup, int customModelData) {
        RegistryKey<ArmorTrimPattern> pattern;
        RegistryKey<ArmorTrimMaterial> material;
        if (customModelData >= 311041) { // Resin case
            var value = customModelData - 311041;
            pattern = legacyPatternOrder.get(value);
            material = ArmorTrimMaterials.RESIN;
        } else {
            var value = customModelData - 311001;
            pattern = legacyPatternOrder.get(value / legacyMaterialOrder.size());
            material = legacyMaterialOrder.get(value % legacyMaterialOrder.size());
        }
        return new ArmorTrim(
                registryLookup.getOrThrow(RegistryKeys.TRIM_MATERIAL).getOrThrow(material),
                registryLookup.getOrThrow(RegistryKeys.TRIM_PATTERN).getOrThrow(pattern)
        );
    }

    public static ArmorTrim getTrim(World world, int customModelData) {
        return getTrim(world.getRegistryManager(), customModelData);
    }

    public static boolean shouldDeleteItem(ItemStack itemStack, @Nullable World world) {
        if (world != null && !Objects.requireNonNull(world.getServer()).getGameRules().getBoolean(ToolTrimsGamerules.DELETE_TOOLSMITHING_TABLES)) return false;
        if (!itemStack.isOf(Items.STRUCTURE_BLOCK)) return false;
        var customModelData = getCustomModelData(itemStack, 0);
        return 312001 <= customModelData && customModelData <= 312021;
    }

    public static @Nullable ItemStack migrateItem(World world, ItemStack itemStack) {
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
            itemStack.set(DataComponentTypes.LORE, LoreComponent.DEFAULT);
            itemStack.remove(DataComponentTypes.CUSTOM_MODEL_DATA);
            var customDataComponent = itemStack.get(DataComponentTypes.CUSTOM_DATA);
            if (customDataComponent != null) {
                var customData = customDataComponent.copyNbt();
                customData.remove("combination");
                customData.remove("trimmed_tool");
                if (customData.isEmpty())
                    itemStack.remove(DataComponentTypes.CUSTOM_DATA);
                else
                    itemStack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(customData));
            }
            if (itemStack.get(DataComponentTypes.TRIM) == null) {
                itemStack.set(DataComponentTypes.TRIM, getTrim(world, customModelData));
            }
            return itemStack;
        }
        return null;
    }

    public static @Nullable ItemStack demigrateItem(ServerWorld world, WrapperLookup registries, ItemStack stack) {
        if (stack.isIn(ToolTrimsTags.TRIMMABLE_TOOLS)) {
            var trim = stack.get(DataComponentTypes.TRIM);
            if (trim == null) return null;
            var id = Identifier.of("tooltrims", "trims/" + trim.pattern().getKey().orElseThrow().getValue().getPath() + "_" + trim.material().getKey().orElseThrow().getValue().getPath());
            var modifier = registries.getOptionalEntry(RegistryKey.of(RegistryKeys.ITEM_MODIFIER, id));
            if (modifier.isEmpty()) return null;
            modifier.get().value().apply(stack, new LootContext.Builder(new LootWorldContext.Builder(world).build(LootContextTypes.EMPTY)).build(Optional.empty()));
            stack.remove(DataComponentTypes.TRIM);
            return stack;
        }
        if (ToolTrimsItems.SMITHING_TEMPLATES.containsValue(stack.getItem())) {
            var stacks = world.getServer().getReloadableRegistries().getLootTable(RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("tooltrims", "items/" + stack.getRegistryEntry().getKey().orElseThrow().getValue().getPath())))
                    .generateLoot(new LootWorldContext.Builder(world).build(LootContextTypes.EMPTY));
            if (stacks.isEmpty()) return null;
            var newStack = stacks.getFirst();
            newStack.setCount(stack.getCount());
            newStack.applyChanges(stack.getComponentChanges());
            return newStack;
        }
        return null;
    }

    public static class State extends PersistentState {
        private static final String CHECKED_NBT = "CheckedForDP";
        public static final Codec<State> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.BOOL.fieldOf("CheckedForDP").forGetter(State::hasCheckedForDP)
        ).apply(instance, State::new));

        private boolean checkedForDP;

        public State() {
            checkedForDP = false;
        }

        public State(boolean checkedForDP) {
            this.checkedForDP = checkedForDP;
        }

        public boolean hasCheckedForDP() {
            return checkedForDP;
        }

        public void setCheckedForDP() {
            this.checkedForDP = true;
            setDirty(true);
        }

        private static final PersistentStateType<State> TYPE = new PersistentStateType<>(ToolTrims.MOD_ID, State::new, CODEC, null);

        public static State ofServer(MinecraftServer server) {
            return Objects.requireNonNull(server.getWorld(World.OVERWORLD))
                    .getPersistentStateManager()
                    .getOrCreate(TYPE);
        }
    }
}
