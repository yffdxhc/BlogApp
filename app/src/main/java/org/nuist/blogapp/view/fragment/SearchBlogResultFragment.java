package org.nuist.blogapp.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
import org.nuist.blogapp.view.adapter.BlogsSearchedAdapter;

import java.util.ArrayList;
import java.util.List;


public class SearchBlogResultFragment extends Fragment {
    private final String TAG = "SearchBlogResultFragment";
    private List<Blog> mBlogList = new ArrayList<>();
    private BlogViewModel blogViewModel;

    public SearchBlogResultFragment() {
        // Required empty public constructor
        Log.d(TAG, "无参构造方法");
    }
    public SearchBlogResultFragment(List<Blog> blogList){
        mBlogList = blogList;
        Log.d(TAG, "有参构造方法，获取搜索到的博客："+ blogList);
    }
    public static SearchBlogResultFragment newInstance(String param1, String param2) {
        SearchBlogResultFragment fragment = new SearchBlogResultFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_blog_result, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.blogs_searched);

        // recyclerView三步
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
        BlogsSearchedAdapter blogsSearchedAdapter = new BlogsSearchedAdapter(mBlogList);
        recyclerView.setAdapter(blogsSearchedAdapter);

/*        blogViewModel.setAndGetBlogsSearched("").observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<List<Blog>>() {
            @Override
            public void onChanged(List<Blog> blogs) {

            }
        });*/

        return rootView;
    }
}