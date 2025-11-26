package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.ToolTrims;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import java.util.Optional;

public class EnchancementModelGenerator extends ModelGenerator {
    public EnchancementModelGenerator(FabricDataOutput output) {
        super(output);
    }

    public static ResourceLocation enchancementId(String path) {
        return ResourceLocation.fromNamespaceAndPath("enchancement", path);
    }

    private record CrossbowModel(
            String path,
            String predicate,
            float predicateValue,
            Item fakeItem
    ) {
        public CrossbowModel(String path, String predicate, float predicateValue) {
            this(path, predicate, predicateValue,
                    Items.registerItem(ResourceKey.create(Registries.ITEM, enchancementId(path)), Item::new, new Item.Properties()));
        }

        public CrossbowModel(String path, String predicate) {
            this(path, predicate, 1);
        }
    }

    private static final CrossbowModel[] crossbowModels = {
            new CrossbowModel("crossbow_amethyst", "amethyst_shard"),
            new CrossbowModel("crossbow_torch", "torch"),
            new CrossbowModel("crossbow_brimstone_0", "brimstone", 1 / 6f),
            new CrossbowModel("crossbow_brimstone_1", "brimstone", 2 / 6f),
            new CrossbowModel("crossbow_brimstone_2", "brimstone", 3 / 6f),
            new CrossbowModel("crossbow_brimstone_3", "brimstone", 4 / 6f),
            new CrossbowModel("crossbow_brimstone_4", "brimstone", 5 / 6f),
            new CrossbowModel("crossbow_brimstone_5", "brimstone", 1f)
    };

    // Needed to register fake items
    public static void register() {}

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        var twoLayersTemplate = new ModelTemplate(Optional.of(ToolTrims.id("item/template_crossbow")), Optional.empty(), TextureSlot.LAYER1);

        var crossbowPullId = ResourceLocation.withDefaultNamespace("crossbow_pulling_2");

        for (var model : crossbowModels) {
            var templateModelId = ToolTrims.id(model.path).withPrefix("item/enchancement/");
            twoLayersTemplate.create(templateModelId, new TextureMapping().put(TextureSlot.LAYER1, templateModelId), itemModelGenerator.modelOutput);
            var templateModel = new ModelTemplate(Optional.of(templateModelId), Optional.empty(), TextureSlot.LAYER0);
            itemModelGenerator.itemModelOutput.accept(model.fakeItem, generateTrimmedToolModels(enchancementId(model.path), crossbowPullId, templateModel, itemModelGenerator));
        }

//        var crossbowOverrides = generateCrossbowOverrides(itemModelGenerator, false);
//        for (var model : crossbowModels) {
//            var templateModelId = ToolTrims.id(model.path).withPrefixedPath("item/enchancement/");
//            twoLayersTemplate.upload(templateModelId, new TextureMap().put(TextureKey.LAYER1, templateModelId), itemModelGenerator.modelCollector);
//            var templateModel = new Model(Optional.of(templateModelId), Optional.empty(), TextureKey.LAYER0);
//            registerTrimmedTool(crossbowOverrides, enchancementId(model.path), crossbowPullId, templateModel, Map.of("charged", 1, enchancementId(model.predicate).toString(), model.predicateValue), true, true, itemModelGenerator.modelCollector);
//        }
//
//        uploadCrossbow(itemModelGenerator, crossbowOverrides);
    }
}
