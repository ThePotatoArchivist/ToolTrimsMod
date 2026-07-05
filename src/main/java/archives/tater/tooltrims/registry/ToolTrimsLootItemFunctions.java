package archives.tater.tooltrims.registry;

import archives.tater.tooltrims.ToolTrims;
import archives.tater.tooltrims.item.loot_functions.SetRandomTrimsFunction;
import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimMaterials;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;

import java.util.List;
import java.util.Optional;

public class ToolTrimsLootItemFunctions {


    private static MapCodec<? extends LootItemFunction> register(String name, MapCodec<? extends LootItemFunction> mapCodec) {
        return Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, ToolTrims.id(name), mapCodec);
    }

    public static void init() {
        register("set_random_trimes", SetRandomTrimsFunction.MAP_CODEC);
        /*
            Code to check the functionality of the loot function.
            I'd delete it but
         */
//        LootTableEvents.MODIFY_DROPS.register((holder, lootContext, list) -> {
//            Registry<TrimPattern> patternRegistry = lootContext.getLevel().registryAccess().lookupOrThrow(Registries.TRIM_PATTERN);
//            Registry<TrimMaterial> materialRegistry = lootContext.getLevel().registryAccess().lookupOrThrow(Registries.TRIM_MATERIAL);
//            WeightedList<Holder<TrimPattern>> patterns = WeightedList.<Holder<TrimPattern>>builder()
//                    .add(patternRegistry.wrapAsHolder(patternRegistry.getValueOrThrow(ToolTrimsPatterns.FROST)), 2)
//                    .add(patternRegistry.wrapAsHolder(patternRegistry.getValueOrThrow(ToolTrimsPatterns.CHARGE)), 1)
//                    .build();
//            WeightedList<Holder<TrimMaterial>> materials = WeightedList.<Holder<TrimMaterial>>builder()
//                    .add(materialRegistry.wrapAsHolder(materialRegistry.getValueOrThrow(TrimMaterials.DIAMOND)), 1)
//                    .add(materialRegistry.wrapAsHolder(materialRegistry.getValueOrThrow(TrimMaterials.COPPER)), 10)
//                    .add(materialRegistry.wrapAsHolder(materialRegistry.getValueOrThrow(TrimMaterials.IRON)), 5)
//                    .build();
//            for (ItemStack itemStack : list) {
//                if (itemStack.is(ToolTrimsTags.TRIMMABLE_TOOLS) && itemStack.get(DataComponents.TRIM) == null) {
//                    SetRandomTrimsFunction function = new SetRandomTrimsFunction(List.of(), Optional.of(patterns), Optional.of(materials));
//                    function.apply(itemStack, lootContext);
//                }
//            }
//        });
    }
}
