package cam72cam.immersiverailroading.gsmr;

import cam72cam.immersiverailroading.ConfigSound;
import cam72cam.immersiverailroading.ImmersiveRailroading;
import cam72cam.immersiverailroading.entity.EntityScriptableRollingStock;
import cam72cam.immersiverailroading.net.SoundPacket;
import cam72cam.immersiverailroading.script.modules.GSMRModule;
import cam72cam.immersiverailroading.util.BiMap;
import cam72cam.mod.entity.Player;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.resource.Identifier;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.Objects;
import java.util.UUID;

public class CallManager {

    static BiMap<Integer, Integer> connectedLines = new BiMap<>();

    public static CallErrorType call(UUID callingBeing, int phoneNumber) {
        //Only call if callingBeing has a phone number
        if(!PhoneNumberManager.hasBeingAPhoneNumber(callingBeing)) {
            return CallErrorType.CALLING_BEING_DOES_NOT_HAVE_A_PHONE_NUMBER;
        }

        int callingNumber = PhoneNumberManager.getPhoneNumber(callingBeing);

        //Does PhoneNumber exist?
        if(callingNumber == -1) {
            return CallErrorType.PHONE_NUMBER_DOES_NOT_EXIST;
        }

        //Is someone connected?
        if(connectedLines.containsValue(callingNumber) || connectedLines.containsKey(callingNumber)
                || connectedLines.containsValue(phoneNumber) || connectedLines.containsKey(phoneNumber)) {
            return CallErrorType.SOMEONE_IS_CONNECTED;
        }

        PhoneNumberLink calledPhoneLink = PhoneNumberManager.getPhoneNumberLink(phoneNumber);

        if(calledPhoneLink == null) {
            return CallErrorType.PHONE_LINK_NOT_FOUND;
        }

        //Call the player
        switch(calledPhoneLink.getCallType()) {
            case TRAIN:
                EntityScriptableRollingStock stock = TrainManager.getTrainInstance(calledPhoneLink.getUUID());

                if(stock == null) {
                    return CallErrorType.ROLLING_STOCK_NOT_FOUND;
                }

                GSMRModule.ringTrain(stock, callingNumber);
                break;
            case PLAYER:
                EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(calledPhoneLink.getUUID());
                SoundPacket packet = new SoundPacket(
                        new Identifier(ImmersiveRailroading.MODID, ConfigSound.SoundCategories.Telephone.telephone.getSoundFile()),
                        new Vec3d(player.getPositionVector()), Vec3d.ZERO,
                        1f, 1f,
                        500,
                        1,
                        SoundPacket.PacketSoundCategory.WHISTLE
                );

                packet.sendToPlayer(new Player(player));

                TextComponentString callMessage = new TextComponentString(GSMRText.CALL_MESSAGE.getText().replaceAll("%s", String.valueOf(callingNumber)));
                TextComponentString accept = new TextComponentString(GSMRText.ACCEPT.getText());
                TextComponentString reject = new TextComponentString(GSMRText.REJECT.getText());

                accept.setStyle(new Style()
                        .setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/call accept"))
                        .setColor(TextFormatting.GREEN)
                        .setBold(true));

                reject.setStyle(new Style()
                        .setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/call decline"))
                        .setColor(TextFormatting.RED)
                        .setBold(true));

                TextComponentString finalMessage = new TextComponentString("");

                finalMessage.appendSibling(callMessage);
                finalMessage.appendText(" ");
                finalMessage.appendSibling(accept);
                finalMessage.appendText(" | ");
                finalMessage.appendSibling(reject);

                player.sendMessage(finalMessage);

                break;
        }

        connectedLines.put(callingNumber, phoneNumber);
        return CallErrorType.NO_ERROR;
    }

    /**
     * This method connects calledBeing and callingBeing with VoiceChat
     * @param calledBeing The person who is being called
     */
    public static void acceptCall(UUID calledBeing) {
        int phoneNumberOfCalledBeing = PhoneNumberManager.getPhoneNumber(calledBeing);

        //Does phone number exists?
        if(phoneNumberOfCalledBeing == -1) {
            return;
        }

        int phoneNumberOfCallingBeing;

        if(connectedLines.containsKey(phoneNumberOfCalledBeing)) {
            phoneNumberOfCallingBeing = connectedLines.getValue(phoneNumberOfCalledBeing);
        } else if(connectedLines.containsValue(phoneNumberOfCalledBeing)) {
            phoneNumberOfCallingBeing = connectedLines.getKey(phoneNumberOfCalledBeing);
        } else {
            return;
        }

        //Does phone number exists?
        if(phoneNumberOfCallingBeing == -1) {
            return;
        }

        //ChangeUUID of calledBeing if it is a trainUUID
        PhoneNumberLink calledBeingLink = PhoneNumberManager.getPhoneNumberLink(phoneNumberOfCalledBeing);

        if(calledBeing == null) {
            if(connectedLines.containsValue(phoneNumberOfCalledBeing)) {
                connectedLines.removeValue(phoneNumberOfCalledBeing);
            } else if(connectedLines.containsKey(phoneNumberOfCalledBeing)) {
                connectedLines.removeKey(phoneNumberOfCalledBeing);
            }
            return;
        }

        //Change UUID if UUID is from train
        UUID finalCalledUUID;

        if(Objects.requireNonNull(calledBeingLink.getCallType()) == CallType.TRAIN) {
            UUID temp = TrainManager.getPlayer(calledBeingLink.getUUID());

            if(temp == null) {
                connectedLines.removeKey(phoneNumberOfCalledBeing);
                return;
            }

            GSMRModule.callAccepted(TrainManager.getTrainInstance(calledBeingLink.getUUID()));
            finalCalledUUID = temp;
        } else {
            finalCalledUUID = calledBeingLink.getUUID();
        }


        //Change UUID of callingBeing if it is a trainUUID
        PhoneNumberLink callingBeing = PhoneNumberManager.getPhoneNumberLink(phoneNumberOfCallingBeing);

        if(callingBeing == null) {
            if(connectedLines.containsValue(phoneNumberOfCalledBeing)) {
                connectedLines.removeValue(phoneNumberOfCalledBeing);
            } else if(connectedLines.containsKey(phoneNumberOfCalledBeing)) {
                connectedLines.removeKey(phoneNumberOfCalledBeing);
            }
            return;
        }

        UUID callingBeingUuid;

        if(Objects.requireNonNull(callingBeing.getCallType()) == CallType.TRAIN) {
            UUID temp = TrainManager.getPlayer(callingBeing.getUUID());

            if(temp == null) {
                connectedLines.removeKey(phoneNumberOfCalledBeing);
                return;
            }

            GSMRModule.callAccepted(TrainManager.getTrainInstance(callingBeing.getUUID()));
            callingBeingUuid = temp;
        } else {
            callingBeingUuid = callingBeing.getUUID();
        }

        GSMRVoicechatPlugin.connectPeople(finalCalledUUID, callingBeingUuid);
    }

    /**
     * Needs to be called careful. Only call it when you want to decline a call, not to hang up!
     * @param calledBeing The person who is being called
     */
    //TODO: Wenn im Anruf, darf das nicht ausgelöst werden
    public static void declineCall(UUID calledBeing) {
        int phoneNumberOfCalledBeing = PhoneNumberManager.getPhoneNumber(calledBeing);

        //Does phone number exists?
        if(phoneNumberOfCalledBeing == -1) {
            return;
        }

        int phoneNumberOfCallingBeing;

        if(connectedLines.containsKey(phoneNumberOfCalledBeing)) {
            phoneNumberOfCallingBeing = connectedLines.getValue(phoneNumberOfCalledBeing);
            connectedLines.removeKey(phoneNumberOfCalledBeing);
        } else if(connectedLines.containsValue(phoneNumberOfCalledBeing)) {
            phoneNumberOfCallingBeing = connectedLines.getKey(phoneNumberOfCalledBeing);
            connectedLines.removeValue(phoneNumberOfCalledBeing);
        } else {
            return;
        }

        //Does phone number exists?
        if(phoneNumberOfCallingBeing == -1) {
            return;
        }

        PhoneNumberLink callingBeing = PhoneNumberManager.getPhoneNumberLink(phoneNumberOfCallingBeing);

        //Inform callingBeing that calledBeing declined call
        switch(callingBeing.getCallType()) {
            case TRAIN:
                GSMRModule.callDeclined(TrainManager.getTrainInstance(callingBeing.getUUID()));
                break;
            case PLAYER:
                EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(callingBeing.getUUID());

                player.sendMessage(new TextComponentString("Player declined call."));
                break;
        }
    }

    public static void hangUp(UUID hangingUpPerson) {
        int hangingUpNumber = PhoneNumberManager.getPhoneNumber(hangingUpPerson);

        //Does he have a phone number?
        if(hangingUpNumber == -1) {
            return;
        }

        //Is he in a call?
        if(!connectedLines.containsValue(hangingUpNumber) && !connectedLines.containsKey(hangingUpNumber)) {
            return;
        }

        PhoneNumberLink hangingUpPersonLink = PhoneNumberManager.getPhoneNumberLink(hangingUpNumber);

        if(hangingUpPersonLink != null) {
            switch (hangingUpPersonLink.getCallType()) {
                case TRAIN:
                    GSMRModule.callEnded(TrainManager.getTrainInstance(hangingUpPersonLink.getUUID()));
                    GSMRVoicechatPlugin.disconnectPeople(TrainManager.getPlayer(hangingUpPersonLink.getUUID()));
                    break;
                case PLAYER:
                    GSMRVoicechatPlugin.disconnectPeople(hangingUpPersonLink.getUUID());
                    break;
            }
        }

        int otherNumber;

        if(connectedLines.containsKey(hangingUpNumber)) {
            otherNumber = connectedLines.getValue(hangingUpNumber);
            connectedLines.removeKey(hangingUpNumber);
        } else if(connectedLines.containsValue(hangingUpNumber)) {
            otherNumber = connectedLines.getKey(hangingUpNumber);
            connectedLines.removeValue(hangingUpNumber);
        } else {
            if(connectedLines.containsValue(hangingUpNumber)) {
                connectedLines.removeValue(hangingUpNumber);
            } else if(connectedLines.containsKey(hangingUpNumber)) {
                connectedLines.removeKey(hangingUpNumber);
            }
            return;
        }

        PhoneNumberLink otherPhoneLink = PhoneNumberManager.getPhoneNumberLink(otherNumber);

        if(otherPhoneLink == null) {
            return;
        }

        switch(otherPhoneLink.getCallType()) {
            case TRAIN:
                GSMRModule.callEnded(TrainManager.getTrainInstance(otherPhoneLink.getUUID()));
                GSMRVoicechatPlugin.disconnectPeople(TrainManager.getPlayer(otherPhoneLink.getUUID()));
                break;
            case PLAYER:
                GSMRVoicechatPlugin.disconnectPeople(otherPhoneLink.getUUID());
                break;
        }
    }

    public static boolean isBeingInCall(UUID being) {
        int phoneNumber = PhoneNumberManager.getPhoneNumber(being);

        if(phoneNumber == -1) {
            return false;
        }

        return connectedLines.containsKey(phoneNumber) || connectedLines.containsValue(phoneNumber);
    }
}
