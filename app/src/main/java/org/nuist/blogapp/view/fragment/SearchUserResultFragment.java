package org.nuist.blogapp.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.nuist.blogapp.R;
import org.nuist.blogapp.ViewModel.UserViewModel;
import org.nuist.blogapp.model.entity.User;
import org.nuist.blogapp.view.adapter.UserSearchedAdapter;

import java.util.List;


public class SearchUserResultFragment extends Fragment {
    private final String TAG = "SearchUserResultFragment";
    private List<User> usersSearched;

    public SearchUserResultFragment() {
        // Required empty public constructor
    }
    public SearchUserResultFragment(List<User> usersSearched) {
        this.usersSearched = usersSearched;
        Log.d(TAG, "SearchUserResultFragment: "+usersSearched);
    }
    public static SearchUserResultFragment newInstance(List<User> usersSearched) {
        SearchUserResultFragment fragment = new SearchUserResultFragment();
        Bundle args = new Bundle();
        args.putSerializable("usersSearched", (java.io.Serializable) usersSearched);
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
        View rootView = inflater.inflate(R.layout.fragment_search_user_result, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.users_searched);

        // recyclerView三步
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
        UserSearchedAdapter userSearchedAdapter = new UserSearchedAdapter(usersSearched);
        recyclerView.setAdapter(userSearchedAdapter);

        return rootView;
    }
}