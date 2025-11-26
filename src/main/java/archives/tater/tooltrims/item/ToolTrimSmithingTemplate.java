package archives.tater.tooltrims.item;

import archives.tater.tooltrims.ToolTrims;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SmithingTemplateItem;

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
            Util.makeDescriptionId("item", ResourceLocation.withDefaultNamespace("smithing_template.armor_trim.additions_slot_description"))
    );
    public static final ResourceLocation EMPTY_SLOT_HOE_TEXTURE = ResourceLocation.withDefaultNamespace("container/slot/hoe");
    public static final ResourceLocation EMPTY_SLOT_AXE_TEXTURE = ResourceLocation.withDefaultNamespace("container/slot/axe");
    public static final ResourceLocation EMPTY_SLOT_SWORD_TEXTURE = ResourceLocation.withDefaultNamespace("container/slot/sword");
    public static final ResourceLocation EMPTY_SLOT_SHOVEL_TEXTURE = ResourceLocation.withDefaultNamespace("container/slot/shovel");
    public static final ResourceLocation EMPTY_SLOT_PICKAXE_TEXTURE = ResourceLocation.withDefaultNamespace("container/slot/pickaxe");
    public static final ResourceLocation EMPTY_SLOT_BOW_TEXTURE = ToolTrims.id("container/slot/bow");
    public static final ResourceLocation EMPTY_SLOT_CROSSBOW_TEXTURE = ToolTrims.id("container/slot/crossbow");
    public static final ResourceLocation EMPTY_SLOT_TRIDENT_TEXTURE = ToolTrims.id("container/slot/trident");
    public static final ResourceLocation EMPTY_SLOT_MACE_TEXTURE = ToolTrims.id("container/slot/mace");

    public static final ResourceLocation EMPTY_SLOT_INGOT_TEXTURE = ResourceLocation.withDefaultNamespace("container/slot/ingot");
    public static final ResourceLocation EMPTY_SLOT_REDSTONE_DUST_TEXTURE = ResourceLocation.withDefaultNamespace("container/slot/redstone_dust");
    public static final ResourceLocation EMPTY_SLOT_QUARTZ_TEXTURE = ResourceLocation.withDefaultNamespace("container/slot/quartz");
    public static final ResourceLocation EMPTY_SLOT_EMERALD_TEXTURE = ResourceLocation.withDefaultNamespace("container/slot/emerald");
    public static final ResourceLocation EMPTY_SLOT_DIAMOND_TEXTURE = ResourceLocation.withDefaultNamespace("container/slot/diamond");
    public static final ResourceLocation EMPTY_SLOT_LAPIS_LAZULI_TEXTURE = ResourceLocation.withDefaultNamespace("container/slot/lapis_lazuli");
    public static final ResourceLocation EMPTY_SLOT_AMETHYST_SHARD_TEXTURE = ResourceLocation.withDefaultNamespace("container/slot/amethyst_shard");

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

    public static List<ResourceLocation> getEmptyAdditionsSlotTextures() {
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

    public static List<ResourceLocation> getEmptyBaseSlotTextures() {
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
