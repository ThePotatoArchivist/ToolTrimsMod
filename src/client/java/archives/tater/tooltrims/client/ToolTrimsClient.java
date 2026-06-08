package archives.tater.tooltrims.client;

import archives.tater.tooltrims.ToolTrims;
import archives.tater.tooltrims.client.data.ClientTrimMaterial;
import archives.tater.tooltrims.client.data.ClientTrimOverlay;
import archives.tater.tooltrims.client.data.ClientTrimPattern;
import archives.tater.tooltrims.client.data.models.item.TrimPatternProperty;
import archives.tater.tooltrims.client.data.models.item.TrimmedTridentModelRenderer;
import archives.tater.tooltrims.client.data.models.item.UnbakedTrimsModel;
import archives.tater.tooltrims.mixin.client.SpriteSourcesAccessor;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.reloader.ResourceReloaderKeys;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.renderer.item.ItemModels;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperties;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.client.resources.model.cuboid.CuboidModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;

import java.util.stream.Stream;

public class ToolTrimsClient implements ClientModInitializer {
    public static final Identifier TRIM_PATTERNS_ID = ToolTrims.id("trim_patterns");
    public static final ClientTrimPattern.Loader TRIM_PATTERNS = new ClientTrimPattern.Loader();
    public static final Identifier TRIM_MATERIALS_ID = ToolTrims.id("trim_materials");
    public static final ClientTrimMaterial.Loader TRIM_MATERIALS = new ClientTrimMaterial.Loader();
    public static final Identifier TRIM_OVERLAYS_ID = ToolTrims.id("trim_overlays");
    public static final ClientTrimOverlay.Loader TRIM_OVERLAYS = new ClientTrimOverlay.Loader();

    public static Stream<Pair<Identifier, CuboidModel>> getTrimModels() {
        return TRIM_OVERLAYS.trimModels().join().stream().flatMap(model ->
                TRIM_PATTERNS.entries().join().values().stream().flatMap(pattern ->
                        TRIM_MATERIALS.entries().join().values().stream().map(material -> {
                                var modelId = UnbakedTrimsModel.createModelId(model.basePath(), pattern, material);
                                return Pair.of(modelId, new CuboidModel(
                                        null,
                                        null,
                                        null,
                                        null,
                                        new TextureSlots.Data.Builder().addTexture(TextureSlot.LAYER0.getId(), new Material(modelId)).build(),
                                        model.parent()
                                ));
                            }
                        )
                )
        );
    }

    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        SelectItemModelProperties.ID_MAPPER.put(ToolTrims.id("trim_pattern"), TrimPatternProperty.TYPE);
        SpecialModelRenderers.ID_MAPPER.put(ToolTrims.id("trimmed_trident"), TrimmedTridentModelRenderer.Unbaked.CODEC);
        ItemModels.ID_MAPPER.put(ToolTrims.id("trims"), UnbakedTrimsModel.CODEC);
        SpriteSourcesAccessor.getID_MAPPER().put(ToolTrims.id("trim_permutations"), TrimPermutationsSpriteSource.CODEC);

        var clientResources = ResourceLoader.get(PackType.CLIENT_RESOURCES);
        clientResources.registerReloadListener(TRIM_PATTERNS_ID, TRIM_PATTERNS);
        clientResources.registerReloadListener(TRIM_MATERIALS_ID, TRIM_MATERIALS);
        clientResources.registerReloadListener(TRIM_OVERLAYS_ID, TRIM_OVERLAYS);
        clientResources.addListenerOrdering(TRIM_PATTERNS_ID, ResourceReloaderKeys.Client.MODELS);
        clientResources.addListenerOrdering(TRIM_MATERIALS_ID, ResourceReloaderKeys.Client.MODELS);
        clientResources.addListenerOrdering(TRIM_OVERLAYS_ID, ResourceReloaderKeys.Client.MODELS);
        clientResources.addListenerOrdering(TRIM_PATTERNS_ID, ResourceReloaderKeys.Client.TEXTURES);
        clientResources.addListenerOrdering(TRIM_MATERIALS_ID, ResourceReloaderKeys.Client.TEXTURES);
        clientResources.addListenerOrdering(TRIM_OVERLAYS_ID, ResourceReloaderKeys.Client.TEXTURES);
    }
}
