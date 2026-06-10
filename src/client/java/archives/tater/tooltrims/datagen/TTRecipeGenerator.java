package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.registry.ToolTrimsTags;
import archives.tater.tooltrims.registry.ToolTrimsItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SmithingTrimRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class TTRecipeGenerator extends RecipeProvider {

    protected TTRecipeGenerator(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    public void offerToolTrimRecipe(Item template, Holder<TrimPattern> pattern, ResourceKey<Recipe<?>> key) {
        SmithingTrimRecipeBuilder.smithingTrim(
                        Ingredient.of(template), tag(ToolTrimsTags.TRIMMABLE_TOOLS), tag(ItemTags.TRIM_MATERIALS), pattern, RecipeCategory.MISC
                )
                .unlocks("has_smithing_trim_template", has(template))
                .save(output, key);
    }

    @Override
    public void buildRecipes() {
        Map<Item, Item> materials = Map.of(
                ToolTrimsItems.LINEAR_TEMPLATE, Items.TERRACOTTA,
                ToolTrimsItems.TRACKS_TEMPLATE, Items.COBBLESTONE,
                ToolTrimsItems.CHARGE_TEMPLATE, Items.COBBLED_DEEPSLATE,
                ToolTrimsItems.FROST_TEMPLATE, Items.SNOW_BLOCK
        );
        ToolTrimsItems.SMITHING_TEMPLATES.forEach((entry, templateItem) -> {
            offerToolTrimRecipe(templateItem, registries.lookupOrThrow(Registries.TRIM_PATTERN).getOrThrow(entry), ResourceKey.create(Registries.RECIPE, entry.identifier().withSuffix("_template_smithing_trim")));
            copySmithingTemplate(templateItem, materials.get(templateItem));
        });
    }

    public static class Provider extends FabricRecipeProvider {

        public Provider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new TTRecipeGenerator(registries, output);
        }

        @Override
        public String getName() {
            return "Tool Trims Recipes";
        }
    }
}
