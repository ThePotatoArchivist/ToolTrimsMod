package archives.tater.tooltrims.mixin.client;

import archives.tater.tooltrims.TridentTextures;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;
import net.minecraft.client.renderer.entity.ThrownTridentRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.ThrownTrident;

@Mixin(ThrownTridentRenderer.class)
public class TridentEntityRendererMixin {
    @ModifyReturnValue(
            method = "getTextureLocation(Lnet/minecraft/world/entity/projectile/ThrownTrident;)Lnet/minecraft/resources/ResourceLocation;",
            at = @At("RETURN")
    )
    private ResourceLocation applyTrimTexture(ResourceLocation original, @Local(argsOnly = true) ThrownTrident tridentEntity) {
        return Objects.requireNonNullElse(TridentTextures.getTextureId(tridentEntity), original);
    }
}
