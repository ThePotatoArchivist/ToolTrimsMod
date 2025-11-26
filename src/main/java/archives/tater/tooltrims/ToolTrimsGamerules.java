package archives.tater.tooltrims;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameRules.BooleanValue;

public class ToolTrimsGamerules {
    public static final GameRules.Key<BooleanValue> DELETE_TOOLSMITHING_TABLES = GameRuleRegistry.register("tooltrims_deleteToolsmithingTable", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(false));

    public static void register() {}
}
