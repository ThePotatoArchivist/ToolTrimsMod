package archives.tater.tooltrims.item.loot_functions;

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
import java.util.Optional;

public class SetRandomTrimsFunction extends LootItemConditionalFunction {
    public static final MapCodec<SetRandomTrimsFunction> MAP_CODEC = RecordCodecBuilder.mapCodec((i) ->
            commonFields(i)
                    .and(WeightedList.codec(TrimPattern.CODEC).optionalFieldOf("patterns").forGetter((f) -> f.patterns))
                    .and(WeightedList.codec(TrimMaterial.CODEC).optionalFieldOf("materials").forGetter((f) -> f.materials))
                    .apply(i, SetRandomTrimsFunction::new));

    private final Optional<WeightedList<Holder<TrimPattern>>> patterns;
    private final Optional<WeightedList<Holder<TrimMaterial>>> materials;

    public SetRandomTrimsFunction(List<LootItemCondition> predicates, final Optional<WeightedList<Holder<TrimPattern>>> patterns, final Optional<WeightedList<Holder<TrimMaterial>>> materials) {
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
        boolean hasMaterials = materials.isPresent();
        boolean hasPatterns = patterns.isPresent();
        if (hasMaterials && hasPatterns) {
            patterns.get().getRandom(context.getRandom()).ifPresent(pattern ->
                    materials.get().getRandom(context.getRandom()).ifPresent(material ->
                            itemStack.set(DataComponents.TRIM, new ArmorTrim(material, pattern))));
        }
        return itemStack;
    }
}
