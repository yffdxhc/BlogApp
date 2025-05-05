package org.nuist.blogapp.view.adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.nuist.blogapp.model.entity.Blog;
import org.nuist.blogapp.model.entity.User;
import org.nuist.blogapp.view.fragment.QuerySubmitFragment;
import org.nuist.blogapp.view.fragment.SearchBlogResultFragment;
import org.nuist.blogapp.view.fragment.SearchUserResultFragment;

import java.util.List;

public class QuerySubmitViewPagerAdapter extends FragmentStateAdapter {
    private final static String TAG = "QuerySubmitViewPagerAdapter";
    private final int numbers;
    private List<Blog> blogsSearched;
    private List<User> usersSearched;
    public QuerySubmitViewPagerAdapter(@NonNull QuerySubmitFragment querySubmitFragment, int numbers, List<Blog> blogsSearched,  List<User> usersSearched) {
        super(querySubmitFragment);
        this.numbers = numbers;
        this.blogsSearched = blogsSearched;
        this.usersSearched = usersSearched;
        Log.d(TAG, "搜索结果ViewPager获取的搜索到的博客："+blogsSearched);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new SearchBlogResultFragment(blogsSearched);
            case 1:
                return new SearchUserResultFragment(usersSearched);
            default:
                return new SearchBlogResultFragment();
        }
    }

    @Override
    public int getItemCount() {
        return numbers;
    }
}
