package archives.tater.tooltrims.mixin.client;

import archives.tater.tooltrims.TridentTextures;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Objects;

@Mixin(BuiltinModelItemRenderer.class)
public class BuiltinModelItemRendererMixin {
    @ModifyArg(
            method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/TridentEntityModel;getLayer(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;"),
            index = 0
    )
    private Identifier applyTridentTrimTexture(Identifier original, @Local(argsOnly = true) ItemStack stack) {
        var networkHandler = MinecraftClient.getInstance().getNetworkHandler();
        if (networkHandler == null) return original;
        return Objects.requireNonNullElse(TridentTextures.getTextureId(networkHandler.getRegistryManager(), stack), original);
    }
}
