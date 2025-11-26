package archives.tater.tooltrims;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperties;
import net.minecraft.client.renderer.special.SpecialModelRenderers;

public class ToolTrimsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        SelectItemModelProperties.ID_MAPPER.put(ToolTrims.id("trim_pattern"), TrimPatternProperty.TYPE);
        SpecialModelRenderers.ID_MAPPER.put(ToolTrims.id("trimmed_trident"), TrimmedTridentModelRenderer.Unbaked.CODEC);
    }
}
