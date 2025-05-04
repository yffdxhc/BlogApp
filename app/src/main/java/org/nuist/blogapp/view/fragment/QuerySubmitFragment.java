package org.nuist.blogapp.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.nuist.blogapp.R;
import org.nuist.blogapp.view.adapter.QuerySubmitViewPagerAdapter;

public class QuerySubmitFragment extends Fragment {
    private final static String TAG = "QuerySubmitFragment";

    private TabLayout tableLayout;
    private ViewPager2 viewPager;
    private QuerySubmitViewPagerAdapter querySubmitViewPagerAdapter;

    public QuerySubmitFragment() {
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
        Log.d(TAG, "onCreateView: ");

        // 1. 先 inflate 当前 Fragment 的布局
        View view = inflater.inflate(R.layout.fragment_query_submit, container, false);

        // 2. 从当前 Fragment 的布局中查找控件
        TabLayout tabLayout = view.findViewById(R.id.query_submit_title);
        ViewPager2 viewPager = view.findViewById(R.id.query_submit_view_pager);

        // 3. 设置适配器
        QuerySubmitViewPagerAdapter adapter = new QuerySubmitViewPagerAdapter(this, 2);
        viewPager.setAdapter(adapter);

        // 4. 绑定 TabLayout 和 ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("博客");
                    break;
                case 1:
                    tab.setText("用户");
                    break;
            }
        }).attach();

        // 5. 返回视图
        return view;
    }
}