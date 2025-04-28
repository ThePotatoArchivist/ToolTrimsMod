package archives.tater.tooltrims.mixin;

import archives.tater.tooltrims.CopyWithWeight;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.entry.EmptyEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.function.LootFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EmptyEntry.class)
public abstract class EmptyEntryMixin extends LeafEntry implements CopyWithWeight<EmptyEntry> {
    protected EmptyEntryMixin(int weight, int quality, LootCondition[] conditions, LootFunction[] functions) {
        super(weight, quality, conditions, functions);
    }

    @Invoker("<init>")
    static EmptyEntry newEmptyEntry(int weight, int quality, LootCondition[] conditions, LootFunction[] functions) {
        throw new AssertionError();
    }

    @Override
    public EmptyEntry tooltrims$copy(int weightChange) {
        return newEmptyEntry(weight + weightChange, quality, conditions, functions);
    }
}
