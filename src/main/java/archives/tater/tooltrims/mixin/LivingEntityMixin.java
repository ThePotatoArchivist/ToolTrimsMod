package archives.tater.tooltrims.mixin;

import archives.tater.tooltrims.ToolTrimsDPCompat;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Level;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @SuppressWarnings("ConstantValue")
    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void checkToolsmithingTable(CallbackInfo ci) {
        if (!((Object) this instanceof ArmorStand armorStand)) return;
        if (!((Object) level() instanceof ServerLevel level)) return;
        if (ToolTrimsDPCompat.shouldDeleteToolsmithingTable(level, armorStand)) {
            ToolTrimsDPCompat.deleteToolsmithingTable(armorStand);
        }
    }
}
