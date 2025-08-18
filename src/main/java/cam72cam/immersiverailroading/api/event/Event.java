package cam72cam.immersiverailroading.api.event;

import cam72cam.immersiverailroading.ImmersiveRailroading;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class Event<T> {

    public T call() {
        List<IRListener> dataList = ListenerManager.listeners.get(this.getClass());

        if(dataList == null)
            return (T) this;
        else {
            for(IRListener data : dataList) {
                for(Method method : data.getClass().getMethods()) {
                    if(method.isAnnotationPresent(IREventHandler.class)) {
                        if(method.getParameters()[0].getType().equals(this.getClass())) {
                            try {
                                method.invoke(data, this);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                ImmersiveRailroading.warn(e.getMessage());
                            }
                        }
                    }
                }
            }
        }

        return (T) this;
    }

}
