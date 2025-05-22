package org.nuist.blogapp.view.menu;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.nuist.blogapp.R;
import org.nuist.blogapp.ViewModel.UserViewModel;
import org.nuist.blogapp.databinding.FragmentMessageBinding;
import org.nuist.blogapp.model.entity.User;
import org.nuist.blogapp.view.adapter.ChatObjectAdapter;
import org.nuist.blogapp.websocket.EventDispatcher;
import org.nuist.blogapp.websocket.WsManager;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment {
    private static final String TAG = "MessageFragment";

    private FragmentMessageBinding binding;
    private UserViewModel userViewModel;

    private View loginContainer;
    private View loggedInContainer;

    private ChatObjectAdapter adapter;
    private List<User> chatObjectList;

    public MessageFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMessageBinding.inflate(inflater, container, false);

        loginContainer = binding.getRoot().findViewById(R.id.message_login_container);
        loggedInContainer = binding.getRoot().findViewById(R.id.message_logged_in_container);

        showLoginLayout(); // 初始状态显示未登录

        userViewModel.getToken().observe(getViewLifecycleOwner(), token -> {
            if (token != null && !token.isEmpty()) {
                showLoggedInLayout(); // 登录成功
                setupRecyclerView();  // 初始化聊天列表
            } else {
                showLoginLayout();
            }
        });

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = binding.getRoot().findViewById(R.id.chat_object_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        chatObjectList = new ArrayList<>();
        adapter = new ChatObjectAdapter(chatObjectList);
        recyclerView.setAdapter(adapter);

        userViewModel.setAndGetChatObject().observe(getViewLifecycleOwner(), users -> {
            Log.d(TAG, "onChanged: " + users);
            chatObjectList.clear();
            chatObjectList.addAll(users);
            adapter.notifyDataSetChanged();
        });
    }

    private void showLoginLayout() {
        if (loginContainer != null) loginContainer.setVisibility(View.VISIBLE);
        if (loggedInContainer != null) loggedInContainer.setVisibility(View.GONE);
    }

    private void showLoggedInLayout() {
        if (loginContainer != null) loginContainer.setVisibility(View.GONE);
        if (loggedInContainer != null) loggedInContainer.setVisibility(View.VISIBLE);
    }
}