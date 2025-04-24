package org.nuist.blogapp.model.apiService;

import org.nuist.blogapp.model.entity.Blog;
import org.nuist.blogapp.model.entity.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BlogService {
    @GET("/blog/getBlogsRecommended")
    Call<Result<List<Blog>>> getBlogsRecommended();
    @GET("/blog/getBlogDocument/{blog_id}")
    Call<Result<String>> getBlogContent(@Path("blog_id") String blog_id);
}
