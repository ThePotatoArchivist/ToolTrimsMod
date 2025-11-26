package archives.tater.tooltrims;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.SlotArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.commands.Commands.literal;

public class ToolTrimsCommands {

    private static final SimpleCommandExceptionType ERROR_NOPLAYER = new SimpleCommandExceptionType(Component.translatable("commands.tooltrims.error.noplayer"));
    private static final SimpleCommandExceptionType ERROR_MISSING_DATAPACK = new SimpleCommandExceptionType(Component.translatable("commands.tooltrims.error.demigrate.missing_datapack"));
    private static final DynamicCommandExceptionType MIGRATE_FAIL = new DynamicCommandExceptionType(itemText -> Component.translatable("commands.tooltrims.migrate.error.fail", itemText));
    private static final DynamicCommandExceptionType DEMIGRATE_FAIL = new DynamicCommandExceptionType(itemText -> Component.translatable("commands.tooltrims.demigrate.error.fail", itemText));

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(literal("tooltrims")
                    .then(literal("migrate")
                            .executes(ctx -> {
                                migrate(registryAccess, ctx, ctx.getSource().getPlayer(), EquipmentSlot.MAINHAND.getIndex(LivingEntity.EQUIPMENT_SLOT_OFFSET), false);
                                return 1;
                            })
                            .then(Commands.argument("slot", SlotArgument.slot()).executes(ctx -> {
                                migrate(registryAccess, ctx, ctx.getSource().getPlayer(), SlotArgument.getSlot(ctx, "slot"), false);
                                return 1;
                            })
                            .then(Commands.argument("target", EntityArgument.player()).executes(ctx -> {
                                migrate(registryAccess, ctx, EntityArgument.getPlayer(ctx, "target"), SlotArgument.getSlot(ctx, "slot"), false);
                                return 1;
                            }))))
                    .then(literal("demigrate")
                            .executes(ctx -> {
                                migrate(registryAccess, ctx, ctx.getSource().getPlayer(), EquipmentSlot.MAINHAND.getIndex(LivingEntity.EQUIPMENT_SLOT_OFFSET), true);
                                return 1;
                            })
                            .then(Commands.argument("slot", SlotArgument.slot()).executes(ctx -> {
                                migrate(registryAccess, ctx, ctx.getSource().getPlayer(), SlotArgument.getSlot(ctx, "slot"), true);
                                return 1;
                            })
                            .then(Commands.argument("target", EntityArgument.player()).executes(ctx -> {
                                migrate(registryAccess, ctx, EntityArgument.getPlayer(ctx, "target"), SlotArgument.getSlot(ctx, "slot"), true);
                                return 1;
                            }))))
        ));
    }

    public static void migrate(CommandBuildContext registries, CommandContext<CommandSourceStack> ctx, @Nullable ServerPlayer player, int slot, boolean reverse) throws CommandSyntaxException {
        if (!ToolTrimsDPCompat.isDatapackRunning(ctx.getSource().getServer())) throw ERROR_MISSING_DATAPACK.create();
        if (player == null) throw ERROR_NOPLAYER.create();
        var stackReference = player.getSlot(slot);
        var currentStack = stackReference.get();
        var newStack = reverse ? ToolTrimsDPCompat.demigrateItem(player.serverLevel(), registries, currentStack) : ToolTrimsDPCompat.migrateItem(player.level(), currentStack);
        if (newStack == null)
            throw (reverse ? DEMIGRATE_FAIL : MIGRATE_FAIL).create(currentStack.getDisplayName());
        stackReference.set(newStack);
        ctx.getSource().sendSuccess(() -> Component.translatable(reverse ? "commands.tooltrims.demigrate.success" : "commands.tooltrims.migrate.success", newStack.getDisplayName()), true);
    }
}
