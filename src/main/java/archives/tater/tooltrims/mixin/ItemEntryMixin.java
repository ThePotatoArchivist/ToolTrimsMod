package archives.tater.tooltrims.mixin;

import archives.tater.tooltrims.loot.CopyWithWeight;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

@Mixin(LootItem.class)
public abstract class ItemEntryMixin extends LootPoolSingletonContainer implements CopyWithWeight<LootItem> {
    @Shadow @Final
    private Holder<Item> item;

    protected ItemEntryMixin(int weight, int quality, List<LootItemCondition> conditions, List<LootItemFunction> functions) {
        super(weight, quality, conditions, functions);
    }

    @Invoker("<init>")
    private static LootItem newItemEntry(Holder<Item> item, int weight, int quality, List<LootItemCondition> conditions, List<LootItemFunction> functions) {
        throw new AssertionError();
    }

    @Override
    public LootItem tooltrims$copy(int weightChange) {
        return newItemEntry(item, weight + weightChange, quality, conditions, functions);
    }
}
