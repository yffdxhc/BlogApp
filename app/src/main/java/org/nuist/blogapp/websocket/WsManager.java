package org.nuist.blogapp.websocket;

import android.util.Log;

import com.google.gson.Gson;

import org.nuist.blogapp.model.TokenManager;
import org.nuist.blogapp.model.entity.WebSocketMessage;

import java.util.concurrent.TimeUnit;

import okhttp3.*;

public class WsManager {
    private static final String TAG = "WsManager";

    // WebSocket 连接地址（带 token）
    private final String wsUrl = "ws://192.168.21.1:8080/ws/chat?token=" + TokenManager.getInstance().getToken();

    private OkHttpClient client;
    private WebSocket webSocket;
    private final EventDispatcher dispatcher = EventDispatcher.getInstance();
    private final Gson gson = new Gson(); // 用于解析 JSON 消息

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

                try {
                    // 提取 JSON 部分
                    String jsonStr;
                    int index = text.indexOf("]: ");
                    if (index != -1) {
                        jsonStr = text.substring(index + 3); // 去掉前缀
                    } else {
                        jsonStr = text; // 如果没有前缀，直接当 JSON
                    }

                    // 解析为 WebSocketMessage 对象
                    WebSocketMessage message = gson.fromJson(jsonStr, WebSocketMessage.class);
                    Log.d(TAG, "收到消息: " + jsonStr);
                    dispatcher.dispatch(message);
                } catch (Exception e) {
                    Log.e(TAG, "解析消息失败: " + text, e);
                }
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

    // 发送 JSON 格式的消息
    public void sendMessage(WebSocketMessage message) {
        if (webSocket != null) {
            String json = gson.toJson(message);
            webSocket.send(json);
        }
    }

    public void close() {
        if (webSocket != null) {
            webSocket.close(1000, "手动关闭");
        }
    }
}
