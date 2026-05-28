package cam72cam.immersiverailroading.api.event;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.InvalidParameterException;
import java.util.*;

public class ListenerManager {

    static Map<Class<? extends Event>, List<IRListener>> listeners = new HashMap<>();

    public static void registerListener(IRListener listener) {
        boolean annotationFound = false;

        for(Method method : listener.getClass().getMethods()) {
            if(method.isAnnotationPresent(IREventHandler.class)) {
                if(Modifier.isStatic(method.getModifiers())) {
                    System.err.println(new NoSuchMethodException("Method is not allowed to be static!").getMessage());
                    continue;
                }

                if(method.getParameterCount() != 1) {
                    if(method.getParameterCount() == 0) {
                        System.err.println(new InvalidParameterException("Event Parameter not found! Cannot assign Listener to Event!").getMessage());
                    } else {
                        System.err.println(new InvalidParameterException("Too many parameters! Only use one!").getMessage());
                    }
                    continue;
                }

                if(!Event.class.isAssignableFrom(method.getParameters()[0].getType())) {
                    System.err.println(new InvalidParameterException("Missing Event Parameter! (cam72cam.immersiverailroading.api.event.impl)").getMessage());
                    continue;
                }

                List<IRListener> listenerContainedMethod;

                if(listeners.containsKey(method.getParameters()[0].getType())) {
                    listenerContainedMethod = listeners.get(method.getParameters()[0].getType());
                } else {
                    listenerContainedMethod = new ArrayList<>();
                }
                listenerContainedMethod.add(listener);

                listeners.put((Class<? extends Event>) method.getParameters()[0].getType(), listenerContainedMethod);
                annotationFound = true;
                System.out.println("[ImmersiveRailroading] Successfully registered " + listener.getClass().getSimpleName());
            }
        }
        if(!annotationFound)
            System.err.println(new AnnotationMissingException("@IREventHandler not found to mark the Event runnable!").getMessage());
    }

}
