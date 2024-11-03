package archives.tater.tooltrims;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.ItemSlotArgumentType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.server.command.CommandManager.literal;

public class ToolTrimsCommands {

    private static final SimpleCommandExceptionType UPGRADE_NOPLAYER = new SimpleCommandExceptionType(Text.translatable("commands.tooltrims.upgrade.error.noplayer"));
    private static final DynamicCommandExceptionType UPGRADE_FAIL = new DynamicCommandExceptionType(itemText -> Text.translatable("commands.tooltrims.upgrade.error.fail", itemText));

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(literal("tooltrims")
                    .then(literal("upgrade")
                            .executes(ctx -> {
                                upgrade(ctx, ctx.getSource().getPlayer(), EquipmentSlot.MAINHAND.getOffsetEntitySlotId(98));
                                return 1;
                            })
                            .then(CommandManager.argument("slot", ItemSlotArgumentType.itemSlot()).executes(ctx -> {
                                upgrade(ctx, ctx.getSource().getPlayer(), ItemSlotArgumentType.getItemSlot(ctx, "slot"));
                                return 1;
                            })
                            .then(CommandManager.argument("target", EntityArgumentType.player()).executes(ctx -> {
                                upgrade(ctx, EntityArgumentType.getPlayer(ctx, "target"), ItemSlotArgumentType.getItemSlot(ctx, "slot"));
                                return 1;
                            }))))
        ));
    }

    public static void upgrade(CommandContext<ServerCommandSource> ctx, @Nullable ServerPlayerEntity player, int slot) throws CommandSyntaxException {
        if (player == null) throw UPGRADE_NOPLAYER.create();
        var stackReference = player.getStackReference(slot);
        var currentStack = stackReference.get();
        var newStack = ToolTrimsDPCompat.upgradeItem(player.getWorld(), currentStack);
        if (newStack == null)
            throw UPGRADE_FAIL.create(currentStack.toHoverableText());
        stackReference.set(newStack);
        ctx.getSource().sendFeedback(() -> Text.translatable("commands.tooltrims.upgrade.success", newStack.toHoverableText()), true);
    }

}
