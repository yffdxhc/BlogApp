package org.nuist.blogapp.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.nuist.blogapp.R;
import org.nuist.blogapp.ViewModel.UserViewModel;


public class MyFollowsFragment extends Fragment {
    private final static String TAG  = "MyFollowsFragment";
    private Fragment currentFragment = new Fragment();
    private FragmentManager fragmentManager;
    private UserViewModel  userViewModel;

    public MyFollowsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentManager = getChildFragmentManager();
        userViewModel.getToken().observe(getViewLifecycleOwner(), token -> {
            if (token != null &&  token.length() > 0) {
                currentFragment = new FollowsFoundFragment();
                fragmentManager.beginTransaction().replace(R.id.my_follows_frame, currentFragment).commit();
            }else {
                currentFragment = new FollowsNotFoundFragment();
                fragmentManager.beginTransaction().replace(R.id.my_follows_frame, currentFragment).commit();
            }
        });
        return inflater.inflate(R.layout.fragment_my_follows, container, false);
    }
}