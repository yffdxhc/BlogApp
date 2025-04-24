package org.nuist.blogapp.model.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import org.nuist.blogapp.model.RetrofitClient;
import org.nuist.blogapp.model.apiService.BlogService;
import org.nuist.blogapp.model.entity.Blog;
import org.nuist.blogapp.model.entity.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class BlogRepository {
    private static final String TAG = "BlogRepository";
    private BlogService blogService;

    public BlogRepository(Context context)
    {
        blogService = RetrofitClient.getClient(context).create(BlogService.class);
    }

    // 获取推荐博客
    public MutableLiveData<List<Blog>> getBlogsRecommended() {
        MutableLiveData<List<Blog>> blogsRecommended = new MutableLiveData<>();
        Call<Result<List<Blog>>> call = blogService.getBlogsRecommended();
        call.enqueue(new retrofit2.Callback<Result<List<Blog>>>() {
            @Override
            public void onResponse(Call<Result<List<Blog>>> call, Response<Result<List<Blog>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Result<List<Blog>> result = response.body();
                    if (result.isSuccess() && result.getData() != null) {
                        Log.d(TAG, "获取推荐博客成功: " + result);
                        blogsRecommended.postValue(result.getData());
                    } else {
                        Log.e(TAG, "获取推荐博客失败: " + result.getMessage());
                    }
                } else {
                    Log.e(TAG, "响应未成功或响应体为空");
                }
            }

            @Override
            public void onFailure(Call<Result<List<Blog>>> call, Throwable t) {
                Log.e(TAG, "网络请求失败", t);
            }
        });
        return blogsRecommended;
    }
}
