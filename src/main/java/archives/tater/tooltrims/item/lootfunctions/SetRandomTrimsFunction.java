package archives.tater.tooltrims.item.lootfunctions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;

public class SetRandomTrimsFunction extends LootItemConditionalFunction {
    public static final MapCodec<SetRandomTrimsFunction> MAP_CODEC = RecordCodecBuilder.mapCodec((i) ->
            commonFields(i)
                    .and(WeightedList.codec(TrimPattern.CODEC).fieldOf("patterns").forGetter((f) -> f.patterns))
                    .and(WeightedList.codec(TrimMaterial.CODEC).fieldOf("materials").forGetter((f) -> f.materials))
                    .apply(i, SetRandomTrimsFunction::new));

    private final WeightedList<Holder<TrimPattern>> patterns;
    private final WeightedList<Holder<TrimMaterial>> materials;

    public SetRandomTrimsFunction(List<LootItemCondition> predicates, final WeightedList<Holder<TrimPattern>> patterns, final WeightedList<Holder<TrimMaterial>> materials) {
        super(predicates);
        this.patterns = patterns;
        this.materials = materials;
    }

    @Override
    public MapCodec<? extends LootItemConditionalFunction> codec() {
        return MAP_CODEC;
    }

    @Override
    protected ItemStack run(ItemStack itemStack, LootContext context) {
        patterns.getRandom(context.getRandom()).ifPresent(pattern ->
                materials.getRandom(context.getRandom()).ifPresent(material ->
                        itemStack.set(DataComponents.TRIM, new ArmorTrim(material, pattern))));
        return itemStack;
    }
}
