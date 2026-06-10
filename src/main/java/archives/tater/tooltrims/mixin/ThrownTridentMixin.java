package archives.tater.tooltrims.mixin;

import archives.tater.tooltrims.registry.ToolTrimsAttachments;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.level.Level;

@Mixin(ThrownTrident.class)
public abstract class ThrownTridentMixin extends AbstractArrow {

    protected ThrownTridentMixin(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
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
        if (trim == null)
            removeAttached(ToolTrimsAttachments.TRIDENT_TRIM);
        else
            setAttached(ToolTrimsAttachments.TRIDENT_TRIM, trim);
    }
}
