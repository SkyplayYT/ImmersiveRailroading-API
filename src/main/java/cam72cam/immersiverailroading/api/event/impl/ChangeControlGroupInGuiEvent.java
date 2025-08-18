package cam72cam.immersiverailroading.api.event.impl;

import cam72cam.immersiverailroading.api.event.CancelableEvent;
import cam72cam.mod.entity.Player;

import java.util.UUID;

public class ChangeControlGroupInGuiEvent extends CancelableEvent<ChangeControlGroupInGuiEvent> {

    final String name;
    float value;
    final Player player;
    final UUID stockUUID;

    public ChangeControlGroupInGuiEvent(String name, float value, UUID stockUUID, Player player) {
        this.name = name;
        this.value = value;
        this.player = player;
        this.stockUUID = stockUUID;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public float getValue() {
        return value;
    }

    public Player getPlayer() {
        return player;
    }

    public UUID getStockUUID() {
        return stockUUID;
    }
}
