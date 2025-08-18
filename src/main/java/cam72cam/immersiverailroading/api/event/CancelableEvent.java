package cam72cam.immersiverailroading.api.event;

public class CancelableEvent<T> extends Event<T> {

    boolean isCanceled = false;

    public void setCancelState(boolean canceled) {
        isCanceled = canceled;
    }

    public boolean isCanceled() {
        return isCanceled;
    }
}
