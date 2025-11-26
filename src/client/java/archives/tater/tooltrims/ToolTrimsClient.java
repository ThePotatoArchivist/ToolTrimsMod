package archives.tater.tooltrims;

import net.fabricmc.api.ClientModInitializer;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.armortrim.TrimPattern;

import static archives.tater.tooltrims.ToolTrimsPatterns.TRIM_PATTERN_PREDICATE;

public class ToolTrimsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        ItemProperties.registerGeneric(TRIM_PATTERN_PREDICATE, (stack, world, entity, seed) -> {
            if (!stack.is(ToolTrimsTags.TRIMMABLE_TOOLS)) return Float.NEGATIVE_INFINITY;
            if (world == null) return 0.0F;
			var trim = stack.get(DataComponents.TRIM);
			if (trim == null) return 0.0F;
			return ToolTrimsPatterns.getModelIndex(((Holder.Reference<TrimPattern>) trim.pattern()).key());
        });
    }
}
