package com.example.crous

import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface CrousService {
    @POST("crous/")
    fun getCat(@Body crous: ExpandedCrous): Call<ExpandedCrous>;

    @GET("crous/")
    fun findMapsData(@Query("page") page: Int,
                @Query("rows") rows: Int,
                @Query("offset") offset: Int,
                @Query("sortBy") sortBy: String,
                @Query("fav") favorites: Int,
                @Query("geoloc") geoloc: Int,
    ):Call<List<MapsData>>;

    @GET("crous/")
    fun findAll(@Query("page") page: Int,
                @Query("rows") rows: Int,
                @Query("offset") offset: Int,
                @Query("sortBy") sortBy: String,
                @Query("fav") favorites: Int,
                @Query("geoloc") geoloc: Int,
                @Query("refresh") refresh: Int,
    ):Call<ReducedResponse>;

    @GET("crous/{id}")
    fun findOneById(@Path("id") id: String) : Call<ExpandedCrous>;

    @FormUrlEncoded
    @POST("crous/search/title")
    fun searchByTitle(@Field("title") title:String): Call<ArrayList<ReducedCrous>>;

    @PUT("crous/{id}")
    fun toggleFavorite(@Path("id") id: String): Call<JSONObject>;

    @PATCH("/{id}")
    fun update(@Path("id") id: String, @Body updateCrousDto: ExpandedCrous);

    @DELETE("crous/{id}")
    fun remove(@Path("id") id: String);
}