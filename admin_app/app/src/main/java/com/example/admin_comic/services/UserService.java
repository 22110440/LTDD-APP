package com.example.admin_comic.services;

import com.example.admin_comic.models.ApiResponse;
import com.example.admin_comic.models.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface UserService {

    @FormUrlEncoded
    @POST("login")
    Call<ApiResponse<User>>
    login(@Field("username") String username,
          @Field("password") String password);

    @FormUrlEncoded
    @POST("signup")
    Call<ApiResponse>
    signup(@Field("username") String username,
           @Field("password") String password,
           @Field("fullname") String fullname);

    @GET("profile")
    Call<ApiResponse<User>> getUser();

    @FormUrlEncoded
    @PUT("profile")
    Call<ApiResponse<User>>
    updateUser(@Field("fullname") String fullname,
               @Field("photoURL") String photoURL);

}
