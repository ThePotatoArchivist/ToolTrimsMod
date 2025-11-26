package archives.tater.tooltrims.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Drowned.class)
public abstract class DrownedEntityMixin extends Zombie {
    public DrownedEntityMixin(EntityType<? extends Zombie> entityType, Level world) {
        super(entityType, world);
    }

    @ModifyArg(
            method = "performRangedAttack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/ThrownTrident;<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;)V"),
            index = 2
    )
    private ItemStack useHeldTridentTrims(ItemStack stack) {
        var mainHandStack = getMainHandItem();
        if (!mainHandStack.is(Items.TRIDENT)) return stack;
        stack.set(DataComponents.TRIM, mainHandStack.get(DataComponents.TRIM));
        return stack;
    }
}
