package archives.tater.tooltrims.mixin;

import archives.tater.tooltrims.loot.CopyWithWeight;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.entry.EmptyEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.function.LootFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(EmptyEntry.class)
public abstract class EmptyEntryMixin extends LeafEntry implements CopyWithWeight<EmptyEntry> {
    protected EmptyEntryMixin(int weight, int quality, List<LootCondition> conditions, List<LootFunction> functions) {
        super(weight, quality, conditions, functions);
    }

    @Invoker("<init>")
    private static EmptyEntry newEmptyEntry(int weight, int quality, List<LootCondition> conditions, List<LootFunction> functions) {
        throw new AssertionError();
    }

    @Override
    public EmptyEntry tooltrims$copy(int weightChange) {
        return newEmptyEntry(weight + weightChange, quality, conditions, functions);
    }
}
