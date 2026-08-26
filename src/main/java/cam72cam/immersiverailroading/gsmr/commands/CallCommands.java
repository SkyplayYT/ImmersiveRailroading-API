package cam72cam.immersiverailroading.gsmr.commands;

import cam72cam.immersiverailroading.gsmr.CallErrorType;
import cam72cam.immersiverailroading.gsmr.CallManager;
import cam72cam.immersiverailroading.gsmr.PhoneNumberManager;
import cam72cam.mod.entity.Player;
import cam72cam.mod.text.Command;
import cam72cam.mod.text.PlayerMessage;
import net.minecraft.util.text.TextFormatting;

import java.util.Optional;
import java.util.function.Consumer;

public class CallCommands extends Command {

    @Override
    public String getPrefix() {
        return "call";
    }

    @Override
    public String getUsage() {
        return "/call <number>/[<accept>/<decline>]";
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

        if(args.length != 1) {
            player.sendMessage(PlayerMessage.direct("Usage: /call <number>/[<accept>/<decline>]"));
            return false;
        }

        try {
            int phoneNumber = Integer.parseInt(args[0]);

            CallErrorType errorType = CallManager.call(player.getUUID(), phoneNumber);

            if(!errorType.equals(CallErrorType.NO_ERROR)) {
                player.sendMessage(PlayerMessage.direct(errorType.toString()));
                return false;
            }

            player.sendMessage(PlayerMessage.direct("§eCalling " + args[0] + "..."));
        } catch (NumberFormatException e) {
            int phoneNumber = PhoneNumberManager.getPhoneNumber(player.getUUID());

            if(phoneNumber == -1) {
                player.sendMessage(PlayerMessage.direct(TextFormatting.RED + "You don't have a phone number!"));
                return false;
            }

            if(!CallManager.isBeingInCall(player.getUUID())) {
                return false;
            }

            if(args[0].equalsIgnoreCase("accept")) {
                CallManager.acceptCall(player.getUUID());
            } else if(args[0].equalsIgnoreCase("decline")) {
                CallManager.declineCall(player.getUUID());
            } else {
                player.sendMessage(PlayerMessage.direct("Usage: /call <number>/[<accept>/<decline>]"));
            }
        }

        return true;
    }
}
