package org.nuist.blogapp.websocket;

import android.util.Log;

import org.nuist.blogapp.model.entity.WebSocketMessage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * EventDispatcher 类用于管理和分发 WebSocket 消息事件。
 * 它通过注册监听器并根据消息类型调用相应的方法来实现事件的分发。
 */
public class EventDispatcher {
    private static final String TAG = "EventDispatcher";
    // 单例实例
    private static final EventDispatcher instance = new EventDispatcher();

    /**
     * 获取 EventDispatcher 的单例实例。
     *
     * @return EventDispatcher 的单例实例
     */
    public static EventDispatcher getInstance() {
        return instance;
    }

    // 存储消息类型与订阅者列表的映射
    private final Map<String, List<Subscriber>> subscriberMap = new HashMap<>();

    /**
     * 注册一个监听器，扫描监听器类中所有带有 @OnMessage 注解的方法，
     * 并将这些方法注册为对应消息类型的处理函数。
     *
     * @param listener 要注册的监听器对象
     */
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

    /**
     * 注销一个监听器，移除所有与该监听器相关的订阅者。
     *
     * @param listener 要注销的监听器对象
     */
    public void unregister(Object listener) {
        for (List<Subscriber> subscribers : subscriberMap.values()) {
            subscribers.removeIf(subscriber -> subscriber.target == listener);
        }
    }

    /**
     * 根据消息类型分发消息，调用所有注册的对应消息类型的处理方法。
     *
     * @param message 要分发的 WebSocket 消息
     */
    public void dispatch(WebSocketMessage message) {
        List<Subscriber> subscribers = subscriberMap.get(message.getType());
        Log.d(TAG, "dispatch: "+message.getType());
        if (subscribers != null) {
            for (Subscriber s : subscribers) {
                try {
                    // 通过反射调用之前注册的 @OnMessage("chat") 对应的方法，并把 WebSocketMessage 对象作为参数传过去。
                    s.method.invoke(s.target, message);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Subscriber 类表示一个订阅者，包含目标对象和处理方法。
     */
    private static class Subscriber {
        Object target;  // 目标对象
        Method method;  // 处理方法

        /**
         * 构造一个订阅者。
         *
         * @param target 目标对象
         * @param method 处理方法
         */
        Subscriber(Object target, Method method) {
            this.target = target;
            this.method = method;
        }
    }
}
