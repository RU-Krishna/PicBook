package com.feature.download.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.common.media.imageMedia.Images
import com.common.media.videoMedia.Videos
import com.feature.download.ui.DownloadsScreen

fun NavGraphBuilder.downloadGraph(
    images: () -> List<Images>,
    videos: () -> List<Videos>
) {
    navigation(
        startDestination = DownloadFeatureScreens.DownloadsHomeScreen.title,
        route = "Downloads"
    ) {
        composable(
            route = DownloadFeatureScreens.DownloadsHomeScreen.title,
            enterTransition = {
                this.slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right
                )
            },
            exitTransition = {
                this.slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left
                )
            }
        ) {
            DownloadsScreen(
                imageList = images,
                videoList = videos
            )
        }

    }
}

internal enum class DownloadFeatureScreens(
    val title: String
) {
    DownloadsHomeScreen(title = "DownloadsHomeScreen"),
    VideoPlayScreen(title = "VideoPlayScreen"),
    ImagePreviewScreen(title = "ImagePreviewScreen")
}