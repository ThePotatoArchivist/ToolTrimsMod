package archives.tater.tooltrims.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.resources.model.sprite.Material;

public class ModelGenerator extends FabricModelProvider {
    public ModelGenerator(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {

    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {
        for (var tool : TrimAssets.HANDHELD)
            for (var material : TrimAssets.TRIM_MATERIALS) {
                var id = tool.withSuffix("_" + material);
                ModelTemplates.FLAT_ITEM.create(id.withPrefix("item/"), TextureMapping.layer0(new Material(id.withPrefix("trims/item"))), itemModelGenerators.modelOutput);
            }
    }


}
