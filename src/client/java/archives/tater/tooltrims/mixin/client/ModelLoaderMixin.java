package archives.tater.tooltrims.mixin.client;

import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ModelLoader.class, priority = 900)
public class ModelLoaderMixin {
    @Unique
    private static final Identifier CROSSBOW = Identifier.ofVanilla("item/crossbow");

    @Inject(method = "loadModelFromJson", at = @At("RETURN"), cancellable = true)
    private void preventCrossbowModification(Identifier id, CallbackInfoReturnable<JsonUnbakedModel> cir) {
        if (id.equals(CROSSBOW)) {
            cir.setReturnValue(cir.getReturnValue());
        }
    }
}
