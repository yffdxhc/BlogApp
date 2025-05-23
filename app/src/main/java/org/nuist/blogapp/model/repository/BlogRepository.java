package org.nuist.blogapp.model.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import org.nuist.blogapp.model.RetrofitClient;
import org.nuist.blogapp.model.apiService.BlogService;
import org.nuist.blogapp.model.entity.Blog;
import org.nuist.blogapp.model.entity.Comment;
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

    public MutableLiveData<Blog> getBlogInfoByBlogId(String blogId){
        MutableLiveData<Blog> blogInfo = new MutableLiveData<>();
        blogService.getBlogInfoByBlogId(blogId).enqueue(new Callback<Result<Blog>>() {
            @Override
            public void onResponse(Call<Result<Blog>> call, Response<Result<Blog>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Result<Blog> result = response.body();
                    if (result.isSuccess()) {
                        Log.d(TAG, "获取博客信息成功: " + result);
                        blogInfo.postValue(result.getData());
                    } else {
                        Log.e(TAG, "获取博客信息失败: " + result.getMessage());
                        blogInfo.postValue(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<Result<Blog>> call, Throwable t) {
                Log.e(TAG, "网络请求失败", t);
            }
        });
        return blogInfo;
    }

    public MutableLiveData<Boolean> isBlogLike(String blogId){
        MutableLiveData<Boolean> isBlogLike = new MutableLiveData<>();
        blogService.isBlogLike(blogId).enqueue(new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(Call<Result<Boolean>> call, Response<Result<Boolean>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Result<Boolean> result = response.body();
                    if (result.isSuccess()) {
                        Log.d(TAG, "判断博客是否已点赞: " + result);
                        isBlogLike.postValue(result.getData());
                    } else {
                        Log.e(TAG, "判断博客是否未点赞: " + result.getMessage());
                        isBlogLike.postValue(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<Result<Boolean>> call, Throwable t) {
                Log.e(TAG, "网络请求失败", t);
            }
        });
        return isBlogLike;
    }
    public MutableLiveData<Boolean> isBlogMarked(String blogId){
        MutableLiveData<Boolean> isBlogMarked = new MutableLiveData<>();
        blogService.isBlogMarked(blogId).enqueue(new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(Call<Result<Boolean>> call, Response<Result<Boolean>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Result<Boolean> result = response.body();
                    if (result.isSuccess()&& result.getData()!=null) {
                        Log.d(TAG, "判断博客是否已收藏: " + result);
                        isBlogMarked.postValue(result.getData());
                    }
                }
            }

            @Override
            public void onFailure(Call<Result<Boolean>> call, Throwable t) {
                Log.e(TAG, "网络请求失败", t);
            }
        });
        return isBlogMarked;
    }

    public MutableLiveData<Boolean> likeButton(String blogId){
        MutableLiveData<Boolean> likeButton = new MutableLiveData<>();
        Call<Result<Boolean>> call = blogService.likeButton(blogId);
        call.enqueue(new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(Call<Result<Boolean>> call, Response<Result<Boolean>> response) {
                if (response.isSuccessful() && response.body() != null){
                    Result<Boolean> result = response.body();
                    if (result.isSuccess()){
                        Log.d(TAG, "点赞成功: " + result);
                        likeButton.postValue(result.getData());
                    }
                }
            }

            @Override
            public void onFailure(Call<Result<Boolean>> call, Throwable t) {
                Log.e(TAG, "网络请求失败", t);
            }
        });
        return likeButton;
    }
    public MutableLiveData<Boolean> bookMarkButton(String blogId){
        MutableLiveData<Boolean> bookMarkButton = new MutableLiveData<>();
        Call<Result<Boolean>> call = blogService.bookMarkButton(blogId);
        call.enqueue(new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(Call<Result<Boolean>> call, Response<Result<Boolean>> response) {
                if (response.isSuccessful() && response.body() != null){
                    Result<Boolean> result = response.body();
                    if (result.isSuccess()&& result.getData()!=null){
                        Log.d(TAG, "收藏成功: " + result);
                        bookMarkButton.postValue(result.getData());
                    }
                }
            }

            @Override
            public void onFailure(Call<Result<Boolean>> call, Throwable t) {
                Log.e(TAG, "网络请求失败", t);
            }
        });
        return bookMarkButton;
    }

    public MutableLiveData<List<Comment>> getComments(String blogId) {
        Log.d(TAG, "getComments called with blogId: " + blogId);
        MutableLiveData<List<Comment>> comments = new MutableLiveData<>();

        Call<Result<List<Comment>>> call = blogService.getComments(blogId);
        Log.d(TAG, "Enqueuing network call: " + call.request().url());

        call.enqueue(new Callback<Result<List<Comment>>>() {
            @Override
            public void onResponse(Call<Result<List<Comment>>> call, Response<Result<List<Comment>>> response) {
                Log.d(TAG, "onResponse called");
                Log.d(TAG, "Response code: " + response.code());
                if (response.isSuccessful()) {
                    Result<List<Comment>> result = response.body();
                    if (result != null) {
                        Log.d(TAG, "Response body: " + result.toString());
                        if (result.isSuccess()) {
                            Log.d(TAG, "获取评论成功, 数据大小: " + (result.getData() != null ? result.getData().size() : 0));
                            comments.postValue(result.getData());
                        } else {
                            Log.e(TAG, "获取评论失败, 服务返回错误: " + result.getMessage());
                            comments.postValue(null);
                        }
                    } else {
                        Log.e(TAG, "响应成功但返回体为空");
                        comments.postValue(null);
                    }
                } else {
                    Log.e(TAG, "响应失败, code: " + response.code() + ", message: " + response.message());
                    comments.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<Result<List<Comment>>> call, Throwable t) {
                Log.e(TAG, "网络请求失败: " + t.getMessage(), t);
                comments.postValue(null);
            }
        });

        return comments;
    }

}
