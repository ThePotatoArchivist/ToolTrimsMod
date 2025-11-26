package archives.tater.tooltrims.item;

import archives.tater.tooltrims.ToolTrims;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SmithingTemplateItem;

import java.util.List;

public class ToolTrimSmithingTemplate {
    private ToolTrimSmithingTemplate() {} // Static utility class without instances

    public static final ChatFormatting TITLE_FORMATTING = ChatFormatting.GRAY;
    public static final ChatFormatting DESCRIPTION_FORMATTING = ChatFormatting.BLUE;
    public static final Component INGREDIENTS_TEXT = Component.translatable(Util.makeDescriptionId("item", ToolTrims.id("smithing_template.tool_trim.ingredients")))
            .withStyle(DESCRIPTION_FORMATTING);
    public static final Component APPLIES_TO_TEXT = Component.translatable(Util.makeDescriptionId("item", ToolTrims.id("smithing_template.tool_trim.applies_to")))
            .withStyle(DESCRIPTION_FORMATTING);
    public static final Component BASE_SLOT_DESCRIPTION_TEXT = Component.translatable(
            Util.makeDescriptionId("item", ToolTrims.id("smithing_template.tool_trim.base_slot_description"))
    );
    public static final Component ADDITIONS_SLOT_DESCRIPTION_TEXT = Component.translatable(
            Util.makeDescriptionId("item", Identifier.withDefaultNamespace("smithing_template.armor_trim.additions_slot_description"))
    );
    public static final Identifier EMPTY_SLOT_HOE_TEXTURE = Identifier.withDefaultNamespace("container/slot/hoe");
    public static final Identifier EMPTY_SLOT_AXE_TEXTURE = Identifier.withDefaultNamespace("container/slot/axe");
    public static final Identifier EMPTY_SLOT_SWORD_TEXTURE = Identifier.withDefaultNamespace("container/slot/sword");
    public static final Identifier EMPTY_SLOT_SHOVEL_TEXTURE = Identifier.withDefaultNamespace("container/slot/shovel");
    public static final Identifier EMPTY_SLOT_PICKAXE_TEXTURE = Identifier.withDefaultNamespace("container/slot/pickaxe");
    public static final Identifier EMPTY_SLOT_BOW_TEXTURE = ToolTrims.id("container/slot/bow");
    public static final Identifier EMPTY_SLOT_CROSSBOW_TEXTURE = ToolTrims.id("container/slot/crossbow");
    public static final Identifier EMPTY_SLOT_TRIDENT_TEXTURE = ToolTrims.id("container/slot/trident");
    public static final Identifier EMPTY_SLOT_MACE_TEXTURE = ToolTrims.id("container/slot/mace");

    public static final Identifier EMPTY_SLOT_INGOT_TEXTURE = Identifier.withDefaultNamespace("container/slot/ingot");
    public static final Identifier EMPTY_SLOT_REDSTONE_DUST_TEXTURE = Identifier.withDefaultNamespace("container/slot/redstone_dust");
    public static final Identifier EMPTY_SLOT_QUARTZ_TEXTURE = Identifier.withDefaultNamespace("container/slot/quartz");
    public static final Identifier EMPTY_SLOT_EMERALD_TEXTURE = Identifier.withDefaultNamespace("container/slot/emerald");
    public static final Identifier EMPTY_SLOT_DIAMOND_TEXTURE = Identifier.withDefaultNamespace("container/slot/diamond");
    public static final Identifier EMPTY_SLOT_LAPIS_LAZULI_TEXTURE = Identifier.withDefaultNamespace("container/slot/lapis_lazuli");
    public static final Identifier EMPTY_SLOT_AMETHYST_SHARD_TEXTURE = Identifier.withDefaultNamespace("container/slot/amethyst_shard");

    public static SmithingTemplateItem of(Item.Properties settings) {
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
