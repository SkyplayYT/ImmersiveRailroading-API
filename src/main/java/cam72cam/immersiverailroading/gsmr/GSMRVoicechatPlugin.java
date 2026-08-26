package cam72cam.immersiverailroading.gsmr;

import de.maxhenkel.voicechat.api.*;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.LeaveGroupEvent;
import de.maxhenkel.voicechat.api.events.VoicechatServerStartedEvent;

import java.util.*;

@ForgeVoicechatPlugin
public class GSMRVoicechatPlugin implements VoicechatPlugin {

    static VoicechatServerApi api;
    static boolean shouldFailSafeBeTriggered = true;

    @Override
    public void initialize(VoicechatApi api) {
        VoicechatPlugin.super.initialize(api);
    }

    @Override
    public String getPluginId() {
        return "ir_gsmr";
    }

    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(VoicechatServerStartedEvent.class, this::onServerStarted);
        registration.registerEvent(LeaveGroupEvent.class, this::onPlayerLeaveGroup);
    }

    public void onServerStarted(VoicechatServerStartedEvent event) {
        api = event.getVoicechat();
    }

    public void onPlayerLeaveGroup(LeaveGroupEvent event) {
        if(event.getConnection() == null) {
            return;
        }

        if(!CallManager.isBeingInCall(event.getConnection().getPlayer().getUuid())) {
            return;
        }

        if(!shouldFailSafeBeTriggered) {
            shouldFailSafeBeTriggered = true;
            return;
        }

        CallManager.hangUp(event.getConnection().getPlayer().getUuid());
    }

    public static VoicechatApi getApi() {
        return api;
    }

    /**
     *
     * @param calledPersons Persons who shall be connected!
     */
    public static void connectPeople(UUID... calledPersons) {
        Group group = api.groupBuilder().setHidden(true).setName("Call").setType(Group.Type.OPEN).setId(UUID.randomUUID()).build();

        for(UUID callingPerson : calledPersons) {
            try {
                Objects.requireNonNull(api.getConnectionOf(callingPerson)).setGroup(group);
            } catch(NullPointerException e) {
                //Ignore
                System.out.println(callingPerson + " does not have VoiceChat installed or did not connect to VoiceChat Server\n" + e.getMessage());
            }
        }
    }

    /**
     *
     * @param calledPersons Persons who shall be disconnected from their voice chats!
     */
    public static void disconnectPeople(UUID... calledPersons) {
        for(UUID callingPerson : calledPersons) {
            VoicechatConnection connection = api.getConnectionOf(callingPerson);

            if(connection == null) {
                continue;
            }

            shouldFailSafeBeTriggered = false;
            connection.setGroup(null);
        }
        shouldFailSafeBeTriggered = true;
    }
}
