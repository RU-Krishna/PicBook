package com.feature.videos.model

data class Hits(
    val id: Int = 0,
    val type: String = "",
    val tags: String = "",
    val duration: Int = 0,
    val picture_id: String = "",
    val videos: VideoSize = VideoSize(),
    val user: String = "",
    val userImageURL: String = ""
)
