package archives.tater.tooltrims.mixin;

import archives.tater.tooltrims.ToolTrimsDPCompat;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorStand.class)
public abstract class ArmorStandEntityMixin extends LivingEntity {
    protected ArmorStandEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void checkToolsmithingTable(CallbackInfo ci) {
        var armorStand = (ArmorStand) (LivingEntity) this;
        if (ToolTrimsDPCompat.shouldDeleteToolsmithingTable(armorStand)) {
            ToolTrimsDPCompat.deleteToolsmithingTable(armorStand);
        }
    }
}
