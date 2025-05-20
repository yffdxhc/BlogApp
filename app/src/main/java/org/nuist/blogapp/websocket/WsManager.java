package org.nuist.blogapp.websocket;

import android.util.Log;
import com.google.gson.Gson;

import org.nuist.blogapp.model.entity.WebSocketMessage;

import okhttp3.*;

import java.util.concurrent.TimeUnit;

public class WsManager {

    private static final String TAG = "WsManager";
    private static final String WS_URL = "ws://192.168.21.1:8080/ws/chat"; // 改成你实际服务器地址

    private OkHttpClient client;
    private WebSocket webSocket;
    private final Gson gson = new Gson();
    private final EventDispatcher dispatcher = EventDispatcher.getInstance();

    private static final WsManager instance = new WsManager();
    public static WsManager getInstance() { return instance; }

    private WsManager() {
        client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .build();
    }

    public void connect() {
        Request request = new Request.Builder().url(WS_URL).build();
        client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket ws, Response response) {
                webSocket = ws;
                Log.d(TAG, "连接成功");
                dispatcher.dispatch(new WebSocketMessage("open", "连接已建立"));
            }

            @Override
            public void onMessage(WebSocket ws, String text) {
                Log.d(TAG, "收到消息：" + text);
                WebSocketMessage msg = gson.fromJson(text, WebSocketMessage.class);
                dispatcher.dispatch(msg);
            }

            @Override
            public void onFailure(WebSocket ws, Throwable t, Response response) {
                Log.e(TAG, "连接失败", t);
                dispatcher.dispatch(new WebSocketMessage("error", t.getMessage()));
            }

            @Override
            public void onClosing(WebSocket ws, int code, String reason) {
                Log.d(TAG, "连接关闭中：" + reason);
                dispatcher.dispatch(new WebSocketMessage("closing", reason));
            }

            @Override
            public void onClosed(WebSocket ws, int code, String reason) {
                Log.d(TAG, "连接已关闭：" + reason);
                dispatcher.dispatch(new WebSocketMessage("closed", reason));
            }
        });
    }

    public void sendMessage(String data) {
        if (webSocket != null) {
            String json = gson.toJson(new WebSocketMessage("chat", data));
            webSocket.send(json);
        }
    }

    public void close() {
        if (webSocket != null) {
            webSocket.close(1000, "手动关闭");
        }
    }
}
