package com.feature.videos.api

import com.feature.videos.model.Videos
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface VideoApiService {

    @GET("videos/")
    fun getVideos(
        @Query("key") key: String,
        @Query("q") q: String,
        @Query("per_page") perPage: Int = 199
    ): Call<Videos>

    @GET("videos/")
    fun getFilteredVideos(
        @Query("key") key: String,
        @Query("q") q: String,
        @Query("per_page") perPage: Int = 199,
        @Query("video_type") videoType: String = "all",
        @Query("category") category: String = "",
        @Query("editors_choice") editorsChoice: Boolean = false,
        @Query("order") order: String = ""
    ): Call<Videos>

}