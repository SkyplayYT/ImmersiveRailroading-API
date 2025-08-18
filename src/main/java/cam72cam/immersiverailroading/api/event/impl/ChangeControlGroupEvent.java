package cam72cam.immersiverailroading.api.event.impl;

import cam72cam.immersiverailroading.api.event.CancelableEvent;
import cam72cam.mod.entity.Player;

import java.util.UUID;

public class ChangeControlGroupEvent extends CancelableEvent<ChangeControlGroupEvent> {

    final String name;
    float value;
    final boolean pressed;
    final Player player;
    final UUID stockUUID;

    public ChangeControlGroupEvent(String name, float value, boolean pressed, UUID stockUUID, Player player) {
        this.name = name;
        this.value = value;
        this.pressed = pressed;
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

    public boolean isPressed() {
        return pressed;
    }

    public Player getPlayer() {
        return player;
    }

    public UUID getStockUUID() {
        return stockUUID;
    }
}
