package org.nuist.blogapp.view.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.nuist.blogapp.view.fragment.BlogRecommendedFragment;
import org.nuist.blogapp.view.fragment.MyFollowsFragment;
import org.nuist.blogapp.view.fragment.QuerySubmitFragment;
import org.nuist.blogapp.view.fragment.SearchBlogResultFragment;
import org.nuist.blogapp.view.fragment.SearchUserResultFragment;

public class QuerySubmitViewPagerAdapter extends FragmentStateAdapter {
    private final int numbers;
    public QuerySubmitViewPagerAdapter(@NonNull QuerySubmitFragment querySubmitFragment, int numbers) {
        super(querySubmitFragment);
        this.numbers = numbers;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new SearchBlogResultFragment();
            case 1:
                return new SearchUserResultFragment();
            default:
                return new SearchBlogResultFragment();
        }
    }

    @Override
    public int getItemCount() {
        return numbers;
    }
}
