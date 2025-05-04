package org.nuist.blogapp.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.nuist.blogapp.R;
import org.nuist.blogapp.view.fragment.QueryChangeFragment;
import org.nuist.blogapp.view.fragment.QuerySubmitFragment;

public class SearchActivity extends AppCompatActivity {

    private SearchView searchView;
    private Toolbar toolbar;
    private Fragment  fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initView();
        ViewListenersBinding();
    }

    private void ViewListenersBinding() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(SearchActivity.this, "新的搜索内容：" + query, Toast.LENGTH_SHORT).show();
                fragment = new QuerySubmitFragment();
                Bundle bundle = new Bundle();
                bundle.putString("query",query);
                fragment.setArguments(bundle);
                switchFragment();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(SearchActivity.this, "修改搜索内容：" + newText, Toast.LENGTH_SHORT).show();
                fragment = new QueryChangeFragment();
                switchFragment();
                return true;
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initView() {
        toolbar = findViewById(R.id.Search_toolbar);
        searchView  = findViewById(R.id.searchView);
        fragment = new QueryChangeFragment();
        switchFragment();
    }

    private void switchFragment() {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, fragment);
            fragmentTransaction.commit();
        }
    }
}
