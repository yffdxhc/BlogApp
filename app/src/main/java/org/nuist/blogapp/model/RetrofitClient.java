package org.nuist.blogapp.model;

import android.content.Context;

import org.nuist.blogapp.model.interceptor.AuthInterceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private final static String BASE_URL="http://192.168.21.1:8080";
    private volatile static Retrofit retrofit;
    public static Retrofit getClient(Context context){
        if (retrofit==null){
            synchronized (RetrofitClient.class){
                if (retrofit == null) {
                    OkHttpClient client = new OkHttpClient.Builder()
                            .addInterceptor(new AuthInterceptor(context))
                            .build();
                    retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(client)
                            .build();
                }
            }
        }
        return retrofit;
    }
    public static String getBaseUrl(){
        return BASE_URL;
    }
}