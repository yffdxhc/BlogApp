package org.nuist.blogapp.websocket;

import org.nuist.blogapp.model.entity.WebSocketMessage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class EventDispatcher {
    private static final EventDispatcher instance = new EventDispatcher();
    public static EventDispatcher getInstance() {
        return instance;
    }

    private final Map<String, List<Subscriber>> subscriberMap = new HashMap<>();

    public void register(Object listener) {
        for (Method method : listener.getClass().getDeclaredMethods()) {
            OnMessage annotation = method.getAnnotation(OnMessage.class);
            if (annotation != null) {
                String type = annotation.value();
                subscriberMap.computeIfAbsent(type, k -> new ArrayList<>())
                        .add(new Subscriber(listener, method));
            }
        }
    }

    public void unregister(Object listener) {
        for (List<Subscriber> subscribers : subscriberMap.values()) {
            subscribers.removeIf(subscriber -> subscriber.target == listener);
        }
    }

    public void dispatch(WebSocketMessage message) {
        List<Subscriber> subscribers = subscriberMap.get(message.getType());
        if (subscribers != null) {
            for (Subscriber s : subscribers) {
                try {
                    s.method.invoke(s.target, message);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class Subscriber {
        Object target;
        Method method;

        Subscriber(Object target, Method method) {
            this.target = target;
            this.method = method;
        }
    }
}
