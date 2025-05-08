package org.nuist.blogapp.model.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import org.nuist.blogapp.model.RetrofitClient;
import org.nuist.blogapp.model.TokenManager;
import org.nuist.blogapp.model.apiService.BlogService;
import org.nuist.blogapp.model.apiService.UserService;
import org.nuist.blogapp.model.entity.Blog;
import org.nuist.blogapp.model.entity.Result;
import org.nuist.blogapp.model.entity.User;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private static final String TAG = "UserRepository";
    private UserService userService;
    private BlogService blogService;
    private TokenManager tokenManager;

    public UserRepository(Context context) {
        this.tokenManager = new TokenManager(context);
        this.userService = RetrofitClient.getClient(context).create(UserService.class);
    }

    // 登录接口，token放到sp里
    public MutableLiveData<String> loginId(Map<String, RequestBody> partMap) {
        Log.d(TAG, "loginId: "+partMap);
        MutableLiveData<String> token = new MutableLiveData<>();

        Call<Result<String>> call = userService.loginId(partMap);
        call.enqueue(new Callback<Result<String>>() {
            @Override
            public void onResponse(Call<Result<String>> call, Response<Result<String>> response) {
                Log.d(TAG, "onResponse: " + response.code());
                Log.d(TAG, "onResponse: " + response.body());
                if (response.isSuccessful() && response.body() != null) {
                    Result<String> result = response.body();
                    if (result.isSuccess() && result.getData() != null) {
                        String newToken = result.getData();
                        Log.d(TAG, "账密登录: " + result.getMessage());
                        // 登录成功后，保存 token 到 SharedPreferences
                        tokenManager.saveToken(newToken);  // 保存 token
                        token.postValue(newToken);  // 更新 LiveData
                    } else {
                        Log.e(TAG, "账密登录失败: " + result.getMessage());
                    }
                } else {
                    Log.e(TAG, "响应未成功或响应体为空");
                }
            }

            @Override
            public void onFailure(Call<Result<String>> call, Throwable t) {
                Log.e(TAG, "网络请求失败", t);
            }
        });

        return token;
    }

    public MutableLiveData<List<User>> getUserSearched(String query) {
        MutableLiveData<List<User>> userSearched = new MutableLiveData<>();
        Call<Result<List<User>>> call = userService.getUserSearched(query);
        call.enqueue(new Callback<Result<List<User>>>() {
            @Override
            public void onResponse(Call<Result<List<User>>> call, Response<Result<List<User>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Result<List<User>> result = response.body();
                    if (result.isSuccess() && result.getData() != null) {
                        Log.d(TAG, "获取搜索结果成功: " + result);
                        userSearched.postValue(result.getData());
                    } else {
                        Log.e(TAG, "获取搜索结果失败: " + result.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Result<List<User>>> call, Throwable t) {
                Log.e(TAG, "网络请求失败", t);
            }
        });

        return userSearched;
    }

    public MutableLiveData<Boolean> test() {
        MutableLiveData<Boolean> test = new MutableLiveData<>();
        Call<Result<String>> call = userService.test();
        call.enqueue(new Callback<Result<String>>() {
            @Override
            public void onResponse(Call<Result<String>> call, Response<Result<String>> response) {
                Log.d(TAG, "身份验证onResponse: " + response.code());
                Log.d(TAG, "身份验证onResponse: " + response.body());
                if (response.isSuccessful() && response.body() != null) {
                    Result<String> result = response.body();
                    if (result.isSuccess()) {
                        Log.d(TAG, "测试成功: " + result);
                        test.postValue(true);
                    } else {
                        Log.e(TAG, "测试失败: " + result.getMessage());
                        test.postValue(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<Result<String>> call, Throwable t) {
                Log.e(TAG, "网络请求失败", t);
            }
        });
        return test;
    }

    public MutableLiveData<List<User>> getUserFollows() {
        MutableLiveData<List<User>> userFollows = new MutableLiveData<>();
        Call<Result<List<User>>> call = userService.getUserFollows();
        call.enqueue(new Callback<Result<List<User>>>() {
            @Override
            public void onResponse(Call<Result<List<User>>> call, Response<Result<List<User>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Result<List<User>> result = response.body();
                    if (result.isSuccess() && result.getData() != null) {
                        Log.d(TAG, "获取关注列表成功: " + result);
                        userFollows.postValue(result.getData());
                    } else {
                        Log.e(TAG, "获取关注列表失败: " + result.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Result<List<User>>> call, Throwable t) {
                Log.e(TAG, "网络请求失败", t);
            }
        });
        return userFollows;
    }
}

