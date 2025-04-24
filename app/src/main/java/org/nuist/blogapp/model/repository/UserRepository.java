package org.nuist.blogapp.model.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import org.nuist.blogapp.model.RetrofitClient;
import org.nuist.blogapp.model.TokenManager;
import org.nuist.blogapp.model.apiService.UserService;
import org.nuist.blogapp.model.entity.Result;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private static final String TAG = "UserRepository";
    private UserService userServiceNoToken;
    private TokenManager tokenManager;

    public UserRepository(Context context) {
        this.tokenManager = new TokenManager(context);
        this.userServiceNoToken = RetrofitClient.getClient(context).create(UserService.class);
    }

    // 登录接口，token放到sp里
    public MutableLiveData<String> loginId(Map<String, RequestBody> partMap) {
        MutableLiveData<String> token = new MutableLiveData<>();

        Call<Result<String>> call = userServiceNoToken.loginId(partMap);
        call.enqueue(new Callback<Result<String>>() {
            @Override
            public void onResponse(Call<Result<String>> call, Response<Result<String>> response) {
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
}

