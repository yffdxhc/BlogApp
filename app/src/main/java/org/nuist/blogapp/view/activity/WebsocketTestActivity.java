package org.nuist.blogapp.view.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.nuist.blogapp.R;
import org.nuist.blogapp.model.entity.WebSocketMessage;
import org.nuist.blogapp.websocket.ChatListener;
import org.nuist.blogapp.websocket.EventDispatcher;
import org.nuist.blogapp.websocket.WsManager;

public class WebsocketTestActivity extends AppCompatActivity {

    private EditText inputEdit;
    private Button sendBtn;
    private TextView chatLog;

    private final ChatListener chatListener = new ChatListener() {
        // 收到聊天消息
        @Override
        public void onChat(WebSocketMessage msg) {
            runOnUiThread(() -> chatLog.append("\n[服务器] " + msg.getData()));
        }

        // 连接成功
        @Override
        public void onOpen(WebSocketMessage msg) {
            runOnUiThread(() -> chatLog.append("\n[系统] WebSocket 连接成功"));
        }

        // 错误信息
        @Override
        public void onError(WebSocketMessage msg) {
            runOnUiThread(() -> chatLog.append("\n[错误] " + msg.getData()));
        }

        // 关闭信息
        @Override
        public void onClosed(WebSocketMessage msg) {
            runOnUiThread(() -> chatLog.append("\n[系统] 连接已关闭：" + msg.getData()));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_websocket_test);

        inputEdit = findViewById(R.id.inputEdit);
        sendBtn = findViewById(R.id.sendBtn);
        chatLog = findViewById(R.id.chatLog);

        // 注册监听器（自动扫描注解）
        EventDispatcher.getInstance().register(chatListener);

        // 建立 WebSocket 连接
        WsManager.getInstance().connect();

        sendBtn.setOnClickListener(v -> {
            String content = inputEdit.getText().toString();
            if (!content.isEmpty()) {
                WsManager.getInstance().sendMessage(content);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WsManager.getInstance().close(); // 关闭连接
        EventDispatcher.getInstance().unregister(chatListener); // 注销监听器
    }
}
