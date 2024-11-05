package archives.tater.tooltrims;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.ItemSlotArgumentType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import org.jetbrains.annotations.Nullable;

import static net.minecraft.server.command.CommandManager.literal;

public class ToolTrimsCommands {

    private static final SimpleCommandExceptionType ERROR_NOPLAYER = new SimpleCommandExceptionType(Text.translatable("commands.tooltrims.error.noplayer"));
    private static final SimpleCommandExceptionType ERROR_MISSING_DATAPACK = new SimpleCommandExceptionType(Text.translatable("commands.tooltrims.error.demigrate.missing_datapack"));
    private static final DynamicCommandExceptionType MIGRATE_FAIL = new DynamicCommandExceptionType(itemText -> Text.translatable("commands.tooltrims.migrate.error.fail", itemText));
    private static final DynamicCommandExceptionType DEMIGRATE_FAIL = new DynamicCommandExceptionType(itemText -> Text.translatable("commands.tooltrims.demigrate.error.fail", itemText));

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(literal("tooltrims")
                    .then(literal("migrate")
                            .executes(ctx -> {
                                migrate(registryAccess, ctx, ctx.getSource().getPlayer(), EquipmentSlot.MAINHAND.getOffsetEntitySlotId(LivingEntity.EQUIPMENT_SLOT_ID), false);
                                return 1;
                            })
                            .then(CommandManager.argument("slot", ItemSlotArgumentType.itemSlot()).executes(ctx -> {
                                migrate(registryAccess, ctx, ctx.getSource().getPlayer(), ItemSlotArgumentType.getItemSlot(ctx, "slot"), false);
                                return 1;
                            })
                            .then(CommandManager.argument("target", EntityArgumentType.player()).executes(ctx -> {
                                migrate(registryAccess, ctx, EntityArgumentType.getPlayer(ctx, "target"), ItemSlotArgumentType.getItemSlot(ctx, "slot"), false);
                                return 1;
                            }))))
                    .then(literal("demigrate")
                            .executes(ctx -> {
                                migrate(registryAccess, ctx, ctx.getSource().getPlayer(), EquipmentSlot.MAINHAND.getOffsetEntitySlotId(LivingEntity.EQUIPMENT_SLOT_ID), true);
                                return 1;
                            })
                            .then(CommandManager.argument("slot", ItemSlotArgumentType.itemSlot()).executes(ctx -> {
                                migrate(registryAccess, ctx, ctx.getSource().getPlayer(), ItemSlotArgumentType.getItemSlot(ctx, "slot"), true);
                                return 1;
                            })
                            .then(CommandManager.argument("target", EntityArgumentType.player()).executes(ctx -> {
                                migrate(registryAccess, ctx, EntityArgumentType.getPlayer(ctx, "target"), ItemSlotArgumentType.getItemSlot(ctx, "slot"), true);
                                return 1;
                            }))))
        ));
    }

    public static void migrate(CommandRegistryAccess registries, CommandContext<ServerCommandSource> ctx, @Nullable ServerPlayerEntity player, int slot, boolean reverse) throws CommandSyntaxException {
        if (!ToolTrimsDPCompat.isDatapackRunning(ctx.getSource().getServer())) throw ERROR_MISSING_DATAPACK.create();
        if (player == null) throw ERROR_NOPLAYER.create();
        var stackReference = player.getStackReference(slot);
        var currentStack = stackReference.get();
        var newStack = reverse ? ToolTrimsDPCompat.demigrateItem(player.getWorld(), registries, currentStack) : ToolTrimsDPCompat.migrateItem(player.getWorld(), currentStack);
        if (newStack == null)
            throw (reverse ? DEMIGRATE_FAIL : MIGRATE_FAIL).create(currentStack.toHoverableText());
        stackReference.set(newStack);
        ctx.getSource().sendFeedback(() -> Text.translatable(reverse ? "commands.tooltrims.demigrate.success" : "commands.tooltrims.migrate.success", newStack.toHoverableText()), true);
    }
}
