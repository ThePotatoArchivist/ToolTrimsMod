package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.ToolTrims;
import archives.tater.tooltrims.ToolTrimsDPCompat;
import archives.tater.tooltrims.ToolTrimsPatterns;
import archives.tater.tooltrims.item.ToolTrimsItems;
import archives.tater.tooltrims.mixin.ItemModelGeneratorAccessor;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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

    private static final List<ItemModelGenerators.TrimModelData> materials = ItemModelGeneratorAccessor.TRIM_MATERIALS();

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
            Items.NETHERITE_HOE,
            Items.TRIDENT
    );

    protected static final ModelTemplate TEMPLATE_BOW = new ModelTemplate(Optional.of(ToolTrims.id("item/template_bow")), Optional.empty(), TextureSlot.LAYER0);
    protected static final ModelTemplate TEMPLATE_CROSSBOW = new ModelTemplate(Optional.of(ToolTrims.id("item/template_crossbow")), Optional.empty(), TextureSlot.LAYER0);

    protected static ResourceLocation getSuffixedModelId(ResourceLocation itemId, String pattern, String material) {
        return ToolTrims.id("item/trims/" + itemId.getNamespace() + "/" + itemId.getPath() + "_" + pattern+ "_" + material);
    }

    protected static JsonArray generateTrimmedOverrides(JsonArray overrides, ResourceLocation modelId, ResourceLocation textureId, ModelTemplate model, Map<String, Number> extraPredicates, boolean includeBase, boolean upload, BiConsumer<ResourceLocation, Supplier<JsonElement>> writer) {
        if (includeBase) {
            var override = new JsonObject();
            var predicate = new JsonObject();
            extraPredicates.forEach(predicate::addProperty);
            override.add("predicate", predicate);
            override.addProperty("model", modelId.withPrefix("item/").toString());
            overrides.add(override);
        }

        for (var pattern : ToolTrimsDPCompat.legacyPatternOrder) {
            for (var material : ToolTrimsDPCompat.legacyMaterialOrder) {
                var trimmedModelId = getSuffixedModelId(modelId, pattern.location().getPath(), material.location().getPath());

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
                var trimmedModelId = getSuffixedModelId(modelId, pattern.location().getPath(), material.name());
                var trimmedTextureId = getSuffixedModelId(textureId, pattern.location().getPath(), material.name());

                if (upload) model.create(trimmedModelId, TextureMapping.layer0(trimmedTextureId), writer);

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

    protected static JsonArray generateTrimmedOverrides(JsonArray overrides, ResourceLocation toolId, ModelTemplate model, Map<String, Number> extraPredicates, boolean includeBase, boolean upload, BiConsumer<ResourceLocation, Supplier<JsonElement>> writer) {
        return generateTrimmedOverrides(overrides, toolId, toolId, model, extraPredicates, includeBase, upload, writer);
    }

    protected static JsonArray generateTrimmedOverrides(JsonArray overrides, ResourceLocation toolId, ModelTemplate model, Map<String, Number> extraPredicates, boolean includeBase, BiConsumer<ResourceLocation, Supplier<JsonElement>> writer) {
        return generateTrimmedOverrides(overrides, toolId, toolId, model, extraPredicates, includeBase, true, writer);
    }

    protected static JsonArray generateTrimmedOverrides(ResourceLocation toolId, ModelTemplate model, BiConsumer<ResourceLocation, Supplier<JsonElement>> writer) {
        return generateTrimmedOverrides(new JsonArray(), toolId, model, Map.of(), false, true, writer);
    }

    protected static JsonArray generateTrimmedOverrides(ResourceLocation modelId, ResourceLocation textureId, ModelTemplate model, BiConsumer<ResourceLocation, Supplier<JsonElement>> writer) {
        return generateTrimmedOverrides(new JsonArray(), modelId, textureId, model, Map.of(), false, true, writer);
    }

    protected static void upload(ModelTemplate model, ResourceLocation identifier, TextureMapping textureMap, BiConsumer<ResourceLocation, Supplier<JsonElement>> writer, Consumer<JsonObject> postProcessJson) {
        model.create(identifier, textureMap, writer, (id, textures) -> {
            var json = model.createBaseTemplate(id, textures);
            postProcessJson.accept(json);
            return json;
        });
    }

    protected static void upload(ModelTemplate model, Item item, TextureMapping textureMap, BiConsumer<ResourceLocation, Supplier<JsonElement>> writer, Consumer<JsonObject> postProcessJson) {
        upload(model, ModelLocationUtils.getModelLocation(item), textureMap, writer, postProcessJson);
    }

    protected static void upload(ModelTemplate model, ResourceLocation identifier, TextureMapping textureMap, BiConsumer<ResourceLocation, Supplier<JsonElement>> writer, JsonArray overrides) {
        upload(model, identifier, textureMap, writer, json -> json.add("overrides", overrides));
    }

    protected static void upload(ModelTemplate model, Item item, TextureMapping textureMap, BiConsumer<ResourceLocation, Supplier<JsonElement>> writer, JsonArray overrides) {
        upload(model, item, textureMap, writer, json -> json.add("overrides", overrides));
    }

    protected static JsonArray generateCrossbowOverrides(ItemModelGenerators itemModelGenerator, boolean upload) {
        var crossbowOverrides = new JsonArray();
        var crossbowId = BuiltInRegistries.ITEM.getKey(Items.CROSSBOW);
        generateTrimmedOverrides(crossbowOverrides, crossbowId, TEMPLATE_CROSSBOW, Map.of(), false, upload, itemModelGenerator.output);
        generateTrimmedOverrides(crossbowOverrides, crossbowId.withSuffix("_pulling_0"), TEMPLATE_CROSSBOW, Map.of("pulling", 1), true, upload, itemModelGenerator.output);
        generateTrimmedOverrides(crossbowOverrides, crossbowId.withSuffix("_pulling_1"), TEMPLATE_CROSSBOW, Map.of("pulling", 1, "pull", 0.58f), true, upload, itemModelGenerator.output);
        generateTrimmedOverrides(crossbowOverrides, crossbowId.withSuffix("_pulling_2"), TEMPLATE_CROSSBOW, Map.of("pulling", 1, "pull", 1), true, upload, itemModelGenerator.output);
        generateTrimmedOverrides(crossbowOverrides, crossbowId.withSuffix("_arrow"), TEMPLATE_CROSSBOW, Map.of("charged", 1), true, upload, itemModelGenerator.output);
        generateTrimmedOverrides(crossbowOverrides, crossbowId.withSuffix("_firework"), TEMPLATE_CROSSBOW, Map.of("charged", 1, "firework", 1), true, upload, itemModelGenerator.output);
        return crossbowOverrides;
    }

    protected static void uploadCrossbow(ItemModelGenerators itemModelGenerator, JsonArray crossbowOverrides) {
        upload(TEMPLATE_CROSSBOW, Items.CROSSBOW, TextureMapping.layer0(TextureMapping.getItemTexture(Items.CROSSBOW).withSuffix("_standby")), itemModelGenerator.output, crossbowOverrides);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        for (var item : ToolTrimsItems.SMITHING_TEMPLATES.values()) {
            itemModelGenerator.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
        }

        for (var tool : standardTools) {
            var overrides = generateTrimmedOverrides(BuiltInRegistries.ITEM.getKey(tool), ModelTemplates.FLAT_HANDHELD_ITEM, itemModelGenerator.output);

            upload(ModelTemplates.FLAT_HANDHELD_ITEM, tool, TextureMapping.layer0(tool), itemModelGenerator.output, overrides);
        }

        upload(ModelTemplates.FLAT_HANDHELD_MACE_ITEM, Items.MACE, TextureMapping.layer0(Items.MACE), itemModelGenerator.output,
                generateTrimmedOverrides(BuiltInRegistries.ITEM.getKey(Items.MACE), ModelTemplates.FLAT_HANDHELD_MACE_ITEM, itemModelGenerator.output));

        var bowOverrides = new JsonArray();
        var bowId = BuiltInRegistries.ITEM.getKey(Items.BOW);
        generateTrimmedOverrides(bowOverrides, bowId, TEMPLATE_BOW, Map.of(), false, itemModelGenerator.output);
        generateTrimmedOverrides(bowOverrides, bowId.withSuffix("_pulling_0"), TEMPLATE_BOW, Map.of("pulling", 1), true, itemModelGenerator.output);
        generateTrimmedOverrides(bowOverrides, bowId.withSuffix("_pulling_1"), TEMPLATE_BOW, Map.of("pulling", 1, "pull", 0.65f), true, itemModelGenerator.output);
        generateTrimmedOverrides(bowOverrides, bowId.withSuffix("_pulling_2"), TEMPLATE_BOW, Map.of("pulling", 1, "pull", 0.9f), true, itemModelGenerator.output);
        upload(TEMPLATE_BOW, Items.BOW, TextureMapping.layer0(Items.BOW), itemModelGenerator.output, bowOverrides);

        uploadCrossbow(itemModelGenerator, generateCrossbowOverrides(itemModelGenerator, true));
    }
}
