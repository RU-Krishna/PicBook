package com.feature.images.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.feature.images.data.ImageContainer
import com.feature.images.ui.ImageScreen
import com.feature.images.ui.ImageViewModel
import com.feature.images.ui.PreviewImage

fun NavGraphBuilder.imageNavigationGraph(
    navController: NavController,
    viewModel: ImageViewModel,
    onClickDownload: (String, String) -> Long
) {

    navigation(
        startDestination = ImageFeatureScreens.ImageHomeScreen.screenName,
        route = "Image"
    ) {

        composable(
            route = ImageFeatureScreens.ImageHomeScreen.screenName,
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
            ImageScreen(
                query = viewModel.searchQuery.value,
                getImages = viewModel::getImages,
                changeQuery = viewModel::changeQuery,
                images = viewModel._imageList.collectAsState().value,
                onImageClick = {
                    viewModel.showImage(it)
                    navController.navigate(ImageFeatureScreens.ImagePreviewScreen.screenName) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(route = ImageFeatureScreens.ImageHomeScreen.name) {
                            saveState = true
                        }
                    }

                }
            )
        }
        composable(
            route = ImageFeatureScreens.ImagePreviewScreen.screenName
        ) {
            val hits = remember {
                viewModel.previewImageHits.value
            }
            PreviewImage(
                onBackPress = {
                    navController.popBackStack(route = ImageFeatureScreens.ImageHomeScreen.screenName, inclusive = false)
                },
                hits = hits,
                onClickDownload = onClickDownload,
                query = { viewModel.searchQuery.value }
            )
        }


    }
}

internal enum class ImageFeatureScreens(
    val screenName: String
) {

    ImageHomeScreen(screenName = "ImageHomeScreen"),
    ImagePreviewScreen(screenName = "ImagePreviewScreen")

}
