package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.ToolTrims;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.Optional;

public class EnchancementModelGenerator extends ModelGenerator {
    public EnchancementModelGenerator(FabricDataOutput output) {
        super(output);
    }

    public static Identifier enchancementId(String path) {
        return Identifier.of("enchancement", path);
    }

    private record CrossbowModel(
            String path,
            String predicate,
            float predicateValue
    ) {
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

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        var twoLayersTemplate = new Model(Optional.of(ToolTrims.id("item/template_crossbow")), Optional.empty(), TextureKey.LAYER1);

        var crossbowPullId = Identifier.ofVanilla("crossbow_pulling_2");
        var crossbowOverrides = generateCrossbowOverrides(itemModelGenerator, false);
        for (var model : crossbowModels) {
            var templateModelId = ToolTrims.id(model.path).withPrefixedPath("item/enchancement/");
            twoLayersTemplate.upload(templateModelId, new TextureMap().put(TextureKey.LAYER1, templateModelId), itemModelGenerator.writer);
            var templateModel = new Model(Optional.of(templateModelId), Optional.empty(), TextureKey.LAYER0);
            generateTrimmedOverrides(crossbowOverrides, enchancementId(model.path), crossbowPullId, templateModel, Map.of("charged", 1, enchancementId(model.predicate).toString(), model.predicateValue), true, true, itemModelGenerator.writer);
        }

        uploadCrossbow(itemModelGenerator, crossbowOverrides);
    }
}
