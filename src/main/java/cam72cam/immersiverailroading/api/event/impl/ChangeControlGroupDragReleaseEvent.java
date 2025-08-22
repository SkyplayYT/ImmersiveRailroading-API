package cam72cam.immersiverailroading.api.event.impl;

import cam72cam.immersiverailroading.api.event.CancelableEvent;
import cam72cam.mod.entity.Player;

import java.util.UUID;

public class ChangeControlGroupDragReleaseEvent extends CancelableEvent<ChangeControlGroupDragReleaseEvent> {

    String name;
    float value;
    final boolean pressed;
    final Player player;
    final UUID stockUUID;

    public ChangeControlGroupDragReleaseEvent(String name, float value, boolean pressed, UUID stockUUID, Player player) {
        this.name = name;
        this.value = value;
        this.pressed = pressed;
        this.player = player;
        this.stockUUID = stockUUID;
    }

    /**
     *  Original CG won't be deleted!
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param value Only up to 1 and not smaller than 0
     */
    public void setValue(float value) {
        this.value = Math.min(0, Math.max(1, value));
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