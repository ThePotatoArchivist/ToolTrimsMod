package archives.tater.tooltrims;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ToolTrimsTags {
    public static final TagKey<Item> TRIMMABLE_TOOLS = TagKey.create(Registries.ITEM, ToolTrims.id("trimmable_tools"));

    public static void register() {}
}
