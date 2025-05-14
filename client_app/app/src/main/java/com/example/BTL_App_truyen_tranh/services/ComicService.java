package com.example.BTL_App_truyen_tranh.services;

import com.example.BTL_App_truyen_tranh.models.ApiResponse;
import com.example.BTL_App_truyen_tranh.models.Chapter;
import com.example.BTL_App_truyen_tranh.models.ChartFilter;
import com.example.BTL_App_truyen_tranh.models.Comic;
import com.example.BTL_App_truyen_tranh.models.Comment;
import com.example.BTL_App_truyen_tranh.models.Favorite;
import com.example.BTL_App_truyen_tranh.models.Genre;
import com.example.BTL_App_truyen_tranh.models.Rating;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ComicService {



    @GET("comics/{comicId}")
    Call<ApiResponse<Comic>>
    getComic(@Path("comicId") Integer comicId);

    @GET("comics/genres/{genreId}")
    Call<ApiResponse<List<Comic>>>
    getComicByGenre(@Path("genreId") Integer genreId);


    @DELETE("comics/{comicId}")
    Call<ApiResponse>
    deleteComic(@Path("comicId") Integer comicId);

    @FormUrlEncoded
    @POST("comics")
    Call<ApiResponse>
    createComic(@Field("title") String title,
                @Field("status") String status,
                @Field("poster") String poster,
                @Field("description") String description,
                @Field("genre") Integer genre
    );

    @FormUrlEncoded
    @PUT("comics/{comicId}")
    Call<ApiResponse>
    updateComic(@Path("comicId") Integer comicId ,
                @Field("title") String title,
                @Field("description") String description,
                @Field("poster") String director,
                @Field("status") String releaseYear,
                @Field("genre") Integer genre
    );

    @GET("comics/search/{query}")
    Call<ApiResponse<List<Comic>>>
    searchComic(@Path("query") String query);


    @FormUrlEncoded
    @POST("{comicId}/chapters")
    Call<ApiResponse>
    createChapter(@Path("comicId") Integer comicId,
                @Field("title") String title,
                @Field("image") List<String> image
    );

    @GET("{comicId}/chapters")
    Call<ApiResponse<List<Chapter>>>
    getChaptersByComic(
            @Path("comicId") Integer comicId);


    @DELETE("chapters/{chapterId}")
    Call<ApiResponse>
    deleteChapter(@Path("chapterId") Integer chapterId);

    @GET("chapters/{chapterId}")
    Call<ApiResponse<Chapter>>
    getChapter(
            @Path("chapterId") Integer chapterId);

    @FormUrlEncoded
    @PUT("chapters/{chapterId}")
    Call<ApiResponse>
    updateChapter(
            @Path("chapterId") Integer chapterId,
            @Field("title") String title,
            @Field("image") List<String> image
    );


    @FormUrlEncoded
    @POST("{comicId}/comments")
    Call<ApiResponse>
    createComment(
            @Path("comicId") Integer comicId,
            @Field("text") String text);

    @GET("{comicId}/comments")
    Call<ApiResponse<List<Comment>>>
    getCommentsByComic(@Path("comicId") Integer comicId);

    @GET("{comicId}/ratings")
    Call<ApiResponse<Rating>>
    getUserRatingByComic(@Path("comicId") Integer comicId);


    @FormUrlEncoded
    @POST("{comicId}/ratings")
    Call<ApiResponse>
    ratingComic(@Path("comicId") Integer comicId,
              @Field("rating") Integer rating);

    @GET("comments")
    Call<ApiResponse<List<Comment>>>
    getComments();

    @DELETE("comments/{commentId}")
    Call<ApiResponse>
    deleteComment(@Path("commentId") Integer commentId);

    @GET("ratings")
    Call<ApiResponse<List<Rating>>>
    getRatings();

    @DELETE("ratings/{ratingId}")
    Call<ApiResponse>
    deleteRating(@Path("ratingId") Integer ratingId);

    @GET("comic-chart")
    Call<ApiResponse<List<Comic>>>
    getComicsChart(@Query("filter") String filter);

    @GET("favorites")
    Call<ApiResponse<List<Favorite>>>
    getFavorites();

    @GET("comics")
    Call<ApiResponse<List<Comic>>>
    getComics(@Query("search") String search,
              @Query("sort") String sort);

    @POST("comic/{comicId}/favorites")
    Call<ApiResponse>
    addFavorite(@Path("comicId") Integer comicId);

    @DELETE("comic/{comicId}/favorites")
    Call<ApiResponse>
    removeFavorite(@Path("comicId") Integer comicId);

    @POST("comic/{comicId}/view-count")
    Call<ApiResponse>
    addViewCount(@Path("comicId") Integer comicId);

}
