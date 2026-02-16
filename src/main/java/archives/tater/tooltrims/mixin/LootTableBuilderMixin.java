package archives.tater.tooltrims.mixin;

import archives.tater.tooltrims.loot.ReplaceablePools;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ObjLongConsumer;

import static com.google.common.collect.Streams.mapWithIndex;

@Mixin(LootTable.Builder.class)
public class LootTableBuilderMixin implements ReplaceablePools {
    @Mutable
    @Shadow @Final private ImmutableList.Builder<LootPool> pools;

    @Override
    public void tooltrims$modifyPoolEntries(ObjLongConsumer<List<LootPoolEntryContainer>> modifier) {
        var modified = new ArrayList<>(mapWithIndex(pools.build().stream(), (pool, index) -> {
            var accessor = (LootPoolAccessor) pool;

            var entries = new ArrayList<>(accessor.tooltrims$getEntries());
            modifier.accept(entries, index);

            return LootPool.lootPool()
                    .setRolls(accessor.tooltrims$getRolls())
                    .setBonusRolls(accessor.tooltrims$getBonusRolls())
                    .add(entries)
                    .when(accessor.tooltrims$getConditions())
                    .apply(accessor.tooltrims$getFunctions())
                    .build();
        }).toList());
        pools = ImmutableList.builder();
        pools.addAll(modified);
    }
}
