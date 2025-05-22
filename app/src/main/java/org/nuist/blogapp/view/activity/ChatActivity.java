package org.nuist.blogapp.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.nuist.blogapp.R;
import org.nuist.blogapp.ViewModel.ChatViewModel;
import org.nuist.blogapp.ViewModel.UserViewModel;
import org.nuist.blogapp.view.adapter.ChatAdapter;
import org.nuist.blogapp.websocket.WsManager;

/**
 * ChatActivity 是一个用于实现聊天功能的 Activity。
 * 它通过 ViewModel 管理聊天消息，并通过 RecyclerView 显示聊天记录。
 */
public class ChatActivity extends AppCompatActivity {

    private ChatViewModel viewModel; // 聊天功能相关的 ViewModel
    private UserViewModel userViewModel; // 用户信息相关的 ViewModel
    private ChatAdapter chatAdapter; // 聊天消息的适配器
    private String receiverUserNumber; // 私聊目标的用户编号
    private String receiverUserName; // 私聊目标的用户编号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat); // 设置布局文件

        // 初始化 ViewModel
        viewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // 从 Intent 中获取私聊目标的用户编号
        receiverUserNumber = getIntent().getStringExtra("user_number");
        receiverUserName = getIntent().getStringExtra("userName");

        // 设置私聊对象的显示文本
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView text = findViewById(R.id.chat_one_people);
        text.setText("私聊对象: " + receiverUserName);

        // 获取输入框、发送按钮和 RecyclerView 的引用
        EditText inputEdit = findViewById(R.id.inputEdit);
        Button sendBtn = findViewById(R.id.sendBtn);
        RecyclerView chatRecyclerView = findViewById(R.id.chatRecyclerView);

        // 初始化聊天适配器并设置 RecyclerView
        chatAdapter = new ChatAdapter();
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        // 观察新消息的更新
        viewModel.getNewMessage().observe(this, message -> {
            chatAdapter.addMessage(message); // 添加新消息到适配器
            chatRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1); // 滚动到最新消息
        });

        // 发送按钮的点击事件
        sendBtn.setOnClickListener(v -> {
            String content = inputEdit.getText().toString().trim(); // 获取输入框内容
            if (!content.isEmpty()) {
                viewModel.sendPrivateMessage(content, receiverUserNumber); // 发送私聊消息
                inputEdit.setText(""); // 清空输入框
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WsManager.getInstance().close();
    }
}
