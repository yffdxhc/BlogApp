package org.nuist.blogapp.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.nuist.blogapp.R;
import org.nuist.blogapp.ViewModel.BlogViewModel;
import org.nuist.blogapp.ViewModel.UserViewModel;
import org.nuist.blogapp.model.entity.Blog;
import org.nuist.blogapp.model.entity.User;
import org.nuist.blogapp.view.adapter.QuerySubmitViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class QuerySubmitFragment extends Fragment {
    private final static String TAG = "QuerySubmitFragment";
    private String query;

    private BlogViewModel blogViewModel;
    private UserViewModel userViewModel;

    private List<Blog> blogsSearched = new ArrayList<>();
    private List<User> usersSearched = new ArrayList<>();

    private boolean blogsLoaded = false;
    private boolean usersLoaded = false;

    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    public QuerySubmitFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        blogViewModel = new ViewModelProvider(this).get(BlogViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        if (getArguments() != null) {
            query = getArguments().getString("query");
            Log.d(TAG, "初始阶段: 获取提交的关键字 " + query);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_query_submit, container, false);
        tabLayout = view.findViewById(R.id.query_submit_title);
        viewPager = view.findViewById(R.id.query_submit_view_pager);

        observeData(); // 开始观察数据

        return view;
    }

    private void observeData() {
        blogViewModel.setAndGetBlogsSearched(query).observe(getViewLifecycleOwner(), blogs -> {
            blogsSearched = blogs != null ? blogs : new ArrayList<>();
            blogsLoaded = true;
            tryInitAdapter();
        });

        userViewModel.setAndGetUsersSearched(query).observe(getViewLifecycleOwner(), users -> {
            usersSearched = users != null ? users : new ArrayList<>();
            usersLoaded = true;
            tryInitAdapter();
        });
    }

    private void tryInitAdapter() {
        if (blogsLoaded && usersLoaded) {
            Log.d(TAG, "两类数据都加载完成，初始化适配器");
            QuerySubmitViewPagerAdapter adapter = new QuerySubmitViewPagerAdapter(this, 2, blogsSearched, usersSearched);
            viewPager.setAdapter(adapter);

            new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
                if (position == 0) {
                    tab.setText("博客");
                } else {
                    tab.setText("用户");
                }
            }).attach();
        }
    }
}
