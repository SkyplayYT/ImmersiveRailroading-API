package cam72cam.immersiverailroading.gsmr;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PhoneNumberManager {

    // Phone Number | Information about person who is being called
    static Map<Integer, PhoneNumberLink> phoneBook = new HashMap<>();

    /**
     *
     * @param phoneNumber
     * @param uuid UUID of callable being
     * @param callType Type of callable being. (train or player)
     * @return False, if phone number or uuid already exists. Otherwise, it's true
     */
    public static boolean registerPhoneNumber(int phoneNumber, UUID uuid, CallType callType) {
        if(isPhoneNumberInUse(phoneNumber)) {
            return false;
        }

        if(hasBeingAPhoneNumber(uuid)) {
            return false;
        }

        phoneBook.put(phoneNumber, new PhoneNumberLink(callType, uuid));
        return true;
    }

    /**
     *
     * @param uuid
     * @return true, if phone number was removed
     */
    public static boolean unregisterPhoneNumber(UUID uuid) {
        for(int phoneNumber : phoneBook.keySet()) {
            if(phoneBook.get(phoneNumber).getUUID().equals(uuid)) {
                phoneBook.remove(phoneNumber);
                return true;
            }
        }

        return false;
    }

    /**
     * @param uuid of train or player
     * @return Is -1 if UUID is not linked to a phone number
     */
    public static int getPhoneNumber(UUID uuid) {
        for(int phoneNumber : phoneBook.keySet()) {
            if(phoneBook.get(phoneNumber).getUUID().equals(uuid)) {
                return phoneNumber;
            }
        }
        return -1;
    }

    /**
     * @param phoneNumber
     * @return null if phoneNumberLink does not exist!
     */
    @Nullable
    public static PhoneNumberLink getPhoneNumberLink(int phoneNumber) {
        return phoneBook.get(phoneNumber);
    }

    public static boolean isPhoneNumberInUse(int phoneNumber) {
        return phoneBook.containsKey(phoneNumber);
    }

    public static boolean hasBeingAPhoneNumber(UUID uuid) {
        for(int phoneNumber : phoneBook.keySet()) {
            if(phoneBook.get(phoneNumber).uuid.equals(uuid)) {
                return true;
            }
        }

        return false;
    }

}
