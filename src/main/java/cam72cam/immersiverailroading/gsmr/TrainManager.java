package cam72cam.immersiverailroading.gsmr;

import cam72cam.immersiverailroading.api.event.IREventHandler;
import cam72cam.immersiverailroading.api.event.IRListener;
import cam72cam.immersiverailroading.api.event.impl.ChangeControlGroupClickEvent;
import cam72cam.immersiverailroading.api.event.impl.ChangeControlGroupDragReleaseEvent;
import cam72cam.immersiverailroading.entity.EntityScriptableRollingStock;
import cam72cam.immersiverailroading.util.BiMap;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TrainManager {

    public static Map<UUID, EntityScriptableRollingStock> link = new HashMap<>();

    public static void registerTrain(UUID uuid, EntityScriptableRollingStock stock) {
        link.put(uuid, stock);
    }

    public static void unregisterTrain(UUID uuid) {
        link.remove(uuid);
    }

    /**
     *
     * @param trainUUID UUID of train
     * @return Instance of Train ({@link EntityScriptableRollingStock})
     */
    public static EntityScriptableRollingStock getTrainInstance(UUID trainUUID) {
        return link.get(trainUUID);
    }

    //Train Player
    public static BiMap<UUID, UUID> trainPlayerMap = new BiMap<>();

    public static void addTrainPlayerLink(UUID trainUUID, UUID playerUUID) {
        if(trainPlayerMap.containsValue(playerUUID)) {
            trainPlayerMap.removeValue(playerUUID);
        }
        if(trainPlayerMap.containsKey(trainUUID)) {
            trainPlayerMap.removeKey(trainUUID);
        }

        trainPlayerMap.put(trainUUID, playerUUID);
    }

    public static void removeTrainPlayerLink(UUID trainUUID) {
        trainPlayerMap.removeValue(trainUUID);
    }

    public static UUID getPlayer(UUID trainUUID) {
        return trainPlayerMap.getValue(trainUUID);
    }

    public static UUID getTrain(UUID playerUUID) {
        return trainPlayerMap.getKey(playerUUID);
    }

    public static boolean isTrainLinked(UUID trainUUID) {
        return trainPlayerMap.containsKey(trainUUID);
    }

    public static class GSMRButtonListener implements IRListener {

        @IREventHandler
        public void onButtonPress(ChangeControlGroupDragReleaseEvent event) {
            if(!event.getName().equalsIgnoreCase("gsmrfuenf")) {
                return;
            }

            addTrainPlayerLink(event.getStockUUID(), event.getPlayer().getUUID());
        }

    }
}
