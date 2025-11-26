package archives.tater.tooltrims.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static archives.tater.tooltrims.ToolTrimsDPCompat.shouldDeleteItem;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    @Shadow public abstract void setItem(ItemStack stack);

    public ItemEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(
            method = "<init>(Lnet/minecraft/world/level/Level;DDDLnet/minecraft/world/item/ItemStack;DDD)V",
            at = @At("TAIL")
    )
    private void checkDeletion(Level world, double x, double y, double z, ItemStack stack, double velocityX, double velocityY, double velocityZ, CallbackInfo ci) {
        if (!world.isClientSide && shouldDeleteItem(stack, world)) setItem(ItemStack.EMPTY);
    }
}
