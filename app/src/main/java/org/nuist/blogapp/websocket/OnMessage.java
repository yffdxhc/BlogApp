package org.nuist.blogapp.websocket;

import java.lang.annotation.*;

// 保留到运行时，用于反射扫描
@Retention(RetentionPolicy.RUNTIME)
// 仅用于方法
@Target(ElementType.METHOD)
public @interface OnMessage {
    // 事件类型，比如 "chat"、"notice"
    String value();
}
