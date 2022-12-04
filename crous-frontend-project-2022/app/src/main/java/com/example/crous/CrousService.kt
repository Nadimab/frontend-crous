package com.example.crous

import retrofit2.Call
import retrofit2.http.*

interface CrousService {
    @POST("crous/")
    fun getCat(@Body crous: ExpandedCrous): Call<ExpandedCrous>;

    @GET("crous/")
    fun findAll(@Query("page") page: Int,
                @Query("rows") rows: Int,
                @Query("offset") offset: Int,
                @Query("sortBy") sortBy: String,
                @Query("fav") favorites: Int,
                @Query("geoloc") geoloc: Int,
    ):Call<List<MapsData>>;

    @GET("crous/{id}")
    fun findOneById(@Path("id") id: String);

    @POST("/search/title")
    fun searchByTitle(@Body title: String);

    @PUT("crous/{id}")
    fun toggleFavorite(@Path("id") id: String);

    @PATCH("/{id}")
    fun update(@Path("id") id: String, @Body updateCrousDto: ExpandedCrous);

    @DELETE("crous/{id}")
    fun remove(@Path("id") id: String);
}