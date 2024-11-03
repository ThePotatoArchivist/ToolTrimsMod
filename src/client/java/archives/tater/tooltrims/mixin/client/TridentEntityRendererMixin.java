package archives.tater.tooltrims.mixin.client;

import archives.tater.tooltrims.TridentTextures;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.entity.TridentEntityRenderer;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

@Mixin(TridentEntityRenderer.class)
public class TridentEntityRendererMixin {
    @ModifyReturnValue(
            method = "getTexture(Lnet/minecraft/entity/projectile/TridentEntity;)Lnet/minecraft/util/Identifier;",
            at = @At("RETURN")
    )
    private Identifier applyTrimTexture(Identifier original, @Local(argsOnly = true) TridentEntity tridentEntity) {
        return Objects.requireNonNullElse(TridentTextures.getTextureId(tridentEntity), original);
    }
}
