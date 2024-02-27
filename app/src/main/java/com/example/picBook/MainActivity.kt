package com.example.picBook

import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.common.download.imageDownload.ImageDownloaderImpl
import com.common.download.videoDownload.VideoDownloaderImpl
import com.common.error.network.NetworkState
import com.common.media.imageMedia.AppImageProviderImpl
import com.common.media.videoMedia.AppVideoProviderImpl
import com.example.picBook.ui.FeatureNavigation
import com.example.picBook.ui.NetworkChangeDialog
import com.example.picBook.ui.NetworkViewModel
import com.example.picBook.ui.theme.PicBazaarTheme
import com.example.picBook.viewModelFactory.imageViewModelFactory
import com.example.picBook.viewModelFactory.videoViewModelFactory
import com.feature.download.navigation.downloadGraph
import com.feature.images.navigation.imageNavigationGraph
import com.feature.images.ui.ImageViewModel
import com.feature.videos.navigation.videoGraph
import com.feature.videos.ui.VideoViewModel
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    private lateinit var networkConnectivity: ConnectivityManager

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val imageViewModel: ImageViewModel by viewModels(factoryProducer = { imageViewModelFactory })
        val videoViewModel: VideoViewModel by viewModels(factoryProducer = { videoViewModelFactory })

        val imageProvider by lazy { AppImageProviderImpl(this) }
        val videoProvider by lazy { AppVideoProviderImpl(this) }


        enableEdgeToEdge()
        setContent {
            PicBazaarTheme {

                val navController: NavHostController = rememberNavController()
                
                val modalBottomSheetState = rememberModalBottomSheetState()


                var offlineFeatureAccessibility by remember {
                    mutableStateOf(true)
                }

                Scaffold(
                    modifier = Modifier.safeDrawingPadding(),
                    bottomBar = {
                        BottomBar(
                            navController = navController,
                            featureAvailabilityCallback = {
                                offlineFeatureAccessibility = true
                            }
                        )
                    }

                ) {

                    if (readMediaPermissions()) {

                        AnimatedVisibility(
                            visible = NetworkViewModel.networkState.value == NetworkState.UnAvailable && offlineFeatureAccessibility,
                            enter = expandIn(),
                            exit = shrinkOut()
                        ) {
                            NetworkChangeDialog(
                                onClick = {
                                    offlineFeatureAccessibility = false
                                    navController.navigate(FeatureNavigation.Downloads.title) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )

                        }


                        NavHost(
                            navController = navController,
                            startDestination = "Downloads",
                            modifier = Modifier.padding(it)
                        ) {

                            imageNavigationGraph(
                                navController = navController,
                                viewModel = imageViewModel,
                                onClickDownload =
                                ImageDownloaderImpl(this@MainActivity)::downloadImage,
                                modalBottomSheetState = modalBottomSheetState
                            )
                            videoGraph(
                                navController = navController,
                                viewModel = videoViewModel,
                                context = this@MainActivity,
                                onClickDownload = VideoDownloaderImpl(this@MainActivity)::downloadVideo,
                                modalBottomSheet = modalBottomSheetState
                            )
                            downloadGraph(
                                images = imageProvider::getImageData,
                                videos = videoProvider::getVideoData
                            )


                        }
                    }

                }

            }
        }
    }

    override fun onResume() {
        super.onResume()

        networkConnectivity = getSystemService(ConnectivityManager::class.java)

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_TRUSTED)
            .build()

        networkConnectivity.registerNetworkCallback(
            networkRequest,
            NetworkCallback
        )


    }

    override fun onPause() {
        super.onPause()
        networkConnectivity.unregisterNetworkCallback(NetworkCallback)
    }


    object NetworkCallback : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            NetworkViewModel.networkAvailable()

            Log.d("net", "Available ${NetworkViewModel.networkState}")
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            NetworkViewModel.networkLost()
            Log.d("net", "Lost ${NetworkViewModel.networkState}")

        }

    }
}


@Composable
fun BottomBar(
    navController: NavController,
    featureAvailabilityCallback: () -> Unit = {}
) {

    var selectedItem by rememberSaveable {
        mutableStateOf(FeatureNavigation.Downloads.title)
    }

    var isBottomBarVisible by rememberSaveable {
        mutableStateOf(true)
    }

    val currentDestination = navController.currentBackStackEntryAsState()

    isBottomBarVisible = when (currentDestination.value?.destination?.route) {

        FeatureNavigation.Downloads.firstScreen -> {
            selectedItem = FeatureNavigation.Downloads.title
            true
        }

        FeatureNavigation.Videos.firstScreen -> {
            featureAvailabilityCallback()
            selectedItem = FeatureNavigation.Videos.title
            true
        }

        FeatureNavigation.Image.firstScreen -> {
            featureAvailabilityCallback()
            selectedItem = FeatureNavigation.Image.title
            true
        }

        else -> {
            false
        }
    }

    AnimatedVisibility(
        visible = isBottomBarVisible,
        enter = slideInVertically(),
        exit = slideOutVertically()
    ) {
        BottomAppBar(
            modifier = Modifier.minimumInteractiveComponentSize()
        ) {
            FeatureNavigation.entries.forEach {
                NavigationBarItem(
                    selected = selectedItem == it.title,
                    onClick = {
                        if (selectedItem != it.title) {
                            selectedItem = it.title
                            navController.navigate(selectedItem) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = it.image),
                            contentDescription = it.title
                        )
                    },
                    label = {
                        Text(
                            text = it.title
                        )
                    }
                )
            }

        }
    }
}

@Composable
fun readMediaPermissions(
    context: Context = LocalContext.current
): Boolean {


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permissions = arrayOf(
            android.Manifest.permission.READ_MEDIA_IMAGES,
            android.Manifest.permission.READ_MEDIA_VIDEO
        )

        var imagePermission by remember {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    permissions[0]
                ) == PackageManager.PERMISSION_GRANTED
            )
        }

        var videoPermission by remember {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    permissions[1]
                ) == PackageManager.PERMISSION_GRANTED
            )
        }

        val permissionLauncher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) {
                imagePermission = it.getValue(permissions[0])
                videoPermission = it.getValue(permissions[1])
            }

        AnimatedVisibility(!imagePermission && !videoPermission) {
            LaunchedEffect(key1 = Unit) {
                permissionLauncher.launch(
                    permissions
                )

                delay(5000)
            }

        }


        return imagePermission && videoPermission
    } else {
        var permission by remember {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            )
        }

        val readPermissionLauncher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
                permission = it
            }

        if (!permission) {
            LaunchedEffect(key1 = Unit) {
                readPermissionLauncher.launch(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }
        }

        return permission
    }


}



