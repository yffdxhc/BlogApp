package org.nuist.blogapp.model.interceptor;

import android.content.Context;
import android.util.Log;

import org.nuist.blogapp.model.TokenManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private static final String TAG = "AuthInterceptor";
    private TokenManager tokenManager;

    public AuthInterceptor(Context context) {
        tokenManager = new TokenManager(context);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = tokenManager.getToken();
        Log.d(TAG, "intercept: "+token);
        if (token != null) {
            Request originalRequest = chain.request();
            Log.d(TAG, "请求地址: " + originalRequest.url());
            Log.d(TAG, "原始请求头: " + originalRequest.headers());
            Request newRequest = originalRequest.newBuilder()
                    // .header("Authorization", "Bearer " + token)  // 添加 Authorization 头
                    .header("token", token)  // 添加 Authorization 头
                    .build();
            Log.d(TAG, "新的请求头: " + newRequest.headers());
            Log.d(TAG, "新的请求地址: " + newRequest.url());
            return chain.proceed(newRequest);
        }

        return chain.proceed(chain.request());
    }
}

