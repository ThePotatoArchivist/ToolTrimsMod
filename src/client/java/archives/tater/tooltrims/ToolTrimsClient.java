package archives.tater.tooltrims;

import archives.tater.tooltrims.datagen.EnchancementModelGenerator;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.item.property.select.SelectProperties;

public class ToolTrimsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        SelectProperties.ID_MAPPER.put(ToolTrims.id("trim_pattern"), TrimPatternProperty.TYPE);
    }
}
