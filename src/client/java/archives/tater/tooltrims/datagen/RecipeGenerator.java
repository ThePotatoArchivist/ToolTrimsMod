package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.ToolTrimsTags;
import archives.tater.tooltrims.item.ToolTrimsItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SmithingTrimRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class RecipeGenerator extends FabricRecipeProvider {

    public RecipeGenerator(FabricDataOutput output, CompletableFuture<Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    public static void offerToolTrimRecipe(RecipeOutput exporter, Item template, ResourceLocation recipeId) {
        SmithingTrimRecipeBuilder.smithingTrim(
                        Ingredient.of(template), Ingredient.of(ToolTrimsTags.TRIMMABLE_TOOLS), Ingredient.of(ToolTrimsTags.TOOL_TRIM_MATERIALS), RecipeCategory.MISC
                )
                .unlocks("has_smithing_trim_template", has(template))
                .save(exporter, recipeId);
    }

    @Override
    public void buildRecipes(RecipeOutput exporter) {
        Map<Item, Item> materials = Map.of(
                ToolTrimsItems.LINEAR_TOOL_TRIM_SMITHING_TEMPLATE, Items.TERRACOTTA,
                ToolTrimsItems.TRACKS_TOOL_TRIM_SMITHING_TEMPLATE, Items.COBBLESTONE,
                ToolTrimsItems.CHARGE_TOOL_TRIM_SMITHING_TEMPLATE, Items.COBBLED_DEEPSLATE,
                ToolTrimsItems.FROST_TOOL_TRIM_SMITHING_TEMPLATE, Items.SNOW_BLOCK
        );
        ToolTrimsItems.SMITHING_TEMPLATES.forEach((entry, templateItem) -> {
            offerToolTrimRecipe(exporter, templateItem, entry.location().withSuffix("_tool_trim_smithing_template_smithing_trim"));
            copySmithingTemplate(exporter, templateItem, materials.get(templateItem));
        });
    }
}
