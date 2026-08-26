package cam72cam.immersiverailroading.gsmr.commands;

import cam72cam.immersiverailroading.gsmr.CallType;
import cam72cam.immersiverailroading.gsmr.PhoneNumberManager;
import cam72cam.mod.entity.Player;
import cam72cam.mod.text.Command;
import cam72cam.mod.text.PlayerMessage;
import net.minecraft.util.text.TextFormatting;

import java.util.Optional;
import java.util.function.Consumer;

public class RegisterCommand extends Command {
    @Override
    public String getPrefix() {
        return "register";
    }

    @Override
    public String getUsage() {
        return "/register <phone number>";
    }

    @Override
    public boolean execute(Consumer<PlayerMessage> sender, Optional<Player> optionalPlayer, String[] args) {
        if(!optionalPlayer.isPresent()) {
            System.out.println(TextFormatting.RED + "Only players can use this command!");
            return false;
        }

        Player player = optionalPlayer.get();

        if(args.length != 1) {
            player.sendMessage(PlayerMessage.direct("Usage: /register <phone number>"));
            return false;
        }

        try {
            int phoneNumber = Integer.parseInt(args[0]);

            if(!PhoneNumberManager.registerPhoneNumber(phoneNumber, player.getUUID(), CallType.PLAYER)) {
                //Only false if phone number is already in use or if player already has a phone number
                if(PhoneNumberManager.isPhoneNumberInUse(phoneNumber)) {
                    player.sendMessage(PlayerMessage.direct("§cPhone number is already in use!"));
                } else {
                    player.sendMessage(PlayerMessage.direct("§cYou already have a phone number!"));
                }
                return false;
            }

            player.sendMessage(PlayerMessage.direct("§aPhone number has been successfully registered!"));
        } catch (NumberFormatException e) {
            player.sendMessage(PlayerMessage.direct("§cInvalid phone number! Check if it is a integer!"));
        }

        return true;
    }
}
