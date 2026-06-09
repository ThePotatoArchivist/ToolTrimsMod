package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.ToolTrimsTags;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class ItemTagGenerator extends FabricTagsProvider.ItemTagsProvider {

    public ItemTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture, null);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        valueLookupBuilder(ToolTrimsTags.TRIMMABLE_TOOLS)
                .forceAddTag(ItemTags.SWORDS)
                .forceAddTag(ItemTags.PICKAXES)
                .forceAddTag(ItemTags.AXES)
                .forceAddTag(ItemTags.SHOVELS)
                .forceAddTag(ItemTags.HOES)
                .forceAddTag(ItemTags.SPEARS)
                .add(
                        Items.BOW,
                        Items.CROSSBOW,
                        Items.TRIDENT,
                        Items.MACE
                );
    }
}
