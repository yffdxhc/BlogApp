package org.nuist.blogapp.view.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.nuist.blogapp.view.fragment.BlogRecommendedFragment;
import org.nuist.blogapp.view.fragment.HotBlogListFragment;
import org.nuist.blogapp.view.fragment.MyFollowsFragment;
import org.nuist.blogapp.view.menu.HomeFragment;

/**
 * 主页下页面切换适配器
 */
public class HomeViewPagerAdapter extends FragmentStateAdapter {
    //页数
    private final int numbers;
    public HomeViewPagerAdapter(@NonNull HomeFragment homeFragment, int numbers) {
        super(homeFragment);
        this.numbers = numbers;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new MyFollowsFragment();
            case 1:
                return new BlogRecommendedFragment();
            case 2:
                return new HotBlogListFragment();
            default:
                return new BlogRecommendedFragment();
        }
    }

    @Override
    public int getItemCount() {
        return numbers;
    }
}
