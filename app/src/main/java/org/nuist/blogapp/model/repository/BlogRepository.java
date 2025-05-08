package org.nuist.blogapp.model.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import org.nuist.blogapp.model.RetrofitClient;
import org.nuist.blogapp.model.apiService.BlogService;
import org.nuist.blogapp.model.entity.Blog;
import org.nuist.blogapp.model.entity.Result;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
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

    public MutableLiveData<String> getBlogDocument(String blog_id) {
        MutableLiveData<String> blogDocument = new MutableLiveData<>();
        Call<Result<String>> call = blogService.getBlogContent(blog_id);
        call.enqueue(new retrofit2.Callback<Result<String>>() {
            @Override
            public void onResponse(Call<Result<String>> call, Response<Result<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Result<String> result = response.body();
                    if (result.isSuccess() && result.getData() != null) {
                        Log.d(TAG, "获取博客内容成功: " + result);
                        blogDocument.postValue(result.getData());
                    } else {
                        Log.e(TAG, "获取博客内容失败: " + result.getMessage());
                    }
                } else {
                    Log.e(TAG, "响应未成功或响应体为空");
                }
            }

            @Override
            public void onFailure(Call<Result<String>> call, Throwable t) {

            }
        });
        return blogDocument;
    }

    public MutableLiveData<List<Blog>> getBlogsSearched(String query) {
        MutableLiveData<List<Blog>> blogsSearched = new MutableLiveData<>();
        Call<Result<List<Blog>>> call = blogService.getBlogsSearched(query);
        call.enqueue(new retrofit2.Callback<Result<List<Blog>>>() {
            @Override
            public void onResponse(Call<Result<List<Blog>>> call, Response<Result<List<Blog>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Result<List<Blog>> result = response.body();
                    if (result.isSuccess() && result.getData() != null) {
                        Log.d(TAG, "获取搜索结果成功: " + result);
                        blogsSearched.postValue(result.getData());
                    } else {
                        Log.e(TAG, "获取搜索结果失败: " + result.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Result<List<Blog>>> call, Throwable t) {
                Log.e(TAG, "网络请求失败", t);
            }
        });
        return blogsSearched;
    }

    public MutableLiveData<Boolean> blogRelease(String blog_title,
                                               String content,
                                               String blog_summary,
                                               Integer type_id,
                                               File coverImage) {
        Log.d(TAG, "blogRelease: "+ blog_title+ " "+content+" "+blog_summary+" "+type_id+" "+coverImage);
        MutableLiveData<Boolean> blogReleaseResult = new MutableLiveData<>();
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), coverImage);
        MultipartBody.Part coverImagePart = MultipartBody.Part.createFormData("cover_image", coverImage.getName(), requestBody);

        Map<String, RequestBody> partMap = new HashMap<>();
        partMap.put("blog_title", RequestBody.create(MediaType.parse("multipart/form-data"), blog_title));
        partMap.put("content", RequestBody.create(MediaType.parse("multipart/form-data"), content));
        partMap.put("blog_summary", RequestBody.create(MediaType.parse("multipart/form-data"), blog_summary));
        partMap.put("type_id", RequestBody.create(MediaType.parse("multipart/form-data"), type_id.toString()));
        Log.d(TAG, "blogRelease: "+ partMap);

        Call<Result<String>> call = blogService.blogRelease(
                partMap,
                coverImagePart
        );
        call.enqueue(new retrofit2.Callback<Result<String>>() {
            @Override
            public void onResponse(Call<Result<String>> call, Response<Result<String>> response) {
                Log.d(TAG, "博客发布响应blogRelease: "+ response);
                Log.d(TAG, "博客发布响应体blogRelease: "+ response.body());
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "blogRelease: "+ response.body());
                    Result<String> result = response.body();
                    if (result.isSuccess()) {
                        Log.d(TAG, "发布博客成功: " + result);
                        blogReleaseResult.postValue(true);
                    } else {
                        Log.e(TAG, "发布博客失败: " + result.getMessage());
                        blogReleaseResult.postValue(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<Result<String>> call, Throwable t) {
                Log.e(TAG, "网络请求失败", t);
            }
        });
        return blogReleaseResult;
    }

    public MutableLiveData<List<Blog>> getBlogsByUserNumber(String userNumber) {
        MutableLiveData<List<Blog>> blogsByUserNumber = new MutableLiveData<>();
        Call<Result<List<Blog>>> call = blogService.getBlogsByUserNumber(userNumber);
        call.enqueue(new Callback<Result<List<Blog>>>() {
            @Override
            public void onResponse(Call<Result<List<Blog>>> call, Response<Result<List<Blog>>> response) {
                Log.d(TAG, "用户文章blogRelease: "+ response);
                Log.d(TAG, "用户文章blogRelease: "+ response.body());
                if (response.isSuccessful() && response.body() != null) {
                    Result<List<Blog>> result = response.body();
                    if (result.isSuccess() && result.getData() != null) {
                        Log.d(TAG, "获取用户博客成功: " + result);
                        blogsByUserNumber.postValue(result.getData());
                    } else {
                        Log.e(TAG, "获取用户博客失败: " + result.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Result<List<Blog>>> call, Throwable t) {
                Log.e(TAG, "网络请求失败", t);
            }
        });
        return blogsByUserNumber;
    }

    public MutableLiveData<List<Blog>> getHotBlogs() {
        MutableLiveData<List<Blog>> hotBlogs = new MutableLiveData<>();
        Call<Result<List<Blog>>> call = blogService.getHotBlogs();
        call.enqueue(new Callback<Result<List<Blog>>>() {
            @Override
            public void onResponse(Call<Result<List<Blog>>> call, Response<Result<List<Blog>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Result<List<Blog>> result = response.body();
                    if (result.isSuccess() && result.getData() != null) {
                        Log.d(TAG, "获取热门博客成功: " + result);
                        hotBlogs.postValue(result.getData());
                    } else {
                        Log.e(TAG, "获取热门博客失败: " + result.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Result<List<Blog>>> call, Throwable t) {
                Log.e(TAG, "网络请求失败", t);
            }
        });
        return hotBlogs;
    }
}
