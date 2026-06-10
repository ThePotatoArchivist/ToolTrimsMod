package archives.tater.tooltrims.registry;

import archives.tater.tooltrims.ToolTrims;
import archives.tater.tooltrims.item.ToolTrimSmithingTemplate;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.equipment.trim.TrimPattern;

import java.util.Map;
import java.util.function.Function;

import static net.minecraft.util.Util.makeDescriptionId;

public class ToolTrimsItems {

    public static Item register(ResourceKey<Item> key, Function<Item.Properties, Item> factory, Item.Properties properties) {
        return Registry.register(BuiltInRegistries.ITEM, key, factory.apply(properties.setId(key)));
    }

    private static Item register(String path, Function<Item.Properties, Item> factory, Item.Properties properties) {
        return register(ResourceKey.create(Registries.ITEM, ToolTrims.id(path)), factory, properties);
    }

    private static Item registerToolTemplate(String name, Rarity rarity) {
        return register(name + "_template", ToolTrimSmithingTemplate::create, new Item.Properties()
                .rarity(rarity)
                .overrideDescription(makeDescriptionId("trim_pattern", ToolTrims.id(name)))
        );
    }

    public static final Item LINEAR_TEMPLATE = registerToolTemplate("linear", Rarity.UNCOMMON);
    public static final Item TRACKS_TEMPLATE = registerToolTemplate("tracks", Rarity.UNCOMMON);
    public static final Item CHARGE_TEMPLATE = registerToolTemplate("charge", Rarity.EPIC);
    public static final Item FROST_TEMPLATE = registerToolTemplate("frost", Rarity.UNCOMMON);

    public static final Map<ResourceKey<TrimPattern>, Item> SMITHING_TEMPLATES = Map.of(
            ToolTrimsPatterns.LINEAR, LINEAR_TEMPLATE,
            ToolTrimsPatterns.TRACKS, TRACKS_TEMPLATE,
            ToolTrimsPatterns.CHARGE, CHARGE_TEMPLATE,
            ToolTrimsPatterns.FROST, FROST_TEMPLATE
    );

    public static void init() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(output ->
            output.insertAfter(Items.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE,
                    LINEAR_TEMPLATE,
                    TRACKS_TEMPLATE,
                    CHARGE_TEMPLATE,
                    FROST_TEMPLATE
            )
        );

        for (var pattern : ToolTrimsPatterns.PATTERNS) {
            var name = pattern.identifier().getPath();
            BuiltInRegistries.ITEM.addAlias(ToolTrims.id(name + "_tool_trim_smithing_template"), ToolTrims.id(name + "_template"));
        }
    }
}
