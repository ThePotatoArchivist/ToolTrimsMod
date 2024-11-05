package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.ToolTrimsTags;
import archives.tater.tooltrims.item.ToolTrimsItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.data.recipe.SmithingTrimRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class TTRecipeGenerator extends RecipeGenerator {

    protected TTRecipeGenerator(WrapperLookup registries, RecipeExporter exporter) {
        super(registries, exporter);
    }

    public void offerToolTrimRecipe(Item template, RegistryKey<Recipe<?>> registryKey) {
        SmithingTrimRecipeJsonBuilder.create(
                        Ingredient.ofItems(template), ingredientFromTag(ToolTrimsTags.TRIMMABLE_TOOLS), ingredientFromTag(ToolTrimsTags.TOOL_TRIM_MATERIALS), RecipeCategory.MISC
                )
                .criterion("has_smithing_trim_template", conditionsFromItem(template))
                .offerTo(exporter, registryKey);
    }

    @Override
    public void generate() {
        Map<Item, Item> materials = Map.of(
                ToolTrimsItems.LINEAR_TOOL_TRIM_SMITHING_TEMPLATE, Items.TERRACOTTA,
                ToolTrimsItems.TRACKS_TOOL_TRIM_SMITHING_TEMPLATE, Items.COBBLESTONE,
                ToolTrimsItems.CHARGE_TOOL_TRIM_SMITHING_TEMPLATE, Items.COBBLED_DEEPSLATE,
                ToolTrimsItems.FROST_TOOL_TRIM_SMITHING_TEMPLATE, Items.SNOW_BLOCK
        );
        ToolTrimsItems.SMITHING_TEMPLATES.forEach((entry, templateItem) -> {
            offerToolTrimRecipe(templateItem, RegistryKey.of(RegistryKeys.RECIPE, entry.getValue().withSuffixedPath("_tool_trim_smithing_template_smithing_trim")));
            offerSmithingTemplateCopyingRecipe(templateItem, materials.get(templateItem));
        });
    }

    public static class Provider extends FabricRecipeProvider {

        public Provider(FabricDataOutput output, CompletableFuture<WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected RecipeGenerator getRecipeGenerator(WrapperLookup wrapperLookup, RecipeExporter exporter) {
            return new TTRecipeGenerator(wrapperLookup, exporter);
        }

        @Override
        public String getName() {
            return "Tool Trims Recipes";
        }
    }
}
