package org.nuist.blogapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 启动页
 */
public class StartActivity extends AppCompatActivity {
    private final static String TAG = "StartActivity";
    private final static int SPLASH_TIME_OUT = 1000; // 5000 毫秒 = 5 秒

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Log.d(TAG, "onCreate: 进入启动页");

        // 使用 Handler 延迟 5 秒后执行 UI 操作
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent it = new Intent(getApplicationContext(), MainActivity.class); // 跳转到启动之后出现的页面 Activity
                startActivity(it);
                Log.d(TAG, String.format("run: %d毫秒后进入App服务MainActivity", SPLASH_TIME_OUT));
                finish(); // 关闭当前活动
            }
        }, SPLASH_TIME_OUT);
    }
}