package archives.tater.tooltrims.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

@Mixin(LootPool.class)
public interface LootPoolAccessor {
    @Accessor("rolls")
    NumberProvider tooltrims$getRolls();

    @Accessor("bonusRolls")
    NumberProvider tooltrims$getBonusRolls();

    @Accessor("entries")
    List<LootPoolEntryContainer> tooltrims$getEntries();

    @Accessor("conditions")
    List<LootItemCondition> tooltrims$getConditions();

    @Accessor("functions")
    List<LootItemFunction> tooltrims$getFunctions();
}
