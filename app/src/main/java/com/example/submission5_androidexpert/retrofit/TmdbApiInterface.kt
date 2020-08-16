package com.example.submission5_androidexpert.retrofit

import com.example.submission5_androidexpert.model.Detail
import com.example.submission5_androidexpert.model.GetMovieListResponse
import com.example.submission5_androidexpert.model.GetTvShowListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApiInterface{
    @GET("discover/movie?")
    fun getMovieList(
        @Query("api_key") apiKey: String,
        @Query("language") languange: String
    ): Call<GetMovieListResponse>

    @GET("discover/tv?")
    fun getTvShowList(
        @Query("api_key") apiKey: String,
        @Query("language") languange: String
    ): Call<GetTvShowListResponse>

    @GET("movie/{id}?")
    fun getMovieDetail(
        @Path("id") id: Int?,
        @Query("api_key") apiKey: String
    ): Call<Detail>

    @GET("tv/{id}?")
    fun getTvShowDetail(
        @Path("id") id: Int?,
        @Query("api_key") apiKey: String
    ): Call<Detail>

    @GET("discover/movie?")
    fun getMovieReleaseToday(
        @Query("api_key") apiKey: String,
        @Query("primary_release_date.gte") from: String,
        @Query("primary_release_date.lte") to: String
    ): Call<GetMovieListResponse>

    @GET("search/movie?")
    fun searchMovie(
        @Query("api_key") apiKey: String,
        @Query("language") languange: String,
        @Query("query") query: String
    ) : Call<GetMovieListResponse>

    @GET("search/tv?")
    fun searchTvShow(
        @Query("api_key") apiKey: String,
        @Query("language") languange: String,
        @Query("query") query: String
    ): Call<GetTvShowListResponse>
}