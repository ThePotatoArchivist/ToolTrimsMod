package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.ToolTrims;
import archives.tater.tooltrims.ToolTrimsDPCompat;
import archives.tater.tooltrims.ToolTrimsPatterns;
import archives.tater.tooltrims.item.ToolTrimsItems;
import archives.tater.tooltrims.mixin.ItemModelGeneratorAccessor;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static archives.tater.tooltrims.ToolTrimsPatterns.TRIM_PATTERN_PREDICATE;

public class ModelGenerator extends FabricModelProvider {
    public ModelGenerator(FabricDataOutput output) {
        super(output);
    }

    private static final List<ItemModelGenerator.TrimMaterial> materials = ItemModelGeneratorAccessor.TRIM_MATERIALS();

    private static final List<Item> standardTools = List.of(
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

    protected static final Model TEMPLATE_BOW = new Model(Optional.of(ToolTrims.id("item/template_bow")), Optional.empty(), TextureKey.LAYER0);
    protected static final Model TEMPLATE_CROSSBOW = new Model(Optional.of(ToolTrims.id("item/template_crossbow")), Optional.empty(), TextureKey.LAYER0);

    protected static Identifier getSuffixedModelId(Identifier itemId, String pattern, String material) {
        return ToolTrims.id("item/trims/" + itemId.getNamespace() + "/" + itemId.getPath() + "_" + pattern+ "_" + material);
    }

    protected static JsonArray generateTrimmedOverrides(JsonArray overrides, Identifier modelId, Identifier textureId, Model model, Map<String, Number> extraPredicates, boolean includeBase, BiConsumer<Identifier, Supplier<JsonElement>> writer) {
        if (includeBase) {
            var override = new JsonObject();
            var predicate = new JsonObject();
            extraPredicates.forEach(predicate::addProperty);
            override.add("predicate", predicate);
            override.addProperty("model", modelId.withPrefixedPath("item/").toString());
            overrides.add(override);
        }

        for (var pattern : ToolTrimsDPCompat.legacyPatternOrder) {
            for (var material : ToolTrimsDPCompat.legacyMaterialOrder) {
                var trimmedModelId = getSuffixedModelId(modelId, pattern.getValue().getPath(), material.getValue().getPath());

                var override = new JsonObject();
                var predicate = new JsonObject();
                extraPredicates.forEach(predicate::addProperty);
                predicate.addProperty("custom_model_data", ToolTrimsDPCompat.getCustomModelData(material, pattern));
                override.add("predicate", predicate);
                override.addProperty("model", trimmedModelId.toString());
                overrides.add(override);
            }
        }

        for (var pattern : ToolTrimsPatterns.PATTERNS) {
            for (var material : materials) {
                var trimmedModelId = getSuffixedModelId(modelId, pattern.getValue().getPath(), material.name());
                var trimmedTextureId = getSuffixedModelId(textureId, pattern.getValue().getPath(), material.name());

                model.upload(trimmedModelId, TextureMap.layer0(trimmedTextureId), writer);

                var override = new JsonObject();
                var predicate = new JsonObject();
                extraPredicates.forEach(predicate::addProperty);
                predicate.addProperty("trim_type", material.itemModelIndex());
                predicate.addProperty(TRIM_PATTERN_PREDICATE.toString(), ToolTrimsPatterns.getModelIndex(pattern));
                override.add("predicate", predicate);
                override.addProperty("model", trimmedModelId.toString());
                overrides.add(override);
            }
        }

        return overrides;
    }

    protected static JsonArray generateTrimmedOverrides(JsonArray overrides, Identifier toolId, Model model, Map<String, Number> extraPredicates, boolean includeBase, BiConsumer<Identifier, Supplier<JsonElement>> writer) {
        return generateTrimmedOverrides(overrides, toolId, toolId, model, extraPredicates, includeBase, writer);
    }

    protected static JsonArray generateTrimmedOverrides(Identifier toolId, Model model, BiConsumer<Identifier, Supplier<JsonElement>> writer) {
        return generateTrimmedOverrides(new JsonArray(), toolId, model, Map.of(), false, writer);
    }

    protected static JsonArray generateTrimmedOverrides(Identifier modelId, Identifier textureId, Model model, BiConsumer<Identifier, Supplier<JsonElement>> writer) {
        return generateTrimmedOverrides(new JsonArray(), modelId, textureId, model, Map.of(), false, writer);
    }

    protected static void upload(Model model, Identifier identifier, TextureMap textureMap, BiConsumer<Identifier, Supplier<JsonElement>> writer, Consumer<JsonObject> postProcessJson) {
        model.upload(identifier, textureMap, writer, (id, textures) -> {
            var json = model.createJson(id, textures);
            postProcessJson.accept(json);
            return json;
        });
    }

    protected static void upload(Model model, Item item, TextureMap textureMap, BiConsumer<Identifier, Supplier<JsonElement>> writer, Consumer<JsonObject> postProcessJson) {
        upload(model, ModelIds.getItemModelId(item), textureMap, writer, postProcessJson);
    }

    protected static void upload(Model model, Identifier identifier, TextureMap textureMap, BiConsumer<Identifier, Supplier<JsonElement>> writer, JsonArray overrides) {
        upload(model, identifier, textureMap, writer, json -> json.add("overrides", overrides));
    }

    protected static void upload(Model model, Item item, TextureMap textureMap, BiConsumer<Identifier, Supplier<JsonElement>> writer, JsonArray overrides) {
        upload(model, item, textureMap, writer, json -> json.add("overrides", overrides));
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        for (var item : ToolTrimsItems.SMITHING_TEMPLATES.values()) {
            itemModelGenerator.register(item, Models.GENERATED);
        }

        for (var tool : standardTools) {
            var overrides = generateTrimmedOverrides(Registries.ITEM.getId(tool), Models.HANDHELD, itemModelGenerator.writer);

            upload(Models.HANDHELD, tool, TextureMap.layer0(tool), itemModelGenerator.writer, overrides);
        }

        var bowOverrides = new JsonArray();
        var bowId = Registries.ITEM.getId(Items.BOW);
        generateTrimmedOverrides(bowOverrides, bowId, TEMPLATE_BOW, Map.of(), false, itemModelGenerator.writer);
        generateTrimmedOverrides(bowOverrides, bowId.withSuffixedPath("_pulling_0"), TEMPLATE_BOW, Map.of("pulling", 1), true, itemModelGenerator.writer);
        generateTrimmedOverrides(bowOverrides, bowId.withSuffixedPath("_pulling_1"), TEMPLATE_BOW, Map.of("pulling", 1, "pull", 0.65f), true, itemModelGenerator.writer);
        generateTrimmedOverrides(bowOverrides, bowId.withSuffixedPath("_pulling_2"), TEMPLATE_BOW, Map.of("pulling", 1, "pull", 0.9f), true, itemModelGenerator.writer);
        upload(TEMPLATE_BOW, Items.BOW, TextureMap.layer0(Items.BOW), itemModelGenerator.writer, bowOverrides);

        var crossbowOverrides = new JsonArray();
        var crossbowId = Registries.ITEM.getId(Items.CROSSBOW);
        generateTrimmedOverrides(crossbowOverrides, crossbowId, TEMPLATE_CROSSBOW, Map.of(), false, itemModelGenerator.writer);
        generateTrimmedOverrides(crossbowOverrides, crossbowId.withSuffixedPath("_pulling_0"), TEMPLATE_CROSSBOW, Map.of("pulling", 1), true, itemModelGenerator.writer);
        generateTrimmedOverrides(crossbowOverrides, crossbowId.withSuffixedPath("_pulling_1"), TEMPLATE_CROSSBOW, Map.of("pulling", 1, "pull", 0.58f), true, itemModelGenerator.writer);
        generateTrimmedOverrides(crossbowOverrides, crossbowId.withSuffixedPath("_pulling_2"), TEMPLATE_CROSSBOW, Map.of("pulling", 1, "pull", 1), true, itemModelGenerator.writer);
        generateTrimmedOverrides(crossbowOverrides, crossbowId.withSuffixedPath("_arrow"), TEMPLATE_CROSSBOW, Map.of("charged", 1), true, itemModelGenerator.writer);
        generateTrimmedOverrides(crossbowOverrides, crossbowId.withSuffixedPath("_firework"), TEMPLATE_CROSSBOW, Map.of("charged", 1, "firework", 1), true, itemModelGenerator.writer);
        upload(TEMPLATE_CROSSBOW, Items.CROSSBOW, TextureMap.layer0(TextureMap.getId(Items.CROSSBOW).withSuffixedPath("_standby")), itemModelGenerator.writer, crossbowOverrides);
    }
}
