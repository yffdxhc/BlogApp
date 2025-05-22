package org.nuist.blogapp.view.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.nuist.blogapp.R;
import org.nuist.blogapp.ViewModel.ChatViewModel;
import org.nuist.blogapp.ViewModel.UserViewModel;
import org.nuist.blogapp.view.adapter.ChatAdapter;

public class WebsocketTestActivity extends AppCompatActivity {

    private EditText inputEdit;
    private Button sendBtn;
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;

    private ChatViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_websocket_test);

        inputEdit = findViewById(R.id.inputEdit);
        sendBtn = findViewById(R.id.sendBtn);
        chatRecyclerView = findViewById(R.id.chatRecyclerView);

        chatAdapter = new ChatAdapter();
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        // 初始化 ViewModel（传入 ApplicationContext）
        viewModel = new ViewModelProvider(
                this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())
        ).get(ChatViewModel.class);

        // 观察新消息
        viewModel.getNewMessage().observe(this, message -> {
            chatAdapter.addMessage(message);
            chatRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
        });

        // 发送按钮
        sendBtn.setOnClickListener(v -> {
            String content = inputEdit.getText().toString().trim();
            if (!content.isEmpty()) {
                viewModel.sendMessage(content);
                inputEdit.setText("");
            }
        });
    }
}


