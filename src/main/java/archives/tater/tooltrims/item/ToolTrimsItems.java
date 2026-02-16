package archives.tater.tooltrims.item;

import archives.tater.tooltrims.ToolTrims;
import archives.tater.tooltrims.ToolTrimsPatterns;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.equipment.trim.TrimPattern;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.function.Function;

public class ToolTrimsItems {

    public static Item register(ResourceKey<Item> key, Function<Item.Properties, Item> factory, Item.Properties properties) {
        return Registry.register(BuiltInRegistries.ITEM, key, factory.apply(properties.setId(key)));
    }

    private static Item register(String path, Function<Item.Properties, Item> factory, Item.Properties properties) {
        return register(ResourceKey.create(Registries.ITEM, ToolTrims.id(path)), factory, properties);
    }

    private static Item registerToolTemplate(String name, Rarity rarity) {
        return register(name + "_tool_trim_smithing_template", ToolTrimSmithingTemplate::of, new Item.Properties().rarity(rarity));
    }

    public static final Item LINEAR_TOOL_TRIM_SMITHING_TEMPLATE = registerToolTemplate("linear", Rarity.UNCOMMON);
    public static final Item TRACKS_TOOL_TRIM_SMITHING_TEMPLATE = registerToolTemplate("tracks", Rarity.UNCOMMON);
    public static final Item CHARGE_TOOL_TRIM_SMITHING_TEMPLATE = registerToolTemplate("charge", Rarity.EPIC);
    public static final Item FROST_TOOL_TRIM_SMITHING_TEMPLATE = registerToolTemplate("frost", Rarity.UNCOMMON);

    public static final Map<ResourceKey<TrimPattern>, Item> SMITHING_TEMPLATES = Map.of(
            ToolTrimsPatterns.LINEAR, LINEAR_TOOL_TRIM_SMITHING_TEMPLATE,
            ToolTrimsPatterns.TRACKS, TRACKS_TOOL_TRIM_SMITHING_TEMPLATE,
            ToolTrimsPatterns.CHARGE, CHARGE_TOOL_TRIM_SMITHING_TEMPLATE,
            ToolTrimsPatterns.FROST, FROST_TOOL_TRIM_SMITHING_TEMPLATE
    );

    public static void register() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(output ->
            output.insertAfter(Items.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE,
                    LINEAR_TOOL_TRIM_SMITHING_TEMPLATE,
                    TRACKS_TOOL_TRIM_SMITHING_TEMPLATE,
                    CHARGE_TOOL_TRIM_SMITHING_TEMPLATE,
                    FROST_TOOL_TRIM_SMITHING_TEMPLATE)
        );

        if (System.getProperty("fabric-api.datagen") != null) {
            try {
                var enchancementClass = Class.forName("archives.tater.tooltrims.datagen.EnchancementModelGenerator");
                enchancementClass.getMethod("register").invoke(null);
            } catch (NoSuchMethodException | ClassNotFoundException | IllegalAccessException |
                     InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
