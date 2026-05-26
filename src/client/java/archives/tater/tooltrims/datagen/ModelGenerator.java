package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.ToolTrims;
import archives.tater.tooltrims.ToolTrimsDPCompat;
import archives.tater.tooltrims.ToolTrimsPatterns;
import archives.tater.tooltrims.client.TrimPatternProperty;
import archives.tater.tooltrims.client.TrimmedTridentModelRenderer;
import archives.tater.tooltrims.item.ToolTrimsItems;
import archives.tater.tooltrims.mixin.client.ItemModelGeneratorAccessor;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.ClientItem;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.properties.numeric.CrossbowPull;
import net.minecraft.client.renderer.item.properties.numeric.CustomModelDataProperty;
import net.minecraft.client.renderer.item.properties.numeric.UseDuration;
import net.minecraft.client.renderer.item.properties.select.Charge;
import net.minecraft.client.renderer.item.properties.select.DisplayContext;
import net.minecraft.client.renderer.item.properties.select.TrimMaterialProperty;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.equipment.trim.TrimMaterials;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class ModelGenerator extends FabricModelProvider {
    public ModelGenerator(FabricPackOutput output) {
        super(output);
    }

    private static final List<ItemModelGenerators.TrimMaterialData> materials = ItemModelGeneratorAccessor.TRIM_MATERIALS();

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
            Items.COPPER_SWORD,
            Items.COPPER_SHOVEL,
            Items.COPPER_PICKAXE,
            Items.COPPER_AXE,
            Items.COPPER_HOE,
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

    private static final List<Item> spears = List.of(
            Items.WOODEN_SPEAR,
            Items.STONE_SPEAR,
            Items.COPPER_SPEAR,
            Items.GOLDEN_SPEAR,
            Items.IRON_SPEAR,
            Items.DIAMOND_SPEAR,
            Items.NETHERITE_SPEAR
    );

    public static final ModelTemplate TWO_LAYERED_SPEAR_IN_HAND = new ModelTemplate(Optional.of(Identifier.withDefaultNamespace("item/spear_in_hand")), Optional.of("_in_hand"), TextureSlot.LAYER0, TextureSlot.LAYER1);

    protected static Identifier getSuffixedModelId(Identifier itemId, String pattern, String material) {
        return ToolTrims.id("item/trims/" + itemId.getNamespace() + "/" + itemId.getPath() + "_" + pattern+ "_" + material);
    }

    protected static ItemModel.Unbaked generateTrimmedToolModels(Identifier baseId, ModelTemplate model, ItemModelGenerators modelGenerator) {
        return generateTrimmedToolModels(baseId, baseId, model, modelGenerator);
    }

    protected static ItemModel.Unbaked generateTrimmedToolModels(Identifier modelId, Identifier textureId, ModelTemplate model, ItemModelGenerators modelGenerator) {
        var baseTextureId = modelId.withPrefix("item/");
        return ItemModelUtils.select(
                TrimPatternProperty.INSTANCE,
                // Legacy
                ItemModelUtils.rangeSelect(
                        new CustomModelDataProperty(0),
                        ItemModelUtils.plainModel(baseTextureId),
                        Stream.concat(
                                ToolTrimsDPCompat.legacyPatternOrder.stream().flatMap(pattern ->
                                        ToolTrimsDPCompat.legacyMaterialOrder.stream().map(material ->
                                                ItemModelUtils.override(
                                                        ItemModelUtils.plainModel(getSuffixedModelId(modelId, pattern.identifier().getPath(), material.identifier().getPath())),
                                                        ToolTrimsDPCompat.getCustomModelData(material, pattern)
                                                )
                                        )
                                ),
                                ToolTrimsDPCompat.legacyPatternOrder.stream().map(pattern ->
                                        ItemModelUtils.override(
                                                ItemModelUtils.plainModel(getSuffixedModelId(modelId, pattern.identifier().getPath(), TrimMaterials.RESIN.identifier().getPath())),
                                                ToolTrimsDPCompat.getCustomModelData(TrimMaterials.RESIN, pattern)
                                        )
                                )
                        ).toList()),
                // Normal
                ToolTrimsPatterns.PATTERNS.stream().map(pattern ->
                        ItemModelUtils.when(pattern, ItemModelUtils.select(new TrimMaterialProperty(), materials.stream().map(material -> {
                            var trimmedModelId = getSuffixedModelId(modelId, pattern.identifier().getPath(), material.materialKey().identifier().getPath());
                            var trimmedTextureId = getSuffixedModelId(textureId, pattern.identifier().getPath(), material.materialKey().identifier().getPath());
                            var mapping = model == TWO_LAYERED_SPEAR_IN_HAND || model == ModelTemplates.TWO_LAYERED_ITEM
                                    ? TextureMapping.layered(new Material(baseTextureId), new Material(trimmedTextureId))
                                    : TextureMapping.layer0(new Material(trimmedTextureId));
                            model.create(trimmedModelId, mapping, modelGenerator.modelOutput);
                            return ItemModelUtils.when(material.materialKey(), ItemModelUtils.plainModel(trimmedModelId));
                        }).toList()))
                ).toList()
        );
    }

    protected static void registerTrimmedTool(Item item, ModelTemplate model, ItemModelGenerators modelGenerator) {
        modelGenerator.itemModelOutput.accept(item, generateTrimmedToolModels(BuiltInRegistries.ITEM.getKey(item), model, modelGenerator));
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
            registerTrimmedTool(tool, ModelTemplates.FLAT_HANDHELD_ITEM, itemModelGenerator);
        }

        registerTrimmedTool(Items.MACE, ModelTemplates.FLAT_HANDHELD_MACE_ITEM, itemModelGenerator);

        for (var spear : spears) {
            var id = BuiltInRegistries.ITEM.getKey(spear);
            itemModelGenerator.itemModelOutput.accept(spear, ItemModelGenerators.createFlatModelDispatch(
                    generateTrimmedToolModels(id, ModelTemplates.TWO_LAYERED_ITEM, itemModelGenerator),
                    generateTrimmedToolModels(id.withSuffix("_in_hand"), TWO_LAYERED_SPEAR_IN_HAND, itemModelGenerator)
            ), new ClientItem.Properties(true, false, 1.95F));
        }

        var bowId = BuiltInRegistries.ITEM.getKey(Items.BOW);
        var bowModel = new ModelTemplate(Optional.of(ModelLocationUtils.getModelLocation(Items.BOW)), Optional.empty(), TextureSlot.LAYER0);
        itemModelGenerator.itemModelOutput.accept(Items.BOW, ItemModelUtils.conditional(
                ItemModelUtils.isUsingItem(),
                ItemModelUtils.rangeSelect(
                        new UseDuration(false),
                        0.05f,
                        generateTrimmedToolModels(bowId.withSuffix("_pulling_0"), bowModel, itemModelGenerator),
                        ItemModelUtils.override(generateTrimmedToolModels(bowId.withSuffix("_pulling_1"), bowModel, itemModelGenerator), 0.65f),
                        ItemModelUtils.override(generateTrimmedToolModels(bowId.withSuffix("_pulling_2"), bowModel, itemModelGenerator), 0.9f)
                ),
                generateTrimmedToolModels(bowId, bowModel, itemModelGenerator)
        ));

        var crossbowId = BuiltInRegistries.ITEM.getKey(Items.CROSSBOW);
        var crossbowModel = new ModelTemplate(Optional.of(ModelLocationUtils.getModelLocation(Items.CROSSBOW)), Optional.empty(), TextureSlot.LAYER0);
        itemModelGenerator.itemModelOutput.accept(Items.CROSSBOW, ItemModelUtils.select(
                new Charge(),
                ItemModelUtils.conditional(
                        ItemModelUtils.isUsingItem(),
                        ItemModelUtils.rangeSelect(
                                new CrossbowPull(),
                                generateTrimmedToolModels(crossbowId.withSuffix("_pulling_0"), crossbowModel, itemModelGenerator),
                                ItemModelUtils.override(generateTrimmedToolModels(crossbowId.withSuffix("_pulling_1"), crossbowModel, itemModelGenerator), 0.58f),
                                ItemModelUtils.override(generateTrimmedToolModels(crossbowId.withSuffix("_pulling_2"), crossbowModel, itemModelGenerator), 1f)
                        ),
                        generateTrimmedToolModels(crossbowId, crossbowModel, itemModelGenerator)
                ),
                ItemModelUtils.when(CrossbowItem.ChargeType.ARROW, generateTrimmedToolModels(crossbowId.withSuffix("_arrow"), crossbowModel, itemModelGenerator)),
                ItemModelUtils.when(CrossbowItem.ChargeType.ROCKET, generateTrimmedToolModels(crossbowId.withSuffix("_firework"), crossbowModel, itemModelGenerator))
        ));

        var tridentId = BuiltInRegistries.ITEM.getKey(Items.TRIDENT);
        itemModelGenerator.itemModelOutput.accept(Items.TRIDENT, ItemModelUtils.select(
                new DisplayContext(),
                ItemModelUtils.conditional(
                        ItemModelUtils.isUsingItem(),
                        ItemModelUtils.specialModel(tridentId.withPrefix("item/").withSuffix("_throwing"), new TrimmedTridentModelRenderer.Unbaked()),
                        ItemModelUtils.specialModel(tridentId.withPrefix("item/").withSuffix("_in_hand"), new TrimmedTridentModelRenderer.Unbaked())
                ),
                ItemModelUtils.when(List.of(ItemDisplayContext.GUI, ItemDisplayContext.GROUND, ItemDisplayContext.FIXED), generateTrimmedToolModels(tridentId, ModelTemplates.FLAT_ITEM, itemModelGenerator))
        ));
    }
}
