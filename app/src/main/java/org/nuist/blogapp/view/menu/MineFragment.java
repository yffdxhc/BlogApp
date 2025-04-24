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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.nuist.blogapp.MainActivity;
import org.nuist.blogapp.R;
import org.nuist.blogapp.ViewModel.UserViewModel;
import org.nuist.blogapp.databinding.FragmentMineBinding;
import org.nuist.blogapp.model.TokenManager;
import org.nuist.blogapp.view.activity.LoginActivity;

import java.util.Objects;

public class MineFragment extends Fragment {
    private final static String TAG ="MineFragment";
    private FragmentMineBinding binding;
    private UserViewModel userViewModel;
    private TokenManager tokenManager;

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
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        tokenManager = new TokenManager(requireActivity());
        Log.d(TAG, "onCreate: 观察token-"+tokenManager.getToken());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        binding = FragmentMineBinding.inflate(inflater,container,false);

        loginStub = binding.loginStub.getViewStub();
        loggedInStub = binding.loggedInStub.getViewStub();

        // 设置图片点击事件
        setLoginIconClickListener();
        // 未登录默认布局
        showLoginLayout();
        // 根据token更换布局
        userViewModel.getToken().observe(getActivity(), new Observer<String>(){
            @Override
            public void onChanged(String s) {
                if (s != null && !s.isEmpty()){
                    showLoggedInLayout();
                }else{
                    showLoginLayout();
                }
            }
        });

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
