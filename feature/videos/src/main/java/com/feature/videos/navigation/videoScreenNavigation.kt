package com.feature.videos.navigation

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.feature.videos.ui.MainVideoScreen
import com.feature.videos.ui.PortraitVideoViewScreen
import com.feature.videos.ui.VideoPlayer
import com.feature.videos.ui.VideoViewModel

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.videoGraph(
    navController: NavController,
    context: Context,
    viewModel: VideoViewModel,
    onClickDownload: (String, String) -> Long,
    modalBottomSheet: SheetState
) {

    val activity = context as Activity

    navigation(
        startDestination = VideoFeatureNavigation.VideoSearchListScreen.title,
        route = "Videos"
    ) {
        composable(
            route = VideoFeatureNavigation.VideoSearchListScreen.title,
            enterTransition = {
                this.slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right
                )
            },
            exitTransition = {
                this.slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left
                )
            }
        ) {

            val data = viewModel._videos.collectAsState()
            MainVideoScreen(
                query =  { viewModel.searchQuery.value },
                onQueryChange = viewModel::changeQuery,
                onSearch = viewModel::getVideos,
                data =  { data.value },
                onPlayVideo = {
                    viewModel.playVideo(it)
                    navController.navigate(VideoFeatureNavigation.PortraitModeVideoScreen.title) {
                        this.launchSingleTop = true
                        this.restoreState = true
                        popUpTo(route = VideoFeatureNavigation.VideoSearchListScreen.title) {
                            saveState = true
                        }
                    }
                },
                modalBottomSheet = modalBottomSheet,
                filters = { viewModel.filter.value },
                applyFilter = viewModel::appplyFilter,
                resetFilter = viewModel::resetFilter
            )
        }
        composable(
            route = VideoFeatureNavigation.PortraitModeVideoScreen.title,
            enterTransition = {
                this.slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right
                )
            },
            exitTransition = {
                this.slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left
                )
            }
        ) {
            if(activity.requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

            val video = remember {
                viewModel.videoData.value
            }

            PortraitVideoViewScreen(
                video =  { video },
                query = { viewModel.searchQuery.value },
                onBackPress = {
                    navController.popBackStack(
                        route = VideoFeatureNavigation.VideoSearchListScreen.title, inclusive = false
                    )
                },
                fullScreenMode =  { viewModel.fullScreenMode.value },
                onClickDownload = onClickDownload

            ) {
                viewModel.alterFullScreenMode()
                navController.navigate(VideoFeatureNavigation.LandScapeModeVideoScreen.title)
            }
        }
        composable(
            route = VideoFeatureNavigation.LandScapeModeVideoScreen.title,
            enterTransition = {
                this.slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right
                )
            },
            exitTransition = {
                this.slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left
                )
            }

        ) {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

            HideSystemStatusBar(activity = activity)

            VideoPlayer(
                modifier = Modifier.fillMaxSize(),
                uri = { viewModel.videoData.value.videos.medium.url },
                height = { viewModel.videoData.value.videos.medium.height
                         },
                width = { viewModel.videoData.value.videos.medium.width },
                fullScreenMode = { viewModel.fullScreenMode.value },
                enterExitFullScreen = {
                    viewModel.alterFullScreenMode()
                    navController.popBackStack(
                        route = VideoFeatureNavigation.PortraitModeVideoScreen.title,
                        inclusive = false
                    )
                },
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                )
            )

        }


    }

}

enum class VideoFeatureNavigation(
    val title: String
) {
    VideoSearchListScreen(title = "VideoHomeScreen"),
    PortraitModeVideoScreen(title = "PortraitModeVideoScreen"),
    LandScapeModeVideoScreen(title = "LandScapeModeVideoScreen")

}

@Composable
fun HideSystemStatusBar(
    activity: Activity
) {
   
    DisposableEffect(key1 = Unit) {

        val window = activity.window

        val insetsController = WindowCompat.getInsetsController(window, window.decorView)

        insetsController.apply {
            hide(WindowInsetsCompat.Type.statusBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        onDispose {
            insetsController.apply {
                show(WindowInsetsCompat.Type.statusBars())
                systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
            }
        }
    }

}


