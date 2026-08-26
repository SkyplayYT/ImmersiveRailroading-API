package cam72cam.immersiverailroading.gsmr.commands;

import cam72cam.immersiverailroading.gsmr.PhoneNumberManager;
import cam72cam.mod.entity.Player;
import cam72cam.mod.text.Command;
import cam72cam.mod.text.PlayerMessage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextFormatting;

import java.util.Optional;
import java.util.function.Consumer;

public class UnRegisterCommand extends Command {
    @Override
    public String getPrefix() {
        return "unregister";
    }

    @Override
    public String getUsage() {
        return "/unregister";
    }

    @Override
    public boolean execute(Consumer<PlayerMessage> sender, Optional<Player> optionalPlayer, String[] args) {
        if(!optionalPlayer.isPresent()) {
            System.out.println("Only players can use this command!");
            return false;
        }

        Player player = optionalPlayer.get();

        if(args.length != 0) {
            player.sendMessage(PlayerMessage.direct(("Usage: /unregister")));
            return false;
        }

        if(PhoneNumberManager.unregisterPhoneNumber(((EntityPlayerMP) sender).getUniqueID())) {
            player.sendMessage(PlayerMessage.direct((TextFormatting.GREEN + "Phone number successfully unregistered!")));
        } else {
            player.sendMessage(PlayerMessage.direct((TextFormatting.RED + "You don't have a phone number to unregister!")));
        }

        return true;
    }
}
