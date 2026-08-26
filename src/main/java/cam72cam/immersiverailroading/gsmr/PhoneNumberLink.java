package cam72cam.immersiverailroading.gsmr;

import java.util.UUID;

public class PhoneNumberLink {

    CallType callType;
    UUID uuid;

    public PhoneNumberLink(CallType callType, UUID uuid) {
        this.callType = callType;
        this.uuid = uuid;
    }

    public CallType getCallType() {
        return callType;
    }

    public UUID getUUID() {
        return uuid;
    }
}