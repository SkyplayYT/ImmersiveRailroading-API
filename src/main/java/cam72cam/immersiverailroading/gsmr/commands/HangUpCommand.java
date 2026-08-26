package cam72cam.immersiverailroading.gsmr.commands;

import cam72cam.immersiverailroading.gsmr.CallManager;
import cam72cam.mod.entity.Player;
import cam72cam.mod.text.Command;
import cam72cam.mod.text.PlayerMessage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextFormatting;

import java.util.Optional;
import java.util.function.Consumer;

public class HangUpCommand extends Command {
    @Override
    public String getPrefix() {
        return "hangup";
    }

    @Override
    public String getUsage() {
        return "/hangup";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return PermissionLevel.LEVEL4;
    }

    @Override
    public boolean execute(Consumer<PlayerMessage> sender, Optional<Player> optionalPlayer, String[] args) {
        if(!optionalPlayer.isPresent()) {
            System.out.println("Only players can use this command!");
            return false;
        }

        Player player = optionalPlayer.get();

        if(args.length != 0) {
            player.sendMessage(PlayerMessage.direct("Usage: /hangup"));
            return false;
        }

        EntityPlayerMP entity = (EntityPlayerMP) sender;

        if(!CallManager.isBeingInCall(entity.getUniqueID())) {
            player.sendMessage(PlayerMessage.direct(TextFormatting.RED + "You are not in call!"));
            return false;
        }

        CallManager.hangUp(entity.getUniqueID());
        player.sendMessage(PlayerMessage.direct(TextFormatting.GREEN + "You have been unhanged!"));

        return true;
    }
}
