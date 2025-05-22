package org.nuist.blogapp.view.menu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;

import org.nuist.blogapp.R;
import org.nuist.blogapp.ViewModel.UserViewModel;
import org.nuist.blogapp.databinding.FragmentMineBinding;
import org.nuist.blogapp.model.RetrofitClient;
import org.nuist.blogapp.model.entity.User;
import org.nuist.blogapp.view.activity.LoginActivity;
import org.nuist.blogapp.view.activity.WebsocketTestActivity;
import org.nuist.blogapp.view.adapter.ChatAdapter;

public class MineFragment extends Fragment {

    private static final String TAG = "MineFragment";

    private FragmentMineBinding binding;
    private UserViewModel userViewModel;

    private View loginContainer;     // 未登录容器视图
    private View loggedInContainer;  // 已登录容器视图

    public MineFragment() {
        // 空构造函数必须保留
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMineBinding.inflate(inflater, container, false);

        // 获取容器视图（通过 ID 找到 include 的父容器）
        loginContainer = binding.getRoot().findViewById(R.id.mine_login_container);
        loggedInContainer = binding.getRoot().findViewById(R.id.mine_logged_in_container);

        // 设置点击事件
        setupClickListeners();

        // 默认显示未登录布局
        showLoginLayout();

        // 监听 token 状态变化并切换布局
        userViewModel.getToken().observe(getViewLifecycleOwner(), token -> {
            Log.d(TAG, "观察 token: " + token);
            if (token != null && !token.isEmpty()) {
                showLoggedInLayout();
            } else {
                showLoginLayout();
            }
        });

        return binding.getRoot();
    }

    /**
     * 设置登录/已登录区域的点击事件
     */
    private void setupClickListeners() {
        // 登录图标点击 -> 跳转登录界面
        ImageView loginIcon = binding.getRoot().findViewById(R.id.login_icon);
        if (loginIcon != null) {
            loginIcon.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            });
        }

        // 用户头像点击 -> 跳转 Websocket 测试页
        ImageView userProfileIcon = binding.getRoot().findViewById(R.id.user_profile_icon);
        if (userProfileIcon != null) {
            userProfileIcon.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), WebsocketTestActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            });
        }

        // 用户头像、名称信息绑定
        userViewModel.setAndGetUserInfo().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    assert userProfileIcon != null;
                    Glide.with(requireContext())
                            .load(RetrofitClient.getBaseUrl()+user.getAvatar())
                            .signature(new ObjectKey(String.valueOf(System.currentTimeMillis())))
                            .into(userProfileIcon);
                    TextView userName =binding.getRoot().findViewById(R.id.user_name);
                    if (userName != null) userName.setText(user.getUsername());
                }
            }
        });
    }


    /**
     * 显示登录前布局
     */
    private void showLoginLayout() {
        if (loginContainer != null) loginContainer.setVisibility(View.VISIBLE);
        if (loggedInContainer != null) loggedInContainer.setVisibility(View.GONE);
    }

    /**
     * 显示登录后布局
     */
    private void showLoggedInLayout() {
        if (loginContainer != null) loginContainer.setVisibility(View.GONE);
        if (loggedInContainer != null) loggedInContainer.setVisibility(View.VISIBLE);
    }
}
