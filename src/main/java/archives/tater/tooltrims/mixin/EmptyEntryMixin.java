package archives.tater.tooltrims.mixin;

import archives.tater.tooltrims.loot.CopyWithWeight;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

@Mixin(EmptyLootItem.class)
public abstract class EmptyEntryMixin extends LootPoolSingletonContainer implements CopyWithWeight<EmptyLootItem> {
    protected EmptyEntryMixin(int weight, int quality, List<LootItemCondition> conditions, List<LootItemFunction> functions) {
        super(weight, quality, conditions, functions);
    }

    @Invoker("<init>")
    private static EmptyLootItem newEmptyEntry(int weight, int quality, List<LootItemCondition> conditions, List<LootItemFunction> functions) {
        throw new AssertionError();
    }

    @Override
    public EmptyLootItem tooltrims$copy(int weightChange) {
        return newEmptyEntry(weight + weightChange, quality, conditions, functions);
    }
}
