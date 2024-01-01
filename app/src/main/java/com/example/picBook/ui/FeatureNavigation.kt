package com.example.picBook.ui

import androidx.annotation.DrawableRes
import com.example.picBook.R

enum class FeatureNavigation(
    val title: String,
    @DrawableRes val image: Int,
    val firstScreen: String
) {
    Image(title = "Image", image = R.drawable.image_24px, firstScreen = "ImageHomeScreen"),
    Videos(title = "Videos", image = R.drawable.video_library_24px, firstScreen = "VideoHomeScreen"),
    Downloads(title = "Downloads", image = R.drawable.download_24px, firstScreen = "DownloadsHomeScreen")
}

