package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.ToolTrims;
import archives.tater.tooltrims.ToolTrimsPatterns;
import archives.tater.tooltrims.mixin.client.ItemModelGeneratorAccessor;

import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.resources.Identifier;

import com.google.common.collect.Streams;

import java.util.List;
import java.util.stream.Stream;

public class TrimAssets {

    private TrimAssets() {}

    public static final List<ItemModelGenerators.TrimMaterialData> TRIM_MATERIALS = ItemModelGeneratorAccessor.TRIM_MATERIALS();

    private static final List<String> TOOL_MATERIALS = List.of("wooden", "stone", "copper", "iron", "golden", "diamond", "netherite");
    private static final List<String> MATERIAL_TOOLS = List.of("sword", "spear", "spear_in_hand", "pickaxe", "axe", "shovel", "hoe");

    // TODO: trident entity

    private static final List<Identifier> STANDARD_TOOLS = Stream.concat(
            Stream.of(
                "trident",
                "bow",
                "bow_pulling_0",
                "bow_pulling_1",
                "bow_pulling_2"
            ),
            TOOL_MATERIALS.stream().flatMap(material ->
                    MATERIAL_TOOLS.stream().map(tool ->
                            material + "_" + tool
                    )
            )
    ).map(ToolTrims::id).toList();

    private static final List<Identifier> CROSSBOWS = Stream.of(
            "standby",
            "pulling_0",
            "pulling_1",
            "pulling_2",
            "arrow",
            "firework"
    ).map(path -> ToolTrims.id("crossbow_" + path)).toList();

    public static final Identifier MACE = ToolTrims.id("mace");

    public static final List<Identifier> TEMPLATES = Streams.concat(
            STANDARD_TOOLS.stream(),
            CROSSBOWS.stream(),
            Stream.of(MACE)
    ).flatMap(tool ->
            ToolTrimsPatterns.PATTERNS.stream().map(pattern ->
                    tool.withSuffix("_" + pattern.identifier().getPath())
            )
    ).toList();
}
