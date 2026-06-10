package archives.tater.tooltrims.item;

import archives.tater.tooltrims.ToolTrims;
import archives.tater.tooltrims.mixin.SmithingTemplateItemAccessor;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SmithingTemplateItem;

import java.util.List;

public class ToolTrimSmithingTemplate {
    private ToolTrimSmithingTemplate() {} // Static utility class without instances

    public static final ChatFormatting DESCRIPTION_FORMAT = ChatFormatting.BLUE;

    public static final Component INGREDIENTS = SmithingTemplateItemAccessor.getARMOR_TRIM_INGREDIENTS();
    public static final Component APPLIES_TO = Component.translatable("template.tooltrims.applies_to")
            .withStyle(DESCRIPTION_FORMAT);
    public static final Component BASE_SLOT_DESCRIPTION = Component.translatable("template.tooltrims.base_slot_description");
    public static final Component ADDITIONS_SLOT_DESCRIPTION = SmithingTemplateItemAccessor.getARMOR_TRIM_ADDITIONS_SLOT_DESCRIPTION();

    public static final Identifier EMPTY_SLOT_SWORD = SmithingTemplateItemAccessor.getEMPTY_SLOT_SWORD();
    public static final Identifier EMPTY_SLOT_PICKAXE = SmithingTemplateItemAccessor.getEMPTY_SLOT_PICKAXE();
    public static final Identifier EMPTY_SLOT_AXE = SmithingTemplateItemAccessor.getEMPTY_SLOT_AXE();
    public static final Identifier EMPTY_SLOT_HOE = SmithingTemplateItemAccessor.getEMPTY_SLOT_HOE();
    public static final Identifier EMPTY_SLOT_SHOVEL = SmithingTemplateItemAccessor.getEMPTY_SLOT_SHOVEL();
    public static final Identifier EMPTY_SLOT_SPEAR = SmithingTemplateItemAccessor.getEMPTY_SLOT_SPEAR();
    public static final Identifier EMPTY_SLOT_BOW = ToolTrims.id("container/slot/bow");
    public static final Identifier EMPTY_SLOT_CROSSBOW = ToolTrims.id("container/slot/crossbow");
    public static final Identifier EMPTY_SLOT_TRIDENT = ToolTrims.id("container/slot/trident");
    public static final Identifier EMPTY_SLOT_MACE = ToolTrims.id("container/slot/mace");

    public static SmithingTemplateItem create(Item.Properties settings) {
        return new SmithingTemplateItem(
                APPLIES_TO,
                INGREDIENTS,
                BASE_SLOT_DESCRIPTION,
                ADDITIONS_SLOT_DESCRIPTION,
                createTrimmableToolIconList(),
                createTrimmableMaterialIconList(),
                settings
        );
    }

    public static List<Identifier> createTrimmableMaterialIconList() {
        return SmithingTemplateItemAccessor.invokeCreateTrimmableMaterialIconList();
    }

    public static List<Identifier> createTrimmableToolIconList() {
        return List.of(
                EMPTY_SLOT_SWORD,
                EMPTY_SLOT_PICKAXE,
                EMPTY_SLOT_AXE,
                EMPTY_SLOT_HOE,
                EMPTY_SLOT_SHOVEL,
                EMPTY_SLOT_SPEAR,
                EMPTY_SLOT_BOW,
                EMPTY_SLOT_CROSSBOW,
                EMPTY_SLOT_TRIDENT,
                EMPTY_SLOT_MACE
        );
    }

}
