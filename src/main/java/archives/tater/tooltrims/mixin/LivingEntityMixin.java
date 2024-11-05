package archives.tater.tooltrims.mixin;

import archives.tater.tooltrims.ToolTrimsDPCompat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @SuppressWarnings("ConstantValue")
    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void checkToolsmithingTable(CallbackInfo ci) {
        if (!((Object) this instanceof ArmorStandEntity armorStand)) return;
        if (ToolTrimsDPCompat.shouldDeleteToolsmithingTable(armorStand)) {
            ToolTrimsDPCompat.deleteToolsmithingTable(armorStand);
        }
    }
}
