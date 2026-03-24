package archives.tater.tooltrims.mixin.client;

import archives.tater.tooltrims.ToolTrimsDataAttachment;
import archives.tater.tooltrims.client.TridentTextures;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.renderer.entity.ThrownTridentRenderer;
import net.minecraft.client.renderer.entity.state.ThrownTridentRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;

import java.util.Optional;

import static java.util.Objects.requireNonNullElse;

@Mixin(ThrownTridentRenderer.class)
public class TridentEntityRendererMixin {
    @Inject(
            method = "extractRenderState(Lnet/minecraft/world/entity/projectile/arrow/ThrownTrident;Lnet/minecraft/client/renderer/entity/state/ThrownTridentRenderState;F)V",
            at = @At("TAIL")
    )
    private void addTrimState(ThrownTrident tridentEntity, ThrownTridentRenderState tridentEntityRenderState, float f, CallbackInfo ci) {
        tridentEntityRenderState.setData(TridentTextures.TRIDENT_TRIM, Optional.ofNullable(tridentEntity.getAttached(ToolTrimsDataAttachment.TRIDENT_TRIM)));
    }

    @ModifyArg(
            method = "submit(Lnet/minecraft/client/renderer/entity/state/ThrownTridentRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/OrderedSubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/resources/Identifier;IIILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"),
            index = 3
    )
    private Identifier applyTrimTexture(Identifier original, @Local(argsOnly = true) ThrownTridentRenderState tridentEntityRenderState) {
        return requireNonNullElse(TridentTextures.getTextureId(tridentEntityRenderState.getDataOrDefault(TridentTextures.TRIDENT_TRIM, Optional.empty()).orElse(null)), original);
    }
}
