package org.nuist.blogapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationBarView;

import org.nuist.blogapp.databinding.ActivityMainBinding;
import org.nuist.blogapp.view.menu.HomeFragment;
import org.nuist.blogapp.view.menu.MessageFragment;
import org.nuist.blogapp.view.menu.MineFragment;

/**
 * 整体框架：首页、消息、我的
 */
public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.d(TAG, "onCreate: ");

        // 点击菜单切换页面
        binding.btnMenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // fragment管理器
                FragmentManager fragmentManager = getSupportFragmentManager();
                // fragment处理器
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                // 获取点击的id
                Integer id = item.getItemId();
                if (id==R.id.bottom_menu_left){
                    fragmentTransaction.replace(R.id.frame_layout,new HomeFragment());
                }else if (id==R.id.bottom_menu_center){
                    fragmentTransaction.replace(R.id.frame_layout,new MessageFragment());
                }else if (id==R.id.bottom_menu_right){
                    fragmentTransaction.replace(R.id.frame_layout,new MineFragment());
                }
                fragmentTransaction.commit();
                return true;
            }
        });
        binding.btnMenu.setSelectedItemId(R.id.bottom_menu_left);

        binding.btnMenu.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {

            }
        });
    }
}