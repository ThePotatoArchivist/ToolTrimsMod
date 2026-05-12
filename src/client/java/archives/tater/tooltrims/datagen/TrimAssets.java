package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.ToolTrims;
import archives.tater.tooltrims.ToolTrimsPatterns;
import archives.tater.tooltrims.mixin.client.ItemModelGeneratorAccessor;

import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class TrimAssets {

    private TrimAssets() {}

    public static final List<String> TRIM_MATERIALS = ItemModelGeneratorAccessor.TRIM_MATERIALS().stream()
            .map(material -> material.materialKey().identifier().getPath())
            .toList();

    private static final List<String> TOOL_MATERIALS = List.of("wooden", "stone", "copper", "iron", "golden", "diamond", "netherite");
    private static final List<String> HANDHELD_TOOLS = List.of("sword", "spear_in_hand", "pickaxe", "axe", "shovel", "hoe");

    // TODO: trident entity

    record Entry(Item baseItem, ModelTemplate model, List<Identifier> textures) {}

    public static final Map<ModelTemplate, List<Identifier>> TYPES = Map.of(
            ModelTemplates.FLAT_HANDHELD_ITEM,
            TOOL_MATERIALS.stream().flatMap(material ->
                    HANDHELD_TOOLS.stream().map(tool ->
                            material + "_" + tool
                    )
            ).map(ToolTrims::id).toList(),

            ModelTemplates.FLAT_ITEM,
            Stream.concat(
                    Stream.of("trident"),
                    TOOL_MATERIALS.stream().map(material ->
                            material + "_spear"
                    )
            ).map(ToolTrims::id).toList(),

            ModelTemplates.SPEAR_IN_HAND,
            TOOL_MATERIALS.stream().map(material ->
                    material + "_spear_in_hand"
            ).map(ToolTrims::id).toList(),

            ModelTemplates.BOW,
            Stream.of(
                    "",
                    "_pulling_0",
                    "_pulling_1",
                    "_pulling_2"
            ).map(path -> ToolTrims.id("bow" + path)).toList(),

            ModelTemplates.CROSSBOW,
            Stream.of(
                    "standby",
                    "pulling_0",
                    "pulling_1",
                    "pulling_2",
                    "arrow",
                    "firework"
            ).map(path -> ToolTrims.id("crossbow_" + path)).toList(),

            ModelTemplates.FLAT_HANDHELD_MACE_ITEM,
            List.of(ToolTrims.id("mace"))
    );

//    public static final Map<ModelTemplate, List<Identifier>> MODELS = TYPES.entrySet().stream();

    private static Stream<Identifier> getPatternedIds(Identifier tool) {
        return ToolTrimsPatterns.PATTERNS.stream().map(pattern ->
                tool.withSuffix("_" + pattern.identifier().getPath())
        );
    }
}
