package org.nuist.blogapp.view.menu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import org.nuist.blogapp.MainActivity;
import org.nuist.blogapp.R;
import org.nuist.blogapp.databinding.FragmentMessageBinding;

public class MessageFragment extends Fragment{
    private static final String TAG ="MessageFragment";
    private FragmentMessageBinding binding;

    private ViewStub loginStub;
    private ViewStub loggedInStub;

    public MessageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMessageBinding.inflate(inflater,container,false);

        loginStub = binding.messageLoginStub.getViewStub();
        loggedInStub = binding.messageLoggedInStub.getViewStub();

        // 未登录默认布局
        showLoginLayout();

        if (MainActivity.loginStatus){
            showLoggedInLayout();
        }
        return binding.getRoot();
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