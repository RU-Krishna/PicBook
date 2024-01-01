package com.example.picBook

import android.app.Application
import android.widget.Toast
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import com.feature.images.data.DefaultImageContainer
import com.feature.images.data.ImageContainer
import com.feature.videos.data.VideoModuleContainer
import com.feature.videos.data.VideoModuleContainerImpl
import java.net.UnknownHostException
import kotlin.system.exitProcess

class MyApplication : Application(), ImageLoaderFactory {


    lateinit var imageContainer: ImageContainer
    lateinit var videoContainer: VideoModuleContainer

    override fun onCreate() {
        super.onCreate()

        try {
            imageContainer = DefaultImageContainer()
            videoContainer = VideoModuleContainerImpl()
        }
        catch(_: UnknownHostException)
        {
            Toast
                .makeText(
                    this,
                    "Connect to the Internet",
                    Toast.LENGTH_SHORT
                )
                .show()
            exitProcess(0)
        }
        }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader(this).newBuilder()
            .memoryCachePolicy(CachePolicy.ENABLED)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.15)
                    .weakReferencesEnabled(true)
                    .build()
            }
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCache {
                DiskCache.Builder()
                    .maxSizePercent(0.05)
                    .directory(cacheDir)
                    .build()
            }
            .logger(DebugLogger())
            .build()
    }


}