package archives.tater.tooltrims.mixin;

import archives.tater.tooltrims.ToolTrimsTags;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ArmorTrim.class)
public class ArmorTrimMixin {
	@WrapOperation(
			method = "getTrim",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isIn(Lnet/minecraft/registry/tag/TagKey;)Z")
	)
	private static boolean allowGetForTools(ItemStack instance, TagKey<Item> tag, Operation<Boolean> original) {
		return original.call(instance, tag) || instance.isIn(ToolTrimsTags.TRIMMABLE_TOOLS);
	}

	@WrapOperation(
			method = "apply",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isIn(Lnet/minecraft/registry/tag/TagKey;)Z")
	)
	private static boolean allowApplyForTools(ItemStack instance, TagKey<Item> tag, Operation<Boolean> original) {
		return original.call(instance, tag) || instance.isIn(ToolTrimsTags.TRIMMABLE_TOOLS);
	}
}
