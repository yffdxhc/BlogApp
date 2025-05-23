package org.nuist.blogapp.model.apiService;

import org.nuist.blogapp.model.entity.Blog;
import org.nuist.blogapp.model.entity.Result;
import org.nuist.blogapp.model.entity.User;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BlogService {
    @GET("/blog/getBlogsRecommended")
    Call<Result<List<Blog>>> getBlogsRecommended();
    @GET("/blog/getBlogDocument/{blog_id}")
    Call<Result<String>> getBlogContent(@Path("blog_id") String blog_id);
    @GET("/blog/getBlogsSearched")
    Call<Result<List<Blog>>> getBlogsSearched(@Query("query") String query);
    @Multipart
    @POST("/blog/blogRelease")
    Call<Result<String>> blogRelease(
            @PartMap Map<String, RequestBody> partMap,
            @Part MultipartBody.Part cover_image
    );

    @GET("/blog/getBlogsByUserNumber")
    Call<Result<List<Blog>>> getBlogsByUserNumber(@Query("user_number") String userNumber);

    @GET("/blog/getHotBlogs")
    Call<Result<List<Blog>>> getHotBlogs();

    @GET("/blog/getBlogInfoByBlogId")
    Call<Result<Blog>> getBlogInfoByBlogId(@Query("blog_id") String blogId);

    @GET("/blog/isBlogLike")
    Call<Result<Boolean>> isBlogLike(@Query("blog_id") String blogId);

    @GET("/blog/isBlogMarked")
    Call<Result<Boolean>> isBlogMarked(@Query("blog_id") String blogId);
}
