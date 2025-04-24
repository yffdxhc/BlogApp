package org.nuist.blogapp.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.nuist.blogapp.R;
import org.nuist.blogapp.ViewModel.BlogViewModel;
import org.nuist.blogapp.model.entity.Blog;
import org.nuist.blogapp.view.adapter.BlogsRecommendedAdapter;

import java.util.ArrayList;
import java.util.List;

public class BlogRecommendedFragment extends Fragment {
    private static final String TAG = "BlogRecommendedFragment";
    // 推荐的书籍
    private List<Blog> blogsRecommended;
    private BlogViewModel blogViewModel;
    private RecyclerView recyclerView;
    BlogsRecommendedAdapter blogsRecommendedAdapter;
    private View rootView;

    public BlogRecommendedFragment() {
        Log.d(TAG, "BlogRecommendedFragment: 无参构造");
        // Required empty public constructor
    }
    public static BlogRecommendedFragment newInstance(String param1, String param2) {
        BlogRecommendedFragment fragment = new BlogRecommendedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        blogsRecommended = new ArrayList<>();
        blogViewModel = new ViewModelProvider(this).get(BlogViewModel.class);
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_blog_recommended, container, false);
        recyclerView = rootView.findViewById(R.id.blogs_recommended);

        Log.d(TAG, "onCreateView: ");
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
        blogViewModel.setAndGetBlogsRecommended().observe(getViewLifecycleOwner(), new Observer<List<Blog>>() {
            @Override
            public void onChanged(List<Blog> blogs) {
                Log.d(TAG, "onChanged: "+blogs);
                blogsRecommended.clear();
                blogsRecommended.addAll(blogs);
                blogsRecommendedAdapter.notifyDataSetChanged();
            }
        });
        blogsRecommendedAdapter = new BlogsRecommendedAdapter(blogsRecommended);
        recyclerView.setAdapter(blogsRecommendedAdapter);

        return rootView;
    }
}