package com.feature.images.api

import com.feature.images.model.Images
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

internal interface ImageApiService {

    @GET("api/")
    fun getImages(
        @Query("key") key: String,
        @Query("q") query: String,
        @Query("per_page") perPage: Int = 198,
        @Query("safesearch") safeSearch: Boolean = true
    ): Call<Images>

    @GET("api/")
    fun getFilteredImages(
        @Query("key") key: String,
        @Query("q") query: String = "Flowers+Nature+Sky+Galaxy+Home+Animals",
        @Query("safesearch") safeSearch: Boolean = true,
        @Query("image_type") imageType: String = "",
        @Query("orientation") orientation: String = "",
        @Query("editors_choice") editorsChoice: Boolean = false,
        @Query("category") category: String = "",
        @Query("color") color: String = "",
        @Query("order") order: String = ""

    ): Call<Images>


}