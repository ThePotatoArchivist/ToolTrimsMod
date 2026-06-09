package archives.tater.tooltrims.mixin.client;

import archives.tater.tooltrims.ToolTrims;
import archives.tater.tooltrims.ToolTrimsDataAttachment;
import archives.tater.tooltrims.client.ToolTrimsClient;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.object.projectile.TridentModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownTridentRenderer;
import net.minecraft.client.renderer.entity.state.ThrownTridentRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.item.equipment.trim.ArmorTrim;

import java.util.Optional;
import java.util.function.Function;

import static net.minecraft.util.Util.memoize;

@Mixin(ThrownTridentRenderer.class)
public class ThrownTridentRendererMixin {
    @Shadow
    @Final
    private TridentModel model;

    @Unique
    private Function<ArmorTrim, TextureAtlasSprite> trimSpriteLookup = _ -> {
        throw new IllegalStateException("Trim sprite lookup not initialized yet");
    };

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void setTrimSpriteLookup(EntityRendererProvider.Context context, CallbackInfo ci) {
        var tridentTrimAtlas = context.getAtlas(ToolTrimsClient.TRIDENT_TRIMS_ATLAS);
        trimSpriteLookup = memoize(trim -> tridentTrimAtlas.getSprite(ToolTrims.id(
                "trims/tridents/trident_entity_" +
                ToolTrimsClient.TRIM_PATTERNS.joinEntries().get(trim.pattern().unwrapKey().orElseThrow().identifier()).suffix() +
                "_" +
                ToolTrimsClient.TRIM_MATERIALS.joinEntries().get(trim.material().unwrapKey().orElseThrow().identifier()).suffix()
        )));
    }

    @Inject(
            method = "extractRenderState(Lnet/minecraft/world/entity/projectile/arrow/ThrownTrident;Lnet/minecraft/client/renderer/entity/state/ThrownTridentRenderState;F)V",
            at = @At("TAIL")
    )
    private void addTrimState(ThrownTrident entity, ThrownTridentRenderState state, float partialTicks, CallbackInfo ci) {
        state.setData(ToolTrimsClient.TRIDENT_TRIM, Optional.ofNullable(entity.getAttached(ToolTrimsDataAttachment.TRIDENT_TRIM)));
    }

    @Inject(
            method = "submit(Lnet/minecraft/client/renderer/entity/state/ThrownTridentRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/state/ThrownTridentRenderState;isFoil:Z")
    )
    private void applyTrimTexture(ThrownTridentRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo ci) {
        var trim = state.getDataOrDefault(ToolTrimsClient.TRIDENT_TRIM, Optional.empty()).orElse(null);
        if (trim == null) return;
        submitNodeCollector.order(1).submitModel(
                model,
                Unit.INSTANCE,
                poseStack,
                ToolTrimsClient.TRIDENT_TRIMS_RENDER_TYPE,
                state.lightCoords,
                OverlayTexture.NO_OVERLAY,
                -1,
                trimSpriteLookup.apply(trim),
                state.outlineColor,
                null
        );
    }
}
