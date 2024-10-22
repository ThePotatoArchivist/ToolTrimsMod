package archives.tater.tooltrims.item;

import archives.tater.tooltrims.ToolTrimsPatterns;
import net.minecraft.item.Item;
import net.minecraft.item.SmithingTemplateItem;
import net.minecraft.item.trim.ArmorTrimPattern;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.List;
import java.util.Map;

import static archives.tater.tooltrims.ToolTrims.MOD_ID;

public class ToolTrimsItems {

    private static final Formatting TITLE_FORMATTING = Formatting.GRAY;
    private static final Formatting DESCRIPTION_FORMATTING = Formatting.BLUE;
    private static final Text APPLIES_TO_TEXT = Text.translatable(Util.createTranslationKey("item", new Identifier(MOD_ID, "smithing_template.tool_trim.applies_to")))
            .formatted(DESCRIPTION_FORMATTING);
    private static final Text INGREDIENTS_TEXT = Text.translatable(Util.createTranslationKey("item", new Identifier(MOD_ID, "smithing_template.tool_trim.ingredients")))
            .formatted(DESCRIPTION_FORMATTING);
    private static final Text BASE_SLOT_DESCRIPTION_TEXT = Text.translatable(
            Util.createTranslationKey("item", new Identifier(MOD_ID, "smithing_template.tool_trim.base_slot_description"))
    );
    private static final Text ADDITIONS_SLOT_DESCRIPTION_TEXT = Text.translatable(
            Util.createTranslationKey("item", new Identifier(MOD_ID, "smithing_template.tool_trim.additions_slot_description"))
    );

    private static final Identifier EMPTY_SLOT_HOE_TEXTURE = new Identifier("item/empty_slot_hoe");
    private static final Identifier EMPTY_SLOT_AXE_TEXTURE = new Identifier("item/empty_slot_axe");
    private static final Identifier EMPTY_SLOT_SWORD_TEXTURE = new Identifier("item/empty_slot_sword");
    private static final Identifier EMPTY_SLOT_SHOVEL_TEXTURE = new Identifier("item/empty_slot_shovel");
    private static final Identifier EMPTY_SLOT_PICKAXE_TEXTURE = new Identifier("item/empty_slot_pickaxe");
    private static final Identifier EMPTY_SLOT_BOW_TEXTURE = new Identifier(MOD_ID, "item/gui/empty_slot_bow");
    private static final Identifier EMPTY_SLOT_CROSSBOW_TEXTURE = new Identifier(MOD_ID, "item/gui/empty_slot_crossbow");

    private static final Identifier EMPTY_SLOT_INGOT_TEXTURE = new Identifier("item/empty_slot_ingot");
    private static final Identifier EMPTY_SLOT_REDSTONE_DUST_TEXTURE = new Identifier("item/empty_slot_redstone_dust");
    private static final Identifier EMPTY_SLOT_QUARTZ_TEXTURE = new Identifier("item/empty_slot_quartz");
    private static final Identifier EMPTY_SLOT_EMERALD_TEXTURE = new Identifier("item/empty_slot_emerald");
    private static final Identifier EMPTY_SLOT_DIAMOND_TEXTURE = new Identifier("item/empty_slot_diamond");
    private static final Identifier EMPTY_SLOT_LAPIS_LAZULI_TEXTURE = new Identifier("item/empty_slot_lapis_lazuli");
    private static final Identifier EMPTY_SLOT_AMETHYST_SHARD_TEXTURE = new Identifier("item/empty_slot_amethyst_shard");

    private static List<Identifier> getEmptyAdditionsSlotTextures() {
        return List.of(
                EMPTY_SLOT_INGOT_TEXTURE,
                EMPTY_SLOT_REDSTONE_DUST_TEXTURE,
                EMPTY_SLOT_LAPIS_LAZULI_TEXTURE,
                EMPTY_SLOT_QUARTZ_TEXTURE,
                EMPTY_SLOT_DIAMOND_TEXTURE,
                EMPTY_SLOT_EMERALD_TEXTURE,
                EMPTY_SLOT_AMETHYST_SHARD_TEXTURE
        );
    }

    private static List<Identifier> getEmptyBaseSlotTextures() {
        return List.of(
                EMPTY_SLOT_SWORD_TEXTURE,
                EMPTY_SLOT_PICKAXE_TEXTURE,
                EMPTY_SLOT_AXE_TEXTURE,
                EMPTY_SLOT_HOE_TEXTURE,
                EMPTY_SLOT_SHOVEL_TEXTURE,
                EMPTY_SLOT_BOW_TEXTURE,
                EMPTY_SLOT_CROSSBOW_TEXTURE
        );
    }

    private static Item register(String path, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(MOD_ID, path), item);
    }

    private static Item registerToolTemplate(String name) {
        return register(name + "_tool_trim_smithing_template", new SmithingTemplateItem(
                APPLIES_TO_TEXT,
                INGREDIENTS_TEXT,
                Text.translatable(Util.createTranslationKey("tool_trim_pattern", new Identifier(MOD_ID, name))).formatted(TITLE_FORMATTING),
                BASE_SLOT_DESCRIPTION_TEXT,
                ADDITIONS_SLOT_DESCRIPTION_TEXT,
                getEmptyBaseSlotTextures(),
                getEmptyAdditionsSlotTextures()
        ));
    }

    public static Item LINEAR_TOOL_TRIM_SMITHING_TEMPLATE = registerToolTemplate("linear");
    public static Item TRACKS_TOOL_TRIM_SMITHING_TEMPLATE = registerToolTemplate("tracks");
    public static Item CHARGE_TOOL_TRIM_SMITHING_TEMPLATE = registerToolTemplate("charge");
    public static Item FROST_TOOL_TRIM_SMITHING_TEMPLATE = registerToolTemplate("frost");

    public static Map<RegistryKey<ArmorTrimPattern>, Item> SMITHING_TEMPLATES = Map.of(
            ToolTrimsPatterns.LINEAR, LINEAR_TOOL_TRIM_SMITHING_TEMPLATE,
            ToolTrimsPatterns.TRACKS, TRACKS_TOOL_TRIM_SMITHING_TEMPLATE,
            ToolTrimsPatterns.CHARGE, CHARGE_TOOL_TRIM_SMITHING_TEMPLATE,
            ToolTrimsPatterns.FROST, FROST_TOOL_TRIM_SMITHING_TEMPLATE
    );

    public static void register() {

    }
}
