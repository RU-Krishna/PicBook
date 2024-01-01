package com.feature.videos.model

data class VideoSize(
    val large: VideoInfo = VideoInfo(),
    val medium: VideoInfo = VideoInfo(),
    val small: VideoInfo = VideoInfo(),
    val tiny: VideoInfo = VideoInfo()
)
