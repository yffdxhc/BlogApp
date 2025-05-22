package org.nuist.blogapp.view.menu;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.nuist.blogapp.R;
import org.nuist.blogapp.ViewModel.UserViewModel;
import org.nuist.blogapp.databinding.FragmentMessageBinding;
import org.nuist.blogapp.model.entity.User;
import org.nuist.blogapp.view.adapter.ChatObjectAdapter;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment {
    private static final String TAG = "MessageFragment";

    // ViewBinding 对象，用于访问布局中的控件
    private FragmentMessageBinding binding;

    // 用户登录状态的 ViewModel
    private UserViewModel userViewModel;

    // 登录前布局容器视图
    private View loginContainer;

    // 登录后布局容器视图
    private View loggedInContainer;
    private ChatObjectAdapter adapter;

    public MessageFragment() {
        // 必须的空构造函数
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化 ViewModel，用于获取 token 等用户状态信息
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 使用 ViewBinding 加载布局
        binding = FragmentMessageBinding.inflate(inflater, container, false);

        // 获取两个布局容器的引用
        loginContainer = binding.getRoot().findViewById(R.id.message_login_container);
        loggedInContainer = binding.getRoot().findViewById(R.id.message_logged_in_container);

        // 初始状态显示“未登录布局”
        showLoginLayout();

        // 监听 token 状态，实现登录/登出布局切换
        userViewModel.getToken().observe(getViewLifecycleOwner(), token -> {
            if (token != null && !token.isEmpty()) {
                showLoggedInLayout(); // 显示登录后界面
                setupClickListeners();
            } else {
                showLoginLayout();    // 显示登录前界面
            }
        });

        return binding.getRoot();
    }

    /**
     * 设置登录/已登录区域的点击事件
     */
    private void setupClickListeners() {
        List<User> chatObject = new ArrayList<>();
        User user = new User();
        user.setUsername("test");
        chatObject.add(user);
        adapter = new ChatObjectAdapter(chatObject);

        RecyclerView recyclerView = binding.getRoot().findViewById(R.id.chat_object_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        userViewModel.setAndGetChatObject().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                Log.d(TAG, "onChanged: " + users);
                adapter.setData(users);  // 直接用adapter提供的方法更新数据和刷新视图
            }
        });
    }



    /**
     * 显示登录前的布局
     */
    private void showLoginLayout() {
        if (loginContainer != null) loginContainer.setVisibility(View.VISIBLE);
        if (loggedInContainer != null) loggedInContainer.setVisibility(View.GONE);
    }

    /**
     * 显示登录后的布局
     */
    private void showLoggedInLayout() {
        if (loginContainer != null) loginContainer.setVisibility(View.GONE);
        if (loggedInContainer != null) loggedInContainer.setVisibility(View.VISIBLE);
    }
}
