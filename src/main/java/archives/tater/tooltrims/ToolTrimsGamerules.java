package archives.tater.tooltrims;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRuleType;
import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;

import org.jetbrains.annotations.NotNull;

public class ToolTrimsGamerules {
    public static final GameRule<@NotNull Boolean> DELETE_TOOLSMITHING_TABLES = Registry.register(
            BuiltInRegistries.GAME_RULE,
            ToolTrims.id("delete_toolsmithing_table"),
            new GameRule<>(
                    GameRuleCategory.MISC,
                    GameRuleType.BOOL,
                    BoolArgumentType.bool(),
                    GameRuleTypeVisitor::visitBoolean,
                    Codec.BOOL,
                    b -> b ? 1 : 0,
                    false,
                    FeatureFlagSet.of()
            )
    );

    public static void register() {}
}
