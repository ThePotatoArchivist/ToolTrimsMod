package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.ToolTrimsTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class ItemTagGenerator extends FabricTagProvider.ItemTagProvider {

    public ItemTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture, null);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(ToolTrimsTags.TRIMMABLE_TOOLS)
                .forceAddTag(ItemTags.TOOLS)
                .forceAddTag(ItemTags.SWORDS)
                .add(Items.BOW)
                .add(Items.CROSSBOW);
    }
}
