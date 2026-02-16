package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.ToolTrimsTags;
import archives.tater.tooltrims.item.ToolTrimsItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SmithingTrimRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class TTRecipeGenerator extends RecipeProvider {

    protected TTRecipeGenerator(net.minecraft.core.HolderLookup.Provider registries, RecipeOutput exporter) {
        super(registries, exporter);
    }

    public void offerToolTrimRecipe(Item template, Holder<TrimPattern> pattern, ResourceKey<Recipe<?>> registryKey) {
        SmithingTrimRecipeBuilder.smithingTrim(
                        Ingredient.of(template), tag(ToolTrimsTags.TRIMMABLE_TOOLS), tag(ToolTrimsTags.TOOL_TRIM_MATERIALS), pattern, RecipeCategory.MISC
                )
                .unlocks("has_smithing_trim_template", has(template))
                .save(output, registryKey);
    }

    @Override
    public void buildRecipes() {
        Map<Item, Item> materials = Map.of(
                ToolTrimsItems.LINEAR_TOOL_TRIM_SMITHING_TEMPLATE, Items.TERRACOTTA,
                ToolTrimsItems.TRACKS_TOOL_TRIM_SMITHING_TEMPLATE, Items.COBBLESTONE,
                ToolTrimsItems.CHARGE_TOOL_TRIM_SMITHING_TEMPLATE, Items.COBBLED_DEEPSLATE,
                ToolTrimsItems.FROST_TOOL_TRIM_SMITHING_TEMPLATE, Items.SNOW_BLOCK
        );
        ToolTrimsItems.SMITHING_TEMPLATES.forEach((entry, templateItem) -> {
            offerToolTrimRecipe(templateItem, registries.lookupOrThrow(Registries.TRIM_PATTERN).getOrThrow(entry), ResourceKey.create(Registries.RECIPE, entry.identifier().withSuffix("_tool_trim_smithing_template_smithing_trim")));
            copySmithingTemplate(templateItem, materials.get(templateItem));
        });
    }

    public static class Provider extends FabricRecipeProvider {

        public Provider(FabricPackOutput output, CompletableFuture<net.minecraft.core.HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected RecipeProvider createRecipeProvider(net.minecraft.core.HolderLookup.Provider wrapperLookup, RecipeOutput exporter) {
            return new TTRecipeGenerator(wrapperLookup, exporter);
        }

        @Override
        public String getName() {
            return "Tool Trims Recipes";
        }
    }
}
