package archives.tater.tooltrims.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Debug(export = true)
@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @Shadow @Final private ItemModelShaper itemModelShaper;

    @Shadow @Final public static ModelResourceLocation TRIDENT_IN_HAND_MODEL;

    @ModifyExpressionValue(
            method = "getModel",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z", ordinal = 0)
    )
    private boolean useDefaultTridentModel(boolean original) {
        return false;
    }

    @ModifyExpressionValue(
            method = "render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z", ordinal = 0)
    )
    private boolean noOverrideTridentModel(boolean original) {
        return false;
    }

    @ModifyVariable(
            method = "render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V",
            at = @At("HEAD"),
            argsOnly = true
    )
    private BakedModel overrideTridentModel(BakedModel original, @Local(argsOnly = true)ItemStack stack, @Local(argsOnly = true) ItemDisplayContext renderMode) {
        if (renderMode == ItemDisplayContext.GUI || renderMode == ItemDisplayContext.GROUND || renderMode == ItemDisplayContext.FIXED) return original;
        if (!stack.is(Items.TRIDENT)) return original;
        return itemModelShaper.getModelManager().getModel(TRIDENT_IN_HAND_MODEL);
    }
}
