package archives.tater.tooltrims;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ToolTrimsTags {
    public static final TagKey<Item> TRIMMABLE_TOOLS = TagKey.of(RegistryKeys.ITEM, ToolTrims.id("trimmable_tools"));
    public static final TagKey<Item> TOOL_TRIM_MATERIALS = TagKey.of(RegistryKeys.ITEM, ToolTrims.id("tool_trim_materials"));

    public static void register() {}
}
