package org.nuist.blogapp.view.menu;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.nuist.blogapp.R;
import org.nuist.blogapp.databinding.FragmentHomeBinding;
import org.nuist.blogapp.model.TokenManager;
import org.nuist.blogapp.view.activity.LoginActivity;
import org.nuist.blogapp.view.activity.MarkdownEditorActivity;
import org.nuist.blogapp.view.activity.SearchActivity;
import org.nuist.blogapp.view.adapter.HomeViewPagerAdapter;

import java.util.Objects;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private FragmentHomeBinding binding;
    private TokenManager tokenManager;

    public HomeFragment() {
        Log.d(TAG, "HomeFragment: 无参构造");
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater,container,false);
        Log.d(TAG, "onCreateView: ");

        initView();
        ViewListenersBinding();

        return binding.getRoot();
    }

    /**
     * 初始化view
     */
    private void initView() {
        Log.d(TAG, "initView: ");
        tokenManager = new TokenManager(getActivity());
        // 设置viewPager2的页面
        binding.homeViewPager.setAdapter(new HomeViewPagerAdapter(this,3));
        // 切换页面
        new TabLayoutMediator(binding.homePagerTable, binding.homeViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("关注");
                        break;
                    case 1:
                        tab.setText("推荐");
                        break;
                    case 2:
                        tab.setText("热榜");
                        break;
                }
            }
        }).attach();
        // 设置初始页面为“推荐”页面
        binding.homeViewPager.setCurrentItem(1);
    }

    private void ViewListenersBinding() {
        Log.d(TAG, "ViewListenersBinding: ");
        binding.searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tokenManager.getToken() == null){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getActivity(), MarkdownEditorActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
    }
}