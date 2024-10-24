package archives.tater.tooltrims.datagen;

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
import net.minecraft.item.trim.ArmorTrimPattern;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static archives.tater.tooltrims.ToolTrims.MOD_ID;
import static archives.tater.tooltrims.ToolTrimsPatterns.TRIM_PATTERN_PREDICATE;

public class ModelGenerator extends FabricModelProvider {
    public ModelGenerator(FabricDataOutput output) {
        super(output);
    }

    public static List<ItemModelGenerator.TrimMaterial> materials = ItemModelGeneratorAccessor.TRIM_MATERIALS();

    private static final Map<String, RegistryKey<ArmorTrimPattern>> patterns = Map.of(
            "linear", ToolTrimsPatterns.LINEAR,
            "tracks", ToolTrimsPatterns.TRACKS,
            "charge", ToolTrimsPatterns.CHARGE,
            "frost", ToolTrimsPatterns.FROST
    );

    private static final List<String> legacyMaterialOrder = List.of(
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

    private static final List<String> patternOrder = List.of(
            "linear",
            "tracks",
            "charge",
            "frost"
    );

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

    private static final Map<String, Map<String, Vector3f>> bowDisplay = Map.of(
            "thirdperson_righthand", Map.of(
                    "rotation", new Vector3f(-80, 260, -40),
                    "translation", new Vector3f(-1, -2, 2.5f),
                    "scale", new Vector3f(0.9f, 0.9f, 0.9f)
            ),
            "thirdperson_lefthand", Map.of(
                    "rotation", new Vector3f(-80, -280, 40),
                    "translation", new Vector3f(-1, -2, 2.5f),
                    "scale", new Vector3f(0.9f, 0.9f, 0.9f)
            ),
            "firstperson_righthand", Map.of(
                    "rotation", new Vector3f(0, -90, 25),
                    "translation", new Vector3f(1.13f, 3.2f, 1.13f),
                    "scale", new Vector3f(0.68f, 0.68f, 0.68f)
            ),
            "firstperson_lefthand", Map.of(
                    "rotation", new Vector3f(0, 90, -25),
                    "translation", new Vector3f(1.13f, 3.2f, 1.13f),
                    "scale", new Vector3f(0.68f, 0.68f, 0.68f)
            )
    );

    private static final Map<String, Map<String, Vector3f>> crossbowDisplay = Map.of(
            "thirdperson_righthand", Map.of(
                    "rotation", new Vector3f(-90, 0, -60),
                    "translation", new Vector3f(2, 0.1f, -3),
                    "scale", new Vector3f(0.9f, 0.9f, 0.9f)
            ),
            "thirdperson_lefthand", Map.of(
                "rotation", new Vector3f(-90, 0, 30),
                "translation", new Vector3f(2, 0.1f, -3),
                "scale", new Vector3f(0.9f, 0.9f, 0.9f)
            ),
            "firstperson_righthand", Map.of(
                "rotation", new Vector3f(-90, 0, -55),
                "translation", new Vector3f(1.13f, 3.2f, 1.13f),
                "scale", new Vector3f(0.68f, 0.68f, 0.68f)
            ),
            "firstperson_lefthand", Map.of(
                    "rotation", new Vector3f(-90, 0, 35),
                    "translation", new Vector3f(1.13f, 3.2f, 1.13f),
                    "scale", new Vector3f(0.68f, 0.68f, 0.68f)
            )
    );

    public static <T> JsonObject mapToJson(Map<String, T> map, Function<T, JsonElement> transform) {
        var object = new JsonObject();
        map.forEach((key, value) -> object.add(key, transform.apply(value)));
        return object;
    }

    public static JsonArray jsonArrayOf(float ...values) {
        var array = new JsonArray();
        for (var v : values) {
            array.add(v);
        }
        return array;
    }

    public static JsonObject displayJson(Map<String, Map<String, Vector3f>> display) {
        return mapToJson(display, view -> mapToJson(view, vec -> jsonArrayOf(vec.x, vec.y, vec.z)));
    }

    private Identifier getSuffixedModelId(Identifier itemId, String pattern, String material) {
        return new Identifier(MOD_ID, "item/trims/" + itemId.getNamespace() + "/" + itemId.getPath() + "_" + pattern+ "_" + material);
    }

    private JsonArray generateTrimmedOverrides(JsonArray overrides, Identifier toolId, Model model, Map<String, Number> extraPredicates, boolean includeBase, BiConsumer<Identifier, Supplier<JsonElement>> writer) {
        if (includeBase) {
            var override = new JsonObject();
            var predicate = new JsonObject();
            extraPredicates.forEach(predicate::addProperty);
            override.add("predicate", predicate);
            override.addProperty("model", toolId.withPrefixedPath("item/").toString());
            overrides.add(override);
        }

        for (var patternName : patternOrder) {
            for (var materialName : legacyMaterialOrder) {
                var trimmedId = getSuffixedModelId(toolId, patternName, materialName);

                var override = new JsonObject();
                var predicate = new JsonObject();
                extraPredicates.forEach(predicate::addProperty);
                predicate.addProperty("custom_model_data", 311001 + patternOrder.indexOf(patternName) * legacyMaterialOrder.size() + legacyMaterialOrder.indexOf(materialName));
                override.add("predicate", predicate);
                override.addProperty("model", trimmedId.toString());
                overrides.add(override);
            }
        }

        for (var patternName : patternOrder) {
            var pattern = patterns.get(patternName);
            for (var material : materials) {
                var materialName = material.name();
                var trimmedId = getSuffixedModelId(toolId, patternName, materialName);

                model.upload(trimmedId, TextureMap.layer0(trimmedId), writer);

                var override = new JsonObject();
                var predicate = new JsonObject();
                extraPredicates.forEach(predicate::addProperty);
                predicate.addProperty("trim_type", material.itemModelIndex());
                predicate.addProperty(TRIM_PATTERN_PREDICATE.toString(), ToolTrimsPatterns.getModelIndex(pattern));
                override.add("predicate", predicate);
                override.addProperty("model", trimmedId.toString());
                overrides.add(override);
            }
        }

        return overrides;
    }

    private JsonArray generateTrimmedOverrides(Identifier toolId, Model model, BiConsumer<Identifier, Supplier<JsonElement>> writer) {
        return generateTrimmedOverrides(new JsonArray(), toolId, model, Map.of(), false, writer);
    }

    private void upload(Model model, Item item, TextureMap textureMap, BiConsumer<Identifier, Supplier<JsonElement>> writer, Consumer<JsonObject> postProcessJson) {
        model.upload(ModelIds.getItemModelId(item), textureMap, writer, (id, textures) -> {
            var json = model.createJson(id, textures);
            postProcessJson.accept(json);
            return json;
        });
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

            upload(Models.HANDHELD, tool, TextureMap.layer0(tool), itemModelGenerator.writer, json -> {
                json.add("overrides", overrides);
            });
        }

        var bowOverrides = new JsonArray();
        var bowId = Registries.ITEM.getId(Items.BOW);
        var bowModel = new Model(Optional.of(ModelIds.getItemModelId(Items.BOW)), Optional.empty(), TextureKey.LAYER0);
        generateTrimmedOverrides(bowOverrides, bowId, bowModel, Map.of(), false, itemModelGenerator.writer);
        generateTrimmedOverrides(bowOverrides, bowId.withSuffixedPath("_pulling_0"), bowModel, Map.of("pulling", 1), true, itemModelGenerator.writer);
        generateTrimmedOverrides(bowOverrides, bowId.withSuffixedPath("_pulling_1"), bowModel, Map.of("pulling", 1, "pull", 0.65f), true, itemModelGenerator.writer);
        generateTrimmedOverrides(bowOverrides, bowId.withSuffixedPath("_pulling_2"), bowModel, Map.of("pulling", 1, "pull", 0.9f), true, itemModelGenerator.writer);
        upload(Models.GENERATED, Items.BOW, TextureMap.layer0(Items.BOW), itemModelGenerator.writer, json -> {
            json.add("overrides", bowOverrides);
            json.add("display", displayJson(bowDisplay));
        });

        var crossbowOverrides = new JsonArray();
        var crossbowId = Registries.ITEM.getId(Items.CROSSBOW);
        var crossbowModel = new Model(Optional.of(ModelIds.getItemModelId(Items.CROSSBOW)), Optional.empty(), TextureKey.LAYER0);
        generateTrimmedOverrides(crossbowOverrides, crossbowId, crossbowModel, Map.of(), false, itemModelGenerator.writer);
        generateTrimmedOverrides(crossbowOverrides, crossbowId.withSuffixedPath("_pulling_0"), crossbowModel, Map.of("pulling", 1), true, itemModelGenerator.writer);
        generateTrimmedOverrides(crossbowOverrides, crossbowId.withSuffixedPath("_pulling_1"), crossbowModel, Map.of("pulling", 1, "pull", 0.58f), true, itemModelGenerator.writer);
        generateTrimmedOverrides(crossbowOverrides, crossbowId.withSuffixedPath("_pulling_2"), crossbowModel, Map.of("pulling", 1, "pull", 1), true, itemModelGenerator.writer);
        generateTrimmedOverrides(crossbowOverrides, crossbowId.withSuffixedPath("_arrow"), crossbowModel, Map.of("charged", 1), true, itemModelGenerator.writer);
        generateTrimmedOverrides(crossbowOverrides, crossbowId.withSuffixedPath("_firework"), crossbowModel, Map.of("charged", 1, "firework", 1), true, itemModelGenerator.writer);
        upload(Models.GENERATED, Items.CROSSBOW, TextureMap.layer0(TextureMap.getId(Items.CROSSBOW).withSuffixedPath("_standby")), itemModelGenerator.writer, json -> {
            json.add("overrides", crossbowOverrides);
            json.add("display", displayJson(crossbowDisplay));
        });
    }
}
