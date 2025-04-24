package org.nuist.blogapp.model.apiService;

import org.nuist.blogapp.model.entity.Result;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

public interface UserService {
    @Multipart
    @POST("/user/loginId")
    Call<Result<String>> loginId(@PartMap Map<String, RequestBody> partMap);
}
