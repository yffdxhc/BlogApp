package org.nuist.blogapp.model.apiService;

import org.nuist.blogapp.model.entity.Blog;
import org.nuist.blogapp.model.entity.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BlogService {
    @GET("/blog/getBlogsRecommended")
    Call<Result<List<Blog>>> getBlogsRecommended();
}
