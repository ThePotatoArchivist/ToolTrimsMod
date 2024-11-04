package archives.tater.tooltrims;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ToolTrimsTags {
    public static final TagKey<Item> TRIMMABLE_TOOLS = TagKey.of(RegistryKeys.ITEM, ToolTrims.id("trimmable_tools"));

    public static void register() {}
}
