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
import org.nuist.blogapp.ViewModel.UserViewModel;
import org.nuist.blogapp.model.entity.Blog;
import org.nuist.blogapp.model.entity.User;
import org.nuist.blogapp.view.adapter.BlogsByUserAdapter;
import org.nuist.blogapp.view.adapter.FollowsNavAdapter;

import java.util.ArrayList;
import java.util.List;

public class FollowsFoundFragment extends Fragment {
    private final String TAG = "FollowsFoundFragment";
    private List<User> follows;
    private List<Blog> blogsByUser;
    private UserViewModel  userViewModel;
    private BlogViewModel blogViewModel;
    private FollowsNavAdapter followsNavAdapter;
    private BlogsByUserAdapter blogsByUserAdapter;
    private RecyclerView followingRecyclerView;
    private RecyclerView blogsByUserRecyclerView;
    private View rootView;

    public FollowsFoundFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        follows = new ArrayList<>();
        blogsByUser = new ArrayList<>();
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        blogViewModel = new ViewModelProvider(this).get(BlogViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        rootView = inflater.inflate(R.layout.fragment_follows_found, container, false);

        followingRecyclerView = rootView.findViewById(R.id.rv_following_nav);
        followingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false));

        userViewModel.setAndGetUsersFollows().observe(getViewLifecycleOwner(), users -> {
            Log.d(TAG, "关注数量: " + users.size());
            follows.clear();
            follows.addAll(users);
            followsNavAdapter.notifyDataSetChanged();
        });

        followsNavAdapter = new FollowsNavAdapter(follows);
        followingRecyclerView.setAdapter(followsNavAdapter);

        // 动态菜单头像点击事件
        followsNavAdapter.setOnUserClickListener(user -> {
            Log.d(TAG, "点击用户: " + user.getUsername());

            // 根据点击的用户 ID 更新动态
            blogViewModel.setAndGetBlogsByUserNumber(user.getUser_number()).observe(getViewLifecycleOwner(), blogs -> {
                blogsByUser.clear();
                blogsByUser.addAll(blogs);
                blogsByUserAdapter.notifyDataSetChanged();
            });
        });

        blogsByUserRecyclerView = rootView.findViewById(R.id.rv_following_content);
        blogsByUserRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));

        blogsByUserAdapter = new BlogsByUserAdapter(blogsByUser);
        blogsByUserRecyclerView.setAdapter(blogsByUserAdapter);
        return rootView;
    }
}