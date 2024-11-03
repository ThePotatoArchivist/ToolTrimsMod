package archives.tater.tooltrims.mixin;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(DrownedEntity.class)
public abstract class DrownedEntityMixin extends ZombieEntity {
    public DrownedEntityMixin(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyArg(
            method = "shootAt",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/TridentEntity;<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;)V"),
            index = 2
    )
    private ItemStack useHeldTridentTrims(ItemStack stack) {
        var mainHandStack = getMainHandStack();
        if (!mainHandStack.isOf(Items.TRIDENT)) return stack;
        stack.set(DataComponentTypes.TRIM, mainHandStack.get(DataComponentTypes.TRIM));
        return stack;
    }
}
