package org.nuist.blogapp.model.apiService;

import org.nuist.blogapp.model.entity.Result;
import org.nuist.blogapp.model.entity.User;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface UserService {
    @Multipart
    @POST("/user/loginId")
    Call<Result<String>> loginId(@PartMap Map<String, RequestBody> partMap);
    @GET("/user/getUserSearched")
    Call<Result<List<User>>> getUserSearched(@Query("query") String query);

    @POST("/user/test")
    Call<Result<String>> test();
    @GET("/user/getUserFollows")
    Call<Result<List<User>>> getUserFollows();

    @GET("/user/getUserInfo")
    Call<Result<User>> getUserInfo();

    @GET("/user/getChatObject")
    Call<Result<List<User>> > getChatObject();
}
