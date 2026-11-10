package archives.tater.tooltrims.client;

import archives.tater.tooltrims.ToolTrims;
import archives.tater.tooltrims.client.atlas.SingleTrimPermutationsSpriteSource;
import archives.tater.tooltrims.client.atlas.TrimPermutationsSpriteSource;
import archives.tater.tooltrims.client.item.TexturedTridentModelRenderer;
import archives.tater.tooltrims.client.item.TrimPatternProperty;
import archives.tater.tooltrims.client.item.UnbakedTrimsModel;
import archives.tater.tooltrims.client.resource.ClientTrimLoader;
import archives.tater.tooltrims.mixin.client.SpriteSourcesAccessor;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.PreparableModelLoadingPlugin;
import net.fabricmc.fabric.api.client.rendering.v1.AtlasRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;

import net.minecraft.client.renderer.item.ItemModels;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperties;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.equipment.trim.ArmorTrim;

import java.util.Optional;

public class ToolTrimsClient implements ClientModInitializer {

    public static final Identifier TRIDENT_TRIMS_ATLAS = ToolTrims.id("trident_trims");
    public static final Identifier TRIDENT_TRIMS_SHEET = AtlasRegistry.generateTextureLocation(TRIDENT_TRIMS_ATLAS);
    public static final RenderType TRIDENT_TRIMS_RENDER_TYPE = RenderTypes.entityCutout(TRIDENT_TRIMS_SHEET);

    public static final RenderStateDataKey<Optional<ArmorTrim>> TRIDENT_TRIM = RenderStateDataKey.create(() -> "tooltrims:trident_trim");

    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        SelectItemModelProperties.ID_MAPPER.put(ToolTrims.id("trim_pattern"), TrimPatternProperty.TYPE);
        SpecialModelRenderers.ID_MAPPER.put(ToolTrims.id("trimmed_trident"), TexturedTridentModelRenderer.Unbaked.CODEC);
        ItemModels.ID_MAPPER.put(ToolTrims.id("trims"), UnbakedTrimsModel.CODEC);
        ItemModels.ID_MAPPER.put(ToolTrims.id("trident_trims"), TexturedTridentModelRenderer.UnbakedTrims.CODEC);
        SpriteSourcesAccessor.getID_MAPPER().put(ToolTrims.id("trim_permutations"), TrimPermutationsSpriteSource.CODEC);
        SpriteSourcesAccessor.getID_MAPPER().put(ToolTrims.id("single_trim_permutations"), SingleTrimPermutationsSpriteSource.CODEC);
        AtlasRegistry.register(new AtlasManager.AtlasConfig(TRIDENT_TRIMS_SHEET, TRIDENT_TRIMS_ATLAS, false));

        PreparableModelLoadingPlugin.register(new ClientTrimLoader(), (result, pluginContext) -> {
            pluginContext.modifyItemModelOnLoad().register((model, context) ->
                    result.modifyModel(model, context.itemId())
            );
        });
    }
}
