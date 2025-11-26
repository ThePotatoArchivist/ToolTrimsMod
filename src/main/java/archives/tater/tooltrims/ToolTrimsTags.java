package archives.tater.tooltrims;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ToolTrimsTags {
    public static final TagKey<Item> TRIMMABLE_TOOLS = TagKey.create(Registries.ITEM, ToolTrims.id("trimmable_tools"));
    public static final TagKey<Item> TOOL_TRIM_MATERIALS = TagKey.create(Registries.ITEM, ToolTrims.id("tool_trim_materials"));

    public static void register() {}
}
