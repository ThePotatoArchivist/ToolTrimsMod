package archives.tater.tooltrims;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.BooleanRule;

public class ToolTrimsGamerules {
    public static final GameRules.Key<BooleanRule> DELETE_TOOLSMITHING_TABLES = GameRuleRegistry.register("tooltrims_deleteToolsmithingTable", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(false));

    public static void register() {}
}
