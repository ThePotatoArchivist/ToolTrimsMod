package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.ToolTrimsPatterns;
import archives.tater.tooltrims.item.ToolTrimsItems;
import archives.tater.tooltrims.mixin.ItemModelGeneratorAccessor;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;

import static archives.tater.tooltrims.ToolTrims.MOD_ID;
import static archives.tater.tooltrims.ToolTrimsPatterns.TRIM_PATTERN_PREDICATE;

public class ModelGenerator extends FabricModelProvider {
    public ModelGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

    }

    public Identifier getSuffixedModelId(Item item, String pattern, String material) {
        var itemId = Registries.ITEM.getId(item);
        return new Identifier(MOD_ID, "item/trims/" + itemId.getNamespace() + "/" + itemId.getPath() + "_" + pattern+ "_" + material);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        for (var item : ToolTrimsItems.SMITHING_TEMPLATES.values()) {
            itemModelGenerator.register(item, Models.GENERATED);
        }

        var materials = ItemModelGeneratorAccessor.TRIM_MATERIALS();

        var patterns = Map.of(
                "linear", ToolTrimsPatterns.LINEAR,
                "tracks", ToolTrimsPatterns.TRACKS,
                "charge", ToolTrimsPatterns.CHARGE,
                "frost", ToolTrimsPatterns.FROST
        );

        var legacyMaterialOrder = List.of(
                "amethyst",
                "copper",
                "diamond",
                "emerald",
                "gold",
                "iron",
                "lapis",
                "netherite",
                "quartz",
                "redstone"
        );
        var patternOrder = List.of(
                "linear",
                "tracks",
                "charge",
                "frost"
        );
        var standardTools = List.of(
                Items.WOODEN_SWORD,
                Items.WOODEN_SHOVEL,
                Items.WOODEN_PICKAXE,
                Items.WOODEN_AXE,
                Items.WOODEN_HOE,
                Items.STONE_SWORD,
                Items.STONE_SHOVEL,
                Items.STONE_PICKAXE,
                Items.STONE_AXE,
                Items.STONE_HOE,
                Items.GOLDEN_SWORD,
                Items.GOLDEN_SHOVEL,
                Items.GOLDEN_PICKAXE,
                Items.GOLDEN_AXE,
                Items.GOLDEN_HOE,
                Items.IRON_SWORD,
                Items.IRON_SHOVEL,
                Items.IRON_PICKAXE,
                Items.IRON_AXE,
                Items.IRON_HOE,
                Items.DIAMOND_SWORD,
                Items.DIAMOND_SHOVEL,
                Items.DIAMOND_PICKAXE,
                Items.DIAMOND_AXE,
                Items.DIAMOND_HOE,
                Items.NETHERITE_SWORD,
                Items.NETHERITE_SHOVEL,
                Items.NETHERITE_PICKAXE,
                Items.NETHERITE_AXE,
                Items.NETHERITE_HOE
        );
        for (var tool : standardTools) {
            var overrides = new JsonArray();
            for (var patternName : patternOrder) {
                var pattern = patterns.get(patternName);
                for (var material : materials) {
                    var materialName = material.name();
                    var trimmedId = getSuffixedModelId(tool, patternName, materialName);
                    Models.HANDHELD.upload(trimmedId, TextureMap.layer0(trimmedId), itemModelGenerator.writer);

                    var override = new JsonObject();
                    var predicate = new JsonObject();
                    predicate.addProperty("trim_type", material.itemModelIndex());
                    predicate.addProperty(TRIM_PATTERN_PREDICATE.toString(), ToolTrimsPatterns.getModelIndex(pattern));
                    override.add("predicate", predicate);
                    override.addProperty("model", trimmedId.toString());
                    overrides.add(override);
                }
            }

            for (var patternName : patternOrder) {
                for (var materialName : legacyMaterialOrder) {
                    var trimmedId = getSuffixedModelId(tool, patternName, materialName);

                    var legacyOverride = new JsonObject();
                    var legacyPredicate = new JsonObject();
                    legacyPredicate.addProperty("custom_model_data", 311001 + patternOrder.indexOf(patternName) * legacyMaterialOrder.size() + legacyMaterialOrder.indexOf(materialName));
                    legacyOverride.add("predicate", legacyPredicate);
                    legacyOverride.addProperty("model", trimmedId.toString());
                    overrides.add(legacyOverride);
                }
            }

            Models.HANDHELD.upload(ModelIds.getItemModelId(tool), TextureMap.layer0(tool), itemModelGenerator.writer, (id, textures) -> {
                var json = Models.HANDHELD.createJson(id, textures);
                json.add("overrides", overrides);
                return json;
            });
        }
    }
}
