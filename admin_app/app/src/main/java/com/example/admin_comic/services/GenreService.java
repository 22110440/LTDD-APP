package com.example.admin_comic.services;

import com.example.admin_comic.models.ApiResponse;
import com.example.admin_comic.models.Genre;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface GenreService {

    @FormUrlEncoded
    @POST("genres")
    Call<ApiResponse<Genre>>
    createGenre(@Field("title") String title
    );

    @DELETE("genres/{genreId}")
    Call<ApiResponse>
    deleteGenre(@Path("genreId") Integer genreId);

    @FormUrlEncoded
    @PUT("genres/{genreId}")
    Call<ApiResponse>
    updateMovie(@Path("genreId") Integer genreId ,
                @Field("title") String title
    );

    @GET("genres")
    Call<ApiResponse<List<Genre>>>
    getGenres();



}
