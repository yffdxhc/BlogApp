package org.nuist.blogapp.view.menu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import org.nuist.blogapp.ViewModel.UserViewModel;
import org.nuist.blogapp.databinding.FragmentMessageBinding;

public class MessageFragment extends Fragment{
    private static final String TAG ="MessageFragment";
    private FragmentMessageBinding binding;
    private UserViewModel userViewModel;

    private ViewStub loginStub;
    private ViewStub loggedInStub;

    public MessageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMessageBinding.inflate(inflater,container,false);

        loginStub = binding.messageLoginStub.getViewStub();
        loggedInStub = binding.messageLoggedInStub.getViewStub();

        // 未登录默认布局
        showLoginLayout();

        setListener();
        return binding.getRoot();
    }

    /**
     * 监听事件
     */
    private void setListener(){
        userViewModel.getToken().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null && !s.isEmpty()){
                    showLoggedInLayout();
                }else{
                    showLoginLayout();
                }
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