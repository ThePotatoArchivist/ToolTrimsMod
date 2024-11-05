package archives.tater.tooltrims.item;

import archives.tater.tooltrims.ToolTrims;
import archives.tater.tooltrims.ToolTrimsPatterns;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.item.SmithingTemplateItem;
import net.minecraft.item.trim.ArmorTrimPattern;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

import java.util.Map;

import static archives.tater.tooltrims.item.ToolTrimSmithingTemplate.*;

public class ToolTrimsItems {

    private static Item register(String path, Item item) {
        return Registry.register(Registries.ITEM, ToolTrims.id(path), item);
    }

    private static Item registerToolTemplate(String name) {
        return register(name + "_tool_trim_smithing_template", new SmithingTemplateItem(
                APPLIES_TO_TEXT,
                INGREDIENTS_TEXT,
                Text.translatable(Util.createTranslationKey("tool_trim_pattern", ToolTrims.id(name))).formatted(ToolTrimSmithingTemplate.TITLE_FORMATTING),
                BASE_SLOT_DESCRIPTION_TEXT,
                ADDITIONS_SLOT_DESCRIPTION_TEXT,
                getEmptyBaseSlotTextures(),
                getEmptyAdditionsSlotTextures()
        ));
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
                entries.addAfter(Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE,
                    LINEAR_TOOL_TRIM_SMITHING_TEMPLATE,
                    TRACKS_TOOL_TRIM_SMITHING_TEMPLATE,
                    CHARGE_TOOL_TRIM_SMITHING_TEMPLATE,
                    FROST_TOOL_TRIM_SMITHING_TEMPLATE)
        );
    }
}
