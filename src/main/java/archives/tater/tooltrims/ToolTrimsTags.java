package archives.tater.tooltrims;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ToolTrimsTags {
    public static TagKey<Item> TRIMMABLE_TOOLS = TagKey.of(RegistryKeys.ITEM, new Identifier(ToolTrims.MOD_ID, "trimmable_tools"));

    public static void register() {}
}
