package archives.tater.tooltrims.item;

import archives.tater.tooltrims.ToolTrims;
import net.minecraft.item.Item;
import net.minecraft.item.SmithingTemplateItem;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.List;

public class ToolTrimSmithingTemplate {
    private ToolTrimSmithingTemplate() {} // Static utility class without instances

    public static final Formatting TITLE_FORMATTING = Formatting.GRAY;
    public static final Formatting DESCRIPTION_FORMATTING = Formatting.BLUE;
    public static final Text INGREDIENTS_TEXT = Text.translatable(Util.createTranslationKey("item", ToolTrims.id("smithing_template.tool_trim.ingredients")))
            .formatted(DESCRIPTION_FORMATTING);
    public static final Text APPLIES_TO_TEXT = Text.translatable(Util.createTranslationKey("item", ToolTrims.id("smithing_template.tool_trim.applies_to")))
            .formatted(DESCRIPTION_FORMATTING);
    public static final Text BASE_SLOT_DESCRIPTION_TEXT = Text.translatable(
            Util.createTranslationKey("item", ToolTrims.id("smithing_template.tool_trim.base_slot_description"))
    );
    public static final Text ADDITIONS_SLOT_DESCRIPTION_TEXT = Text.translatable(
            Util.createTranslationKey("item", Identifier.ofVanilla("smithing_template.armor_trim.additions_slot_description"))
    );
    public static final Identifier EMPTY_SLOT_HOE_TEXTURE = Identifier.ofVanilla("container/slot/hoe");
    public static final Identifier EMPTY_SLOT_AXE_TEXTURE = Identifier.ofVanilla("container/slot/axe");
    public static final Identifier EMPTY_SLOT_SWORD_TEXTURE = Identifier.ofVanilla("container/slot/sword");
    public static final Identifier EMPTY_SLOT_SHOVEL_TEXTURE = Identifier.ofVanilla("container/slot/shovel");
    public static final Identifier EMPTY_SLOT_PICKAXE_TEXTURE = Identifier.ofVanilla("container/slot/pickaxe");
    public static final Identifier EMPTY_SLOT_BOW_TEXTURE = ToolTrims.id("container/slot/bow");
    public static final Identifier EMPTY_SLOT_CROSSBOW_TEXTURE = ToolTrims.id("container/slot/crossbow");
    public static final Identifier EMPTY_SLOT_TRIDENT_TEXTURE = ToolTrims.id("container/slot/trident");
    public static final Identifier EMPTY_SLOT_MACE_TEXTURE = ToolTrims.id("container/slot/mace");

    public static final Identifier EMPTY_SLOT_INGOT_TEXTURE = Identifier.ofVanilla("container/slot/ingot");
    public static final Identifier EMPTY_SLOT_REDSTONE_DUST_TEXTURE = Identifier.ofVanilla("container/slot/redstone_dust");
    public static final Identifier EMPTY_SLOT_QUARTZ_TEXTURE = Identifier.ofVanilla("container/slot/quartz");
    public static final Identifier EMPTY_SLOT_EMERALD_TEXTURE = Identifier.ofVanilla("container/slot/emerald");
    public static final Identifier EMPTY_SLOT_DIAMOND_TEXTURE = Identifier.ofVanilla("container/slot/diamond");
    public static final Identifier EMPTY_SLOT_LAPIS_LAZULI_TEXTURE = Identifier.ofVanilla("container/slot/lapis_lazuli");
    public static final Identifier EMPTY_SLOT_AMETHYST_SHARD_TEXTURE = Identifier.ofVanilla("container/slot/amethyst_shard");

    public static SmithingTemplateItem of(Item.Settings settings) {
        return new SmithingTemplateItem(APPLIES_TO_TEXT,
                INGREDIENTS_TEXT,
                BASE_SLOT_DESCRIPTION_TEXT,
                ADDITIONS_SLOT_DESCRIPTION_TEXT,
                getEmptyBaseSlotTextures(),
                getEmptyAdditionsSlotTextures(),
                settings
        );
    }

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
                EMPTY_SLOT_CROSSBOW_TEXTURE,
                EMPTY_SLOT_TRIDENT_TEXTURE,
                EMPTY_SLOT_MACE_TEXTURE
        );
    }

}
