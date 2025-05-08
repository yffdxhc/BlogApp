package org.nuist.blogapp.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.nuist.blogapp.R;
import org.nuist.blogapp.ViewModel.BlogViewModel;
import org.nuist.blogapp.model.entity.Blog;
import org.nuist.blogapp.view.adapter.HotBlogAdapter;

import java.util.ArrayList;
import java.util.List;


public class HotBlogListFragment extends Fragment {
    private static final String TAG = "HotBlogListFragment";
    private RecyclerView recyclerView;
    private HotBlogAdapter hotBlogAdapter;
    private List<Blog> hotBlogs;
    private BlogViewModel blogViewModel;
    private View rootView;

    public HotBlogListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hotBlogs = new ArrayList<>();
        blogViewModel = new ViewModelProvider(this).get(BlogViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_hot_blog_list, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        hotBlogAdapter = new HotBlogAdapter(hotBlogs);
        recyclerView.setAdapter(hotBlogAdapter);

        blogViewModel.setAndGetHotBlogs().observe(getViewLifecycleOwner(), blogs -> {
            Log.d(TAG, "onChanged: " + blogs);
            hotBlogs.clear();
            hotBlogs.addAll(blogs);
            hotBlogAdapter.notifyDataSetChanged();
        });

        return rootView;
    }
}