package archives.tater.tooltrims.mixin.client;

import archives.tater.tooltrims.TridentTextures;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

@Mixin(BlockEntityWithoutLevelRenderer.class)
public class BuiltinModelItemRendererMixin {
    @ModifyArg(
            method = "renderByItem",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/TridentModel;renderType(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;"),
            index = 0
    )
    private ResourceLocation applyTridentTrimTexture(ResourceLocation original, @Local(argsOnly = true) ItemStack stack) {
        return Objects.requireNonNullElse(TridentTextures.getTextureId(stack), original);
    }
}
