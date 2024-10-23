package archives.tater.tooltrims.item;

import archives.tater.tooltrims.ToolTrimsPatterns;
import net.minecraft.item.Item;
import net.minecraft.item.SmithingTemplateItem;
import net.minecraft.item.trim.ArmorTrimPattern;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.Map;

import static archives.tater.tooltrims.ToolTrims.MOD_ID;
import static archives.tater.tooltrims.item.ToolTrimSmithingTemplate.*;

public class ToolTrimsItems {

    private static Item register(String path, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(MOD_ID, path), item);
    }

    private static Item registerToolTemplate(String name) {
        return register(name + "_tool_trim_smithing_template", new SmithingTemplateItem(
                APPLIES_TO_TEXT,
                INGREDIENTS_TEXT,
                Text.translatable(Util.createTranslationKey("tool_trim_pattern", new Identifier(MOD_ID, name))).formatted(ToolTrimSmithingTemplate.TITLE_FORMATTING),
                BASE_SLOT_DESCRIPTION_TEXT,
                ADDITIONS_SLOT_DESCRIPTION_TEXT,
                getEmptyBaseSlotTextures(),
                getEmptyAdditionsSlotTextures()
        ));
    }

    public static Item LINEAR_TOOL_TRIM_SMITHING_TEMPLATE = ToolTrimsItems.registerToolTemplate("linear");
    public static Item TRACKS_TOOL_TRIM_SMITHING_TEMPLATE = ToolTrimsItems.registerToolTemplate("tracks");
    public static Item CHARGE_TOOL_TRIM_SMITHING_TEMPLATE = ToolTrimsItems.registerToolTemplate("charge");
    public static Item FROST_TOOL_TRIM_SMITHING_TEMPLATE = ToolTrimsItems.registerToolTemplate("frost");

    public static Map<RegistryKey<ArmorTrimPattern>, Item> SMITHING_TEMPLATES = Map.of(
            ToolTrimsPatterns.LINEAR, LINEAR_TOOL_TRIM_SMITHING_TEMPLATE,
            ToolTrimsPatterns.TRACKS, TRACKS_TOOL_TRIM_SMITHING_TEMPLATE,
            ToolTrimsPatterns.CHARGE, CHARGE_TOOL_TRIM_SMITHING_TEMPLATE,
            ToolTrimsPatterns.FROST, FROST_TOOL_TRIM_SMITHING_TEMPLATE
    );

    public static void register() {

    }
}
