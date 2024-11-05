package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.ToolTrims;
import archives.tater.tooltrims.ToolTrimsDPCompat;
import archives.tater.tooltrims.ToolTrimsPatterns;
import archives.tater.tooltrims.TrimPatternProperty;
import archives.tater.tooltrims.item.ToolTrimsItems;
import archives.tater.tooltrims.mixin.client.ItemModelGeneratorAccessor;
import com.google.common.collect.Streams;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.*;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.property.numeric.CrossbowPullProperty;
import net.minecraft.client.render.item.property.numeric.CustomModelDataFloatProperty;
import net.minecraft.client.render.item.property.numeric.UseDurationProperty;
import net.minecraft.client.render.item.property.select.ChargeTypeProperty;
import net.minecraft.client.render.item.property.select.TrimMaterialProperty;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.equipment.trim.ArmorTrimMaterial;
import net.minecraft.item.equipment.trim.ArmorTrimMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Optional;

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
            Items.NETHERITE_HOE,
            Items.MACE
    );

    protected static final Model TEMPLATE_BOW = new Model(Optional.of(ToolTrims.id("item/template_bow")), Optional.empty(), TextureKey.LAYER0);
    protected static final Model TEMPLATE_CROSSBOW = new Model(Optional.of(ToolTrims.id("item/template_crossbow")), Optional.empty(), TextureKey.LAYER0);

    protected static Identifier getSuffixedModelId(Identifier itemId, String pattern, String material) {
        return ToolTrims.id("item/trims/" + itemId.getNamespace() + "/" + itemId.getPath() + "_" + pattern+ "_" + material);
    }

    protected static ItemModel.Unbaked generateTrimmedToolModels(Identifier baseId, Model model, ItemModelGenerator modelGenerator) {
        return generateTrimmedToolModels(baseId, baseId, model, modelGenerator);
    }

    protected static ItemModel.Unbaked generateTrimmedToolModels(Identifier modelId, Identifier textureId, Model model, ItemModelGenerator modelGenerator) {
        return ItemModels.select(
                TrimPatternProperty.INSTANCE,
                // Legacy
                ItemModels.rangeDispatch(
                        new CustomModelDataFloatProperty(0),
                        ItemModels.basic(modelId.withPrefixedPath("item/")),
                        Streams.concat(
                                ToolTrimsDPCompat.legacyPatternOrder.stream().flatMap(pattern ->
                                        ToolTrimsDPCompat.legacyMaterialOrder.stream().map(material ->
                                                ItemModels.rangeDispatchEntry(
                                                        ItemModels.basic(getSuffixedModelId(modelId, pattern.getValue().getPath(), material.getValue().getPath())),
                                                        ToolTrimsDPCompat.getCustomModelData(material, pattern)
                                                )
                                        )
                                ),
                                ToolTrimsDPCompat.legacyPatternOrder.stream().map(pattern ->
                                        ItemModels.rangeDispatchEntry(
                                                ItemModels.basic(getSuffixedModelId(modelId, pattern.getValue().getPath(), ArmorTrimMaterials.RESIN.getValue().getPath())),
                                                ToolTrimsDPCompat.getCustomModelData(ArmorTrimMaterials.RESIN, pattern)
                                        )
                                )
                        ).toList()),
                // Normal
                ToolTrimsPatterns.PATTERNS.stream().map(pattern ->
                        ItemModels.switchCase(pattern, ItemModels.select(new TrimMaterialProperty(), materials.stream().map(material -> {
                            var trimmedModelId = getSuffixedModelId(modelId, pattern.getValue().getPath(), material.name());
                            var trimmedTextureId = getSuffixedModelId(textureId, pattern.getValue().getPath(), material.name());
                            model.upload(trimmedModelId, TextureMap.layer0(trimmedTextureId), modelGenerator.modelCollector);
                            return ItemModels.switchCase(material.materialKey(), ItemModels.basic(trimmedModelId));
                        }).toList()))
                ).toList()
        );
    }

    protected static void registerTrimmedTool(Item item, Model model, ItemModelGenerator modelGenerator) {
        modelGenerator.output.accept(item, generateTrimmedToolModels(Registries.ITEM.getId(item), model, modelGenerator));
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
            registerTrimmedTool(tool, Models.HANDHELD, itemModelGenerator);
        }

        var bowId = Registries.ITEM.getId(Items.BOW);
        itemModelGenerator.output.accept(Items.BOW, ItemModels.condition(
                ItemModels.usingItemProperty(),
                ItemModels.rangeDispatch(
                        new UseDurationProperty(false),
                        0.05f,
                        generateTrimmedToolModels(bowId.withSuffixedPath("_pulling_0"), TEMPLATE_BOW, itemModelGenerator),
                        ItemModels.rangeDispatchEntry(generateTrimmedToolModels(bowId.withSuffixedPath("_pulling_1"), TEMPLATE_BOW, itemModelGenerator), 0.65f),
                        ItemModels.rangeDispatchEntry(generateTrimmedToolModels(bowId.withSuffixedPath("_pulling_2"), TEMPLATE_BOW, itemModelGenerator), 0.9f)
                ),
                generateTrimmedToolModels(bowId, TEMPLATE_BOW, itemModelGenerator)
        ));

        var itemId = Registries.ITEM.getId(Items.CROSSBOW);
        itemModelGenerator.output.accept(Items.CROSSBOW, ItemModels.condition(
                ItemModels.usingItemProperty(),
                ItemModels.rangeDispatch(
                        new CrossbowPullProperty(),
                        generateTrimmedToolModels(itemId.withSuffixedPath("_pulling_0"), TEMPLATE_CROSSBOW, itemModelGenerator),
                        ItemModels.rangeDispatchEntry(generateTrimmedToolModels(itemId.withSuffixedPath("_pulling_1"), TEMPLATE_CROSSBOW, itemModelGenerator), 0.58f),
                        ItemModels.rangeDispatchEntry(generateTrimmedToolModels(itemId.withSuffixedPath("_pulling_2"), TEMPLATE_CROSSBOW, itemModelGenerator), 1f)
                ),
                ItemModels.select(
                        new ChargeTypeProperty(),
                        generateTrimmedToolModels(itemId, TEMPLATE_CROSSBOW, itemModelGenerator),
                        ItemModels.switchCase(CrossbowItem.ChargeType.ARROW, generateTrimmedToolModels(itemId.withSuffixedPath("_arrow"), TEMPLATE_CROSSBOW, itemModelGenerator)),
                        ItemModels.switchCase(CrossbowItem.ChargeType.ROCKET, generateTrimmedToolModels(itemId.withSuffixedPath("_firework"), TEMPLATE_CROSSBOW, itemModelGenerator))
                )
        ));
    }
}
