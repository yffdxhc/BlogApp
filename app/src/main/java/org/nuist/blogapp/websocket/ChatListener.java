package org.nuist.blogapp.websocket;

import android.util.Log;

import org.nuist.blogapp.model.entity.WebSocketMessage;

public abstract class ChatListener {

    @OnMessage("chat")
    public void onChat(WebSocketMessage msg) {
        Log.d("ChatListener", "收到聊天消息：" + msg.getData());
    }

    @OnMessage("open")
    public void onOpen(WebSocketMessage msg) {
        Log.d("ChatListener", "连接成功：" + msg.getData());
    }

    @OnMessage("error")
    public void onError(WebSocketMessage msg) {
        Log.e("ChatListener", "连接失败：" + msg.getData());
    }

    // 关闭信息
    public abstract void onClosed(WebSocketMessage msg);
}
