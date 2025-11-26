package archives.tater.tooltrims.mixin;

import archives.tater.tooltrims.ToolTrimsDataAttachment;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("UnstableApiUsage")
@Mixin(ThrownTrident.class)
public abstract class TridentEntityMixin extends AbstractArrow {
    protected TridentEntityMixin(EntityType<? extends AbstractArrow> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(
            method = {
                    "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;)V",
                    "<init>(Lnet/minecraft/world/level/Level;DDDLnet/minecraft/world/item/ItemStack;)V",
                    "readAdditionalSaveData"
            },
            at = @At("TAIL")
    )
    private void syncTrim(CallbackInfo ci) {
        if (level().isClientSide()) return;
        var trim = getPickupItemStackOrigin().get(DataComponents.TRIM);
        if (trim != null)
            setAttached(ToolTrimsDataAttachment.TRIDENT_TRIM, trim);
    }
}
