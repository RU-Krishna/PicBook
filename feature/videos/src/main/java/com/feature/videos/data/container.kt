package com.feature.videos.data

import com.feature.videos.api.VideoApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface VideoModuleContainer {
    val repository: VideoRepository
}

class VideoModuleContainerImpl : VideoModuleContainer {

    private val BASE_URL = "https://pixabay.com/api/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitVideoService: VideoApiService by lazy {
        retrofit.create(VideoApiService::class.java)
    }

    override val repository: VideoRepository by lazy {
        VideoRepositoryImpl(retrofitVideoService)
    }


}