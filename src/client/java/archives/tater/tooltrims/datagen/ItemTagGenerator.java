package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.registry.ToolTrimsTags;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.references.ItemIds;
import net.minecraft.tags.ItemTags;

import java.util.concurrent.CompletableFuture;

public class ItemTagGenerator extends FabricTagsProvider.ItemTagsProvider {

    public ItemTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        builder(ToolTrimsTags.TRIMMABLE_TOOLS)
                .forceAddTag(ItemTags.SWORDS)
                .forceAddTag(ItemTags.PICKAXES)
                .forceAddTag(ItemTags.AXES)
                .forceAddTag(ItemTags.SHOVELS)
                .forceAddTag(ItemTags.HOES)
                .forceAddTag(ItemTags.SPEARS)
                .add(ItemIds.BOW)
                .add(ItemIds.CROSSBOW)
                .add(ItemIds.TRIDENT)
                .add(ItemIds.MACE);
    }
}
