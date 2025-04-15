package org.nuist.blogapp.view.menu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.nuist.blogapp.MainActivity;
import org.nuist.blogapp.R;
import org.nuist.blogapp.databinding.FragmentMineBinding;
import org.nuist.blogapp.view.activity.LoginActivity;

public class MineFragment extends Fragment {
    private final static String TAG ="MineFragment";
    private FragmentMineBinding binding;

    // 未登录布局
    private ViewStub loginStub;
    // 已登录布局
    private ViewStub loggedInStub;

    public MineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMineBinding.inflate(inflater,container,false);

        loginStub = binding.loginStub.getViewStub();
        loggedInStub = binding.loggedInStub.getViewStub();

        // 设置图片点击事件
        setLoginIconClickListener();

        // 未登录默认布局
        showLoginLayout();
        // 处于登录状态时切换布局
        if (MainActivity.loginStatus){
            showLoggedInLayout();
        }

        return binding.getRoot();
    }

    /**
     * 设置登录图标点击事件
     */
    private void setLoginIconClickListener() {
        loginStub.setOnInflateListener(new ViewStub.OnInflateListener() {
            @Override
            public void onInflate(ViewStub stub, View inflated) {
                ImageView loginIcon = inflated.findViewById(R.id.login_icon);
                loginIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    /**
     * 显示登录前布局
     */
    private void showLoginLayout() {
        // 隐藏登录后布局
        if (loggedInStub.getVisibility() == View.VISIBLE) {
            loggedInStub.setVisibility(View.GONE);
        }
        // 显示登录前布局
        if (loginStub.getVisibility() != View.VISIBLE) {
            loginStub.setVisibility(View.VISIBLE);
        }
        // 强制展开 loginStub
        if (loginStub.getParent() != null) {
            loginStub.inflate();
        }
    }

    /**
     * 显示登录后布局
     */
    private void showLoggedInLayout() {
        // 隐藏登录前布局
        if (loginStub.getVisibility() == View.VISIBLE) {
            loginStub.setVisibility(View.GONE);
        }
        // 显示登录后布局
        if (loggedInStub.getVisibility() != View.VISIBLE) {
            loggedInStub.setVisibility(View.VISIBLE);
        }
    }
}
