package archives.tater.tooltrims.mixin.client;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ModelBakery.class, priority = 900)
public class ModelLoaderMixin {
    @Unique
    private static final ResourceLocation CROSSBOW = ResourceLocation.withDefaultNamespace("item/crossbow");

    @Inject(method = "loadBlockModel", at = @At("RETURN"), cancellable = true)
    private void preventCrossbowModification(ResourceLocation id, CallbackInfoReturnable<BlockModel> cir) {
        if (id.equals(CROSSBOW)) {
            cir.setReturnValue(cir.getReturnValue());
        }
    }
}
