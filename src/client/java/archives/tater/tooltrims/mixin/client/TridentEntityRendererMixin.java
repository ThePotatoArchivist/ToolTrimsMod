package archives.tater.tooltrims.mixin.client;

import archives.tater.tooltrims.ToolTrimsDataAttachment;
import archives.tater.tooltrims.TridentTextures;
import archives.tater.tooltrims.duck.TrimmedState;

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

import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
@Mixin(ThrownTridentRenderer.class)
public class TridentEntityRendererMixin {
    @Inject(
            method = "extractRenderState(Lnet/minecraft/world/entity/projectile/arrow/ThrownTrident;Lnet/minecraft/client/renderer/entity/state/ThrownTridentRenderState;F)V",
            at = @At("TAIL")
    )
    private void addTrimState(ThrownTrident tridentEntity, ThrownTridentRenderState tridentEntityRenderState, float f, CallbackInfo ci) {
        ((TrimmedState) tridentEntityRenderState).tooltrims$setTrim(tridentEntity.getAttached(ToolTrimsDataAttachment.TRIDENT_TRIM));
    }

    @ModifyArg(
            method = "submit(Lnet/minecraft/client/renderer/entity/state/ThrownTridentRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/object/projectile/TridentModel;renderType(Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/renderer/rendertype/RenderType;"),
            index = 0
    )
    private Identifier applyTrimTexture(Identifier original, @Local(argsOnly = true) ThrownTridentRenderState tridentEntityRenderState) {
        return Objects.requireNonNullElse(TridentTextures.getTextureId(((TrimmedState) tridentEntityRenderState).tooltrims$getTrim()), original);
    }
}
