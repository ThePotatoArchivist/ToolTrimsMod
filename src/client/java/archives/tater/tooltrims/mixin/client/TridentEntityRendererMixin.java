package archives.tater.tooltrims.mixin.client;

import archives.tater.tooltrims.ToolTrimsDataAttachment;
import archives.tater.tooltrims.TridentTextures;
import archives.tater.tooltrims.duck.TrimmedState;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.entity.TridentEntityRenderer;
import net.minecraft.client.render.entity.state.TridentEntityRenderState;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
@Mixin(TridentEntityRenderer.class)
public class TridentEntityRendererMixin {
    @Inject(
            method = "updateRenderState(Lnet/minecraft/entity/projectile/TridentEntity;Lnet/minecraft/client/render/entity/state/TridentEntityRenderState;F)V",
            at = @At("TAIL")
    )
    private void addTrimState(TridentEntity tridentEntity, TridentEntityRenderState tridentEntityRenderState, float f, CallbackInfo ci) {
        ((TrimmedState) tridentEntityRenderState).tooltrims$setTrim(tridentEntity.getAttached(ToolTrimsDataAttachment.TRIDENT_TRIM));
    }

    @ModifyArg(
            method = "render(Lnet/minecraft/client/render/entity/state/TridentEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/TridentEntityModel;getLayer(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;"),
            index = 0
    )
    private Identifier applyTrimTexture(Identifier original, @Local(argsOnly = true) TridentEntityRenderState tridentEntityRenderState) {
        return Objects.requireNonNullElse(TridentTextures.getTextureId(((TrimmedState) tridentEntityRenderState).tooltrims$getTrim()), original);
    }
}
