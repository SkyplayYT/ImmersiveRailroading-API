package cam72cam.immersiverailroading.api.event.impl;

import cam72cam.immersiverailroading.api.event.CancelableEvent;

import java.util.UUID;

public class ChangeLuaControlGroupEvent extends CancelableEvent<ChangeLuaControlGroupEvent> {

    String name;
    float value;
    final UUID stockUUID;

    public ChangeLuaControlGroupEvent(String name, float value, UUID stockUUID) {
        this.name = name;
        this.value = value;
        this.stockUUID = stockUUID;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     *
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

    public UUID getStockUUID() {
        return stockUUID;
    }
}
