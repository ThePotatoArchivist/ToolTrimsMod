package archives.tater.tooltrims.mixin.client;

import archives.tater.tooltrims.registry.ToolTrimsAttachments;
import archives.tater.tooltrims.client.ToolTrimsClient;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.objectweb.asm.Opcodes;

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

import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;

import static archives.tater.tooltrims.datagen.ClientTrimOverlayGenerator.trimmedId;
import static net.minecraft.util.Util.memoize;

@Mixin(ThrownTridentRenderer.class)
public class ThrownTridentRendererMixin {
    @Shadow
    @Final
    private TridentModel model;

    @Unique
    private Function<ArmorTrim, @Nullable TextureAtlasSprite> trimSpriteLookup = _ -> null;

    @SuppressWarnings("DataFlowIssue")
    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void setTrimSpriteLookup(EntityRendererProvider.Context context, CallbackInfo ci) {
        var tridentTrimAtlas = context.getAtlas(ToolTrimsClient.TRIDENT_TRIMS_ATLAS);
        trimSpriteLookup = memoize(trim -> {
            var pattern = ToolTrimsClient.TRIM_PATTERNS.joinEntries().get(trim.pattern().unwrapKey().orElseThrow().identifier());
            var material = ToolTrimsClient.TRIM_MATERIALS.joinEntries().get(trim.material().unwrapKey().orElseThrow().identifier());
            return pattern == null || material == null
                    ? null
                    : tridentTrimAtlas.getSprite(trimmedId("trident_entity_" + pattern.suffix() + "_" + material.suffix()));
        });
    }

    @Inject(
            method = "extractRenderState(Lnet/minecraft/world/entity/projectile/arrow/ThrownTrident;Lnet/minecraft/client/renderer/entity/state/ThrownTridentRenderState;F)V",
            at = @At("TAIL")
    )
    private void addTrimState(ThrownTrident entity, ThrownTridentRenderState state, float partialTicks, CallbackInfo ci) {
        state.setData(ToolTrimsClient.TRIDENT_TRIM, Optional.ofNullable(entity.getAttached(ToolTrimsAttachments.TRIDENT_TRIM)));
    }

    @Inject(
            method = "submit(Lnet/minecraft/client/renderer/entity/state/ThrownTridentRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/state/ThrownTridentRenderState;isFoil:Z", opcode = Opcodes.GETFIELD)
    )
    private void applyTrimTexture(ThrownTridentRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo ci) {
        var trim = state.getDataOrDefault(ToolTrimsClient.TRIDENT_TRIM, Optional.empty()).orElse(null);
        if (trim == null) return;
        var sprite = trimSpriteLookup.apply(trim);
        if (sprite == null) return;
        submitNodeCollector.order(1).submitModel(
                model,
                Unit.INSTANCE,
                poseStack,
                ToolTrimsClient.TRIDENT_TRIMS_RENDER_TYPE,
                state.lightCoords,
                OverlayTexture.NO_OVERLAY,
                -1,
                sprite,
                state.outlineColor,
                null
        );
    }
}
