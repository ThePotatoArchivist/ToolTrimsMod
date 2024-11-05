package archives.tater.tooltrims;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.equipment.trim.ArmorTrimPattern;
import net.minecraft.registry.entry.RegistryEntry;

import static archives.tater.tooltrims.ToolTrimsPatterns.TRIM_PATTERN_PREDICATE;

public class ToolTrimsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        ModelPredicateProviderRegistry.register(TRIM_PATTERN_PREDICATE, (stack, world, entity, seed) -> {
            if (!stack.isIn(ToolTrimsTags.TRIMMABLE_TOOLS)) return Float.NEGATIVE_INFINITY;
            if (world == null) return 0.0F;
			var trim = stack.get(DataComponentTypes.TRIM);
			if (trim == null) return 0.0F;
			return ToolTrimsPatterns.getModelIndex(((RegistryEntry.Reference<ArmorTrimPattern>) trim.pattern()).registryKey());
        });
    }
}
