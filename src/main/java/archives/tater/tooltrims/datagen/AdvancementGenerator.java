package archives.tater.tooltrims.datagen;

import archives.tater.tooltrims.ToolTrims;
import archives.tater.tooltrims.ToolTrimsPatterns;
import archives.tater.tooltrims.item.ToolTrimsItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.AdvancementRequirements.CriterionMerger;
import net.minecraft.advancement.criterion.RecipeCraftedCriterion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.trim.ArmorTrimMaterials;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class AdvancementGenerator extends FabricAdvancementProvider {
    public AdvancementGenerator(FabricDataOutput output) {
        super(output);
    }

    private static Advancement.Builder requireAllToolTrims(Advancement.Builder builder) {
        for (var pattern : ToolTrimsPatterns.PATTERNS) {
            var id = pattern.getValue();
            builder.criterion("tool_trimmed_" + id, RecipeCraftedCriterion.Conditions.create(id.withSuffixedPath("_tool_trim_smithing_template_smithing_trim")));
        }
        return builder;
    }

    private static Advancement.Builder createWithAllToolTrims() {
        return requireAllToolTrims(Advancement.Builder.createUntelemetered());
    }

    @Override
    public void generateAdvancement(Consumer<AdvancementEntry> consumer) {
        var shinyToolsIcon = new ItemStack(Items.NETHERITE_SWORD);
        var shinyToolsIconTrim = new NbtCompound();
        shinyToolsIconTrim.putString("material", ArmorTrimMaterials.DIAMOND.getValue().toString());
        shinyToolsIconTrim.putString("pattern", ToolTrimsPatterns.FROST.getValue().toString());
        shinyToolsIcon.setSubNbt("Trim", shinyToolsIconTrim);

        var shinyTools = createWithAllToolTrims()
                .parent(new AdvancementEntry(new Identifier("adventure/root"), null)) // fake advancement
                .display(shinyToolsIcon,
                        Text.translatable("advancements.adventure.shiny_tools.title"),
                        Text.translatable("advancements.adventure.shiny_tools.description"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false)
                .criteriaMerger(CriterionMerger.OR)
                .build(consumer, ToolTrims.id("adventure/shiny_tools").toString());

        createWithAllToolTrims()
                .parent(shinyTools)
                .display(
                        ToolTrimsItems.LINEAR_TOOL_TRIM_SMITHING_TEMPLATE,
                        Text.translatable("advancements.adventure.tools_of_all_styles.title"),
                        Text.translatable("advancements.adventure.tools_of_all_styles.description"),
                        null,
                        AdvancementFrame.CHALLENGE,
                        true,
                        true,
                        false)
                .criteriaMerger(CriterionMerger.AND)
                .build(consumer, ToolTrims.id("adventure/tools_of_all_styles").toString());
    }
}
