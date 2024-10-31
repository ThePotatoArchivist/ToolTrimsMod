package archives.tater.tooltrims;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.BooleanRule;

public class ToolTrimsGamerules {
    public static GameRules.Key<BooleanRule> DELETE_LEGACY_ITEMS = GameRuleRegistry.register("deleteLegacyItems", GameRules.Category.MOBS, GameRuleFactory.createBooleanRule(false));

    public static void register() {}
}
