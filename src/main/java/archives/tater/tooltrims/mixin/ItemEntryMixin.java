package archives.tater.tooltrims.mixin;

import archives.tater.tooltrims.loot.CopyWithWeight;
import net.minecraft.item.Item;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.function.LootFunction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ItemEntry.class)
public abstract class ItemEntryMixin extends LeafEntry implements CopyWithWeight<ItemEntry> {
    @Shadow @Final Item item;

    protected ItemEntryMixin(int weight, int quality, LootCondition[] conditions, LootFunction[] functions) {
        super(weight, quality, conditions, functions);
    }

    @Invoker("<init>")
    static ItemEntry newItemEntry(Item item, int weight, int quality, LootCondition[] conditions, LootFunction[] functions) {
        throw new AssertionError();
    }

    @Override
    public ItemEntry tooltrims$copy(int weightChange) {
        return newItemEntry(item, weight + weightChange, quality, conditions, functions);
    }
}
