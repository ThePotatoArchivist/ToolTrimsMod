package archives.tater.tooltrims.registry;

import archives.tater.tooltrims.ToolTrims;
import archives.tater.tooltrims.item.lootfunctions.SetRandomTrimsFunction;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;

public class ToolTrimsLootItemFunctions {


    private static MapCodec<? extends LootItemFunction> register(String name, MapCodec<? extends LootItemFunction> mapCodec) {
        return Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, ToolTrims.id(name), mapCodec);
    }

    public static void init() {
        register("set_random_trimes", SetRandomTrimsFunction.MAP_CODEC);
    }
}
