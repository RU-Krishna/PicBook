package com.feature.videos.data

import com.feature.videos.api.VideoApiService
import com.feature.videos.model.Videos
import retrofit2.Call

interface VideoRepository {

    fun getVideos(
        key: String,
        q: String
    ): Call<Videos>

    fun getFilteredVideos(
        key: String,
        q: String,
        videoType: String,
        editorsChoice: Boolean,
        category: String,
        order: String
    ): Call<Videos>
}

internal class VideoRepositoryImpl(
    private val api: VideoApiService
) : VideoRepository {

    override fun getVideos(key: String, q: String): Call<Videos> =
        api.getVideos(
            key = key,
            q = q
        )

    override fun getFilteredVideos(
        key: String,
        q: String,
        videoType: String,
        editorsChoice: Boolean,
        category: String,
        order: String
    ): Call<Videos> =
        api.getFilteredVideos(
            key = key,
            q = q,
            videoType = videoType,
            editorsChoice = editorsChoice,
            category = category,
            order = order
        )
}