package archives.tater.tooltrims.item;

import archives.tater.tooltrims.ToolTrims;
import archives.tater.tooltrims.ToolTrimsPatterns;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.item.equipment.trim.ArmorTrimPattern;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import java.util.Map;
import java.util.function.Function;

public class ToolTrimsItems {

    private static Item register(String path, Function<Item.Settings, Item> factory, Item.Settings settings) {
        return Items.register(RegistryKey.of(RegistryKeys.ITEM, ToolTrims.id(path)), factory, settings);
    }

    private static Item registerToolTemplate(String name) {
        return register(name + "_tool_trim_smithing_template", ToolTrimSmithingTemplate::of, new Item.Settings());
    }

    public static final Item LINEAR_TOOL_TRIM_SMITHING_TEMPLATE = registerToolTemplate("linear");
    public static final Item TRACKS_TOOL_TRIM_SMITHING_TEMPLATE = registerToolTemplate("tracks");
    public static final Item CHARGE_TOOL_TRIM_SMITHING_TEMPLATE = registerToolTemplate("charge");
    public static final Item FROST_TOOL_TRIM_SMITHING_TEMPLATE = registerToolTemplate("frost");

    public static final Map<RegistryKey<ArmorTrimPattern>, Item> SMITHING_TEMPLATES = Map.of(
            ToolTrimsPatterns.LINEAR, LINEAR_TOOL_TRIM_SMITHING_TEMPLATE,
            ToolTrimsPatterns.TRACKS, TRACKS_TOOL_TRIM_SMITHING_TEMPLATE,
            ToolTrimsPatterns.CHARGE, CHARGE_TOOL_TRIM_SMITHING_TEMPLATE,
            ToolTrimsPatterns.FROST, FROST_TOOL_TRIM_SMITHING_TEMPLATE
    );

    public static void register() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries ->
            entries.addAfter(Items.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE,
                    LINEAR_TOOL_TRIM_SMITHING_TEMPLATE,
                    TRACKS_TOOL_TRIM_SMITHING_TEMPLATE,
                    CHARGE_TOOL_TRIM_SMITHING_TEMPLATE,
                    FROST_TOOL_TRIM_SMITHING_TEMPLATE)
        );
    }
}
