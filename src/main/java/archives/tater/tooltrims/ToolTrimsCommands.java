package archives.tater.tooltrims;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.ItemSlotArgumentType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.server.command.CommandManager.literal;

public class ToolTrimsCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("tooltrims")
                    .then(literal("upgrade")
                            .executes(ctx -> {
                                upgrade(ctx.getSource().getPlayer(), EquipmentSlot.MAINHAND.getOffsetEntitySlotId(98));
                                return 1;
                            })
                            .then(CommandManager.argument("slot", ItemSlotArgumentType.itemSlot()).executes(ctx -> {
                                upgrade(ctx.getSource().getPlayer(), ItemSlotArgumentType.getItemSlot(ctx, "slot"));
                                return 1;
                            })
                            .then(CommandManager.argument("target", EntityArgumentType.player()).executes(ctx -> {
                                upgrade(EntityArgumentType.getPlayer(ctx, "target"), ItemSlotArgumentType.getItemSlot(ctx, "slot"));
                                return 1;
                            }))))
            );
        });
    }

    public static void upgrade(@Nullable ServerPlayerEntity player, int slot) {
        if (player == null) throw new CommandException(Text.of("Must be executed by a player"));
        ToolTrimsDPCompat.upgradeItem(player.getStackReference(slot));
    }

}
