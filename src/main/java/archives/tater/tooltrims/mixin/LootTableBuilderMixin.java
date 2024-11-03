package archives.tater.tooltrims.mixin;

import archives.tater.tooltrims.loot.ReplaceablePools;
import com.google.common.collect.ImmutableList;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.LootPoolEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ObjLongConsumer;

import static com.google.common.collect.Streams.mapWithIndex;

@Mixin(LootTable.Builder.class)
public class LootTableBuilderMixin implements ReplaceablePools {
    @Mutable
    @Shadow @Final private ImmutableList.Builder<LootPool> pools;

    @Override
    public void tooltrims$modifyPoolEntries(ObjLongConsumer<List<LootPoolEntry>> modifier) {
        var modified = new ArrayList<>(mapWithIndex(pools.build().stream(), (pool, index) -> {
            var accessor = (LootPoolAccessor) pool;

            var entries = new ArrayList<>(accessor.tooltrims$getEntries());
            modifier.accept(entries, index);

            return LootPool.builder()
                    .rolls(accessor.tooltrims$getRolls())
                    .bonusRolls(accessor.tooltrims$getBonusRolls())
                    .with(entries)
                    .conditionally(accessor.tooltrims$getConditions())
                    .apply(accessor.tooltrims$getFunctions())
                    .build();
        }).toList());
        pools = ImmutableList.builder();
        pools.addAll(modified);
    }
}
