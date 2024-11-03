package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.ToolTrimsTags;
import archives.tater.tooltrims.item.ToolTrimsItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.SmithingTrimRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;

import java.util.Map;

public class RecipeGenerator extends FabricRecipeProvider {

    public RecipeGenerator(FabricDataOutput output) {
        super(output);
    }

    public static void offerToolTrimRecipe(RecipeExporter exporter, Item template, Identifier recipeId) {
        SmithingTrimRecipeJsonBuilder.create(
                        Ingredient.ofItems(template), Ingredient.fromTag(ToolTrimsTags.TRIMMABLE_TOOLS), Ingredient.fromTag(ItemTags.TRIM_MATERIALS), RecipeCategory.MISC
                )
                .criterion("has_smithing_trim_template", conditionsFromItem(template))
                .offerTo(exporter, recipeId);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        Map<Item, Item> materials = Map.of(
                ToolTrimsItems.LINEAR_TOOL_TRIM_SMITHING_TEMPLATE, Items.TERRACOTTA,
                ToolTrimsItems.TRACKS_TOOL_TRIM_SMITHING_TEMPLATE, Items.COBBLESTONE,
                ToolTrimsItems.CHARGE_TOOL_TRIM_SMITHING_TEMPLATE, Items.COBBLED_DEEPSLATE,
                ToolTrimsItems.FROST_TOOL_TRIM_SMITHING_TEMPLATE, Items.SNOW_BLOCK
        );
        ToolTrimsItems.SMITHING_TEMPLATES.forEach((entry, templateItem) -> {
            offerToolTrimRecipe(exporter, templateItem, entry.getValue().withSuffixedPath("_tool_trim_smithing_template_smithing_trim"));
            offerSmithingTemplateCopyingRecipe(exporter, templateItem, materials.get(templateItem));
        });
    }
}
