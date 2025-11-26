package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.ToolTrims;
import archives.tater.tooltrims.ToolTrimsPatterns;
import archives.tater.tooltrims.item.ToolTrimsItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements.Strategy;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.RecipeCraftedTrigger;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.armortrim.TrimMaterials;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class AdvancementGenerator extends FabricAdvancementProvider {
    public AdvancementGenerator(FabricDataOutput output, CompletableFuture<Provider> wrapperLookup) {
        super(output, wrapperLookup);
    }

    private static Advancement.Builder requireAllToolTrims(Advancement.Builder builder) {
        for (var pattern : ToolTrimsPatterns.PATTERNS) {
            var id = pattern.location();
            builder.addCriterion("tool_trimmed_" + id, RecipeCraftedTrigger.TriggerInstance.craftedItem(id.withSuffix("_tool_trim_smithing_template_smithing_trim")));
        }
        return builder;
    }

    private static Advancement.Builder createWithAllToolTrims() {
        return requireAllToolTrims(Advancement.Builder.recipeAdvancement());
    }

    @Override
    public void generateAdvancement(Provider wrapperLookup, Consumer<AdvancementHolder> consumer) {
        var shinyToolsIcon = new ItemStack(Items.NETHERITE_SWORD);
        shinyToolsIcon.set(DataComponents.TRIM, new ArmorTrim(
                wrapperLookup.lookupOrThrow(Registries.TRIM_MATERIAL).getOrThrow(TrimMaterials.DIAMOND),
                wrapperLookup.lookupOrThrow(Registries.TRIM_PATTERN).getOrThrow(ToolTrimsPatterns.FROST)
        ));

        var shinyTools = createWithAllToolTrims()
                .parent(new AdvancementHolder(ResourceLocation.withDefaultNamespace("adventure/root"), null)) // fake advancement
                .display(shinyToolsIcon,
                        Component.translatable("advancements.adventure.shiny_tools.title"),
                        Component.translatable("advancements.adventure.shiny_tools.description"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false)
                .requirements(Strategy.OR)
                .save(consumer, ToolTrims.id("adventure/shiny_tools").toString());

        createWithAllToolTrims()
                .parent(shinyTools)
                .display(
                        ToolTrimsItems.LINEAR_TOOL_TRIM_SMITHING_TEMPLATE,
                        Component.translatable("advancements.adventure.tools_of_all_styles.title"),
                        Component.translatable("advancements.adventure.tools_of_all_styles.description"),
                        null,
                        AdvancementType.CHALLENGE,
                        true,
                        true,
                        false)
                .requirements(Strategy.AND)
                .save(consumer, ToolTrims.id("adventure/tools_of_all_styles").toString());
    }
}
