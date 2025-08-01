package archives.tater.tooltrims.mixin;

import archives.tater.tooltrims.ToolTrimsDataAttachment;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("UnstableApiUsage")
@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin extends PersistentProjectileEntity {
    protected TridentEntityMixin(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(
            method = {
                    "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;)V",
                    "<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)V",
                    "readCustomDataFromNbt"
            },
            at = @At("TAIL")
    )
    private void syncTrim(CallbackInfo ci) {
        if (getWorld().isClient) return;
        setAttached(ToolTrimsDataAttachment.TRIDENT_TRIM, getItemStack().get(DataComponentTypes.TRIM));
    }
}
