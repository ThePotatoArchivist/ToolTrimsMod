package archives.tater.tooltrims;

import archives.tater.tooltrims.item.ToolTrimsItems;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import org.jspecify.annotations.Nullable;

public class ToolTrimsDPCompat {
    public static void register() {
        ResourceLoader.registerBuiltinPack(
                ToolTrims.id("legacy"),
                FabricLoader.getInstance().getModContainer(ToolTrims.MOD_ID).orElseThrow(),
                Component.literal("Tool Trims Legacy"),
                PackActivationType.NORMAL
        );

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            if (isDatapackRunning(server))
                ToolTrims.LOGGER.warn(Component.translatable("tooltrims.warning.datapack_running").getString());
        });

        ServerPlayConnectionEvents.JOIN.register((handler, _, server) -> {
            if (!server.isPublished() && isDatapackRunning(server))
                handler.player.sendSystemMessage(Component.translatable("tooltrims.warning.datapack_running").withStyle(ChatFormatting.GOLD));
        });
    }

    public static boolean isDatapackRunning(MinecraftServer server) {
        return server.getFunctions()
                .getTag(Identifier.withDefaultNamespace("load"))
                .stream().anyMatch(function -> function.id().equals(Identifier.fromNamespaceAndPath("tooltrims", "load_wait")));
    }

    private static @Nullable Item getMigratedItem(ItemStack dpTemplate) {
        if (dpTemplate.is(Items.TADPOLE_SPAWN_EGG)) return ToolTrimsItems.LINEAR_TOOL_TRIM_SMITHING_TEMPLATE;
        if (dpTemplate.is(Items.SILVERFISH_SPAWN_EGG)) return ToolTrimsItems.TRACKS_TOOL_TRIM_SMITHING_TEMPLATE;
        if (dpTemplate.is(Items.COD_SPAWN_EGG)) return ToolTrimsItems.CHARGE_TOOL_TRIM_SMITHING_TEMPLATE;
        if (dpTemplate.is(Items.SNOW_GOLEM_SPAWN_EGG)) return ToolTrimsItems.FROST_TOOL_TRIM_SMITHING_TEMPLATE;
        return null;
    }

    public static @Nullable ItemStack migrateItem(ItemStack stack) {
        if (!stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getStringOr("tooltrims:item", "").equals("template")) return null;

        var item = getMigratedItem(stack);
        if (item == null) return null;

        var newStack = item.getDefaultInstance();
        newStack.setCount(stack.getCount());

        return newStack;
    }

    private static @Nullable Identifier getTemplateLootTable(ItemStack template) {
        if (template.is(ToolTrimsItems.LINEAR_TOOL_TRIM_SMITHING_TEMPLATE)) return ToolTrims.id("linear_template");
        if (template.is(ToolTrimsItems.TRACKS_TOOL_TRIM_SMITHING_TEMPLATE)) return ToolTrims.id("tracks_template");
        if (template.is(ToolTrimsItems.CHARGE_TOOL_TRIM_SMITHING_TEMPLATE)) return ToolTrims.id("charge_template");
        if (template.is(ToolTrimsItems.FROST_TOOL_TRIM_SMITHING_TEMPLATE)) return ToolTrims.id("frost_template");
        return null;
    }

    public static @Nullable ItemStack demigrateItem(ServerLevel level, ItemStack stack) {
        if (!ToolTrimsItems.SMITHING_TEMPLATES.containsValue(stack.getItem())) return null;
        var lootTable = getTemplateLootTable(stack);
        if (lootTable == null) return null;
        var stacks = level.getServer().reloadableRegistries().getLootTable(ResourceKey.create(Registries.LOOT_TABLE, lootTable))
                .getRandomItems(new LootParams.Builder(level).create(LootContextParamSets.EMPTY));
        if (stacks.isEmpty()) return null;
        var newStack = stacks.getFirst();
        newStack.setCount(stack.getCount());
        newStack.applyComponentsAndValidate(stack.getComponentsPatch());
        return newStack;
    }
}
