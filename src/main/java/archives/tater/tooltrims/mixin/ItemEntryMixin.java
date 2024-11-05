package archives.tater.tooltrims.mixin;

import archives.tater.tooltrims.loot.CopyWithWeight;
import net.minecraft.item.Item;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(ItemEntry.class)
public abstract class ItemEntryMixin extends LeafEntry implements CopyWithWeight<ItemEntry> {
    @Shadow @Final
    private RegistryEntry<Item> item;

    protected ItemEntryMixin(int weight, int quality, List<LootCondition> conditions, List<LootFunction> functions) {
        super(weight, quality, conditions, functions);
    }

    @Invoker("<init>")
    private static ItemEntry newItemEntry(RegistryEntry<Item> item, int weight, int quality, List<LootCondition> conditions, List<LootFunction> functions) {
        throw new AssertionError();
    }

    @Override
    public ItemEntry tooltrims$copy(int weightChange) {
        return newItemEntry(item, weight + weightChange, quality, conditions, functions);
    }
}
