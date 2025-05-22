package org.nuist.blogapp.websocket;

import android.util.Log;
import org.nuist.blogapp.model.TokenManager;
import org.nuist.blogapp.model.entity.WebSocketMessage;
import okhttp3.*;

import java.util.concurrent.TimeUnit;

public class WsManager {
    private static final String TAG = "WsManager";
    private final String wsUrl = "ws://192.168.21.1:8080/ws/chat?token=" + TokenManager.getInstance().getToken();

    private OkHttpClient client;
    private WebSocket webSocket;
    private final EventDispatcher dispatcher = EventDispatcher.getInstance();

    private static final WsManager instance = new WsManager();
    public static WsManager getInstance() { return instance; }

    private WsManager() {
        client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .build();
    }

    public void connect() {
        Request request = new Request.Builder()
                .url(wsUrl)
                .build();

        client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket ws, Response response) {
                webSocket = ws;
                Log.d(TAG, "连接成功");
            }

            @Override
            public void onMessage(WebSocket ws, String text) {
                Log.d(TAG, "收到消息: " + text);
                // 解析格式: [userId]: content
                String token = "server"; // 非必要，可以保留
                String type = "chat";
                String content = text;

                WebSocketMessage message = new WebSocketMessage(type, content, token);
                dispatcher.dispatch(message);
            }

            @Override
            public void onFailure(WebSocket ws, Throwable t, Response response) {
                Log.e(TAG, "连接失败", t);
            }

            @Override
            public void onClosing(WebSocket ws, int code, String reason) {
                Log.d(TAG, "连接关闭中：" + reason);
            }

            @Override
            public void onClosed(WebSocket ws, int code, String reason) {
                Log.d(TAG, "连接已关闭：" + reason);
            }
        });
    }

    public void sendMessage(String data) {
        if (webSocket != null) {
            webSocket.send(data); // 发送纯文本
        }
    }

    public void close() {
        if (webSocket != null) {
            webSocket.close(1000, "手动关闭");
        }
    }
}
