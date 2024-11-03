package archives.tater.tooltrims.mixin;

import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.provider.number.LootNumberProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(LootPool.class)
public interface LootPoolAccessor {
    @Accessor("rolls")
    LootNumberProvider tooltrims$getRolls();

    @Accessor("bonusRolls")
    LootNumberProvider tooltrims$getBonusRolls();

    @Accessor("entries")
    List<LootPoolEntry> tooltrims$getEntries();

    @Accessor("conditions")
    List<LootCondition> tooltrims$getConditions();

    @Accessor("functions")
    List<LootFunction> tooltrims$getFunctions();
}
