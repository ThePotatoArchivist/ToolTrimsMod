package archives.tater.tooltrims.item;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.List;

import static archives.tater.tooltrims.ToolTrims.MOD_ID;

public class ToolTrimSmithingTemplate {
    private ToolTrimSmithingTemplate() {} // Static utility class without instances

    public static final Formatting TITLE_FORMATTING = Formatting.GRAY;
    public static final Formatting DESCRIPTION_FORMATTING = Formatting.BLUE;
    public static final Text INGREDIENTS_TEXT = Text.translatable(Util.createTranslationKey("item", new Identifier(MOD_ID, "smithing_template.tool_trim.ingredients")))
            .formatted(DESCRIPTION_FORMATTING);
    public static final Text APPLIES_TO_TEXT = Text.translatable(Util.createTranslationKey("item", new Identifier(MOD_ID, "smithing_template.tool_trim.applies_to")))
            .formatted(DESCRIPTION_FORMATTING);
    public static final Text BASE_SLOT_DESCRIPTION_TEXT = Text.translatable(
            Util.createTranslationKey("item", new Identifier(MOD_ID, "smithing_template.tool_trim.base_slot_description"))
    );
    public static final Text ADDITIONS_SLOT_DESCRIPTION_TEXT = Text.translatable(
            Util.createTranslationKey("item", new Identifier(MOD_ID, "smithing_template.tool_trim.additions_slot_description"))
    );
    public static final Identifier EMPTY_SLOT_HOE_TEXTURE = new Identifier("item/empty_slot_hoe");
    public static final Identifier EMPTY_SLOT_AXE_TEXTURE = new Identifier("item/empty_slot_axe");
    public static final Identifier EMPTY_SLOT_SWORD_TEXTURE = new Identifier("item/empty_slot_sword");
    public static final Identifier EMPTY_SLOT_SHOVEL_TEXTURE = new Identifier("item/empty_slot_shovel");
    public static final Identifier EMPTY_SLOT_PICKAXE_TEXTURE = new Identifier("item/empty_slot_pickaxe");
    public static final Identifier EMPTY_SLOT_BOW_TEXTURE = new Identifier(MOD_ID, "item/empty_slot_bow");
    public static final Identifier EMPTY_SLOT_CROSSBOW_TEXTURE = new Identifier(MOD_ID, "item/empty_slot_crossbow");

    public static final Identifier EMPTY_SLOT_INGOT_TEXTURE = new Identifier("item/empty_slot_ingot");
    public static final Identifier EMPTY_SLOT_REDSTONE_DUST_TEXTURE = new Identifier("item/empty_slot_redstone_dust");
    public static final Identifier EMPTY_SLOT_QUARTZ_TEXTURE = new Identifier("item/empty_slot_quartz");
    public static final Identifier EMPTY_SLOT_EMERALD_TEXTURE = new Identifier("item/empty_slot_emerald");
    public static final Identifier EMPTY_SLOT_DIAMOND_TEXTURE = new Identifier("item/empty_slot_diamond");
    public static final Identifier EMPTY_SLOT_LAPIS_LAZULI_TEXTURE = new Identifier("item/empty_slot_lapis_lazuli");
    public static final Identifier EMPTY_SLOT_AMETHYST_SHARD_TEXTURE = new Identifier("item/empty_slot_amethyst_shard");

    public static List<Identifier> getEmptyAdditionsSlotTextures() {
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

    public static List<Identifier> getEmptyBaseSlotTextures() {
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

}
