package archives.tater.tooltrims.client;

import archives.tater.tooltrims.ToolTrims;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.reloader.ResourceReloaderKeys;

import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperties;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;

public class ToolTrimsClient implements ClientModInitializer {
    public static final Identifier TRIM_OVERLAYS_ID = ToolTrims.id("trim_overlays");
    public static final TrimOverlayLoader TRIM_OVERLAYS = new TrimOverlayLoader();

    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        SelectItemModelProperties.ID_MAPPER.put(ToolTrims.id("trim_pattern"), TrimPatternProperty.TYPE);
        SpecialModelRenderers.ID_MAPPER.put(ToolTrims.id("trimmed_trident"), TrimmedTridentModelRenderer.Unbaked.CODEC);

        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloadListener(TRIM_OVERLAYS_ID, TRIM_OVERLAYS);
        ResourceLoader.get(PackType.CLIENT_RESOURCES).addListenerOrdering(TRIM_OVERLAYS_ID, ResourceReloaderKeys.Client.MODELS);
        ModelLoadingPlugin.register(TRIM_OVERLAYS);
    }
}
