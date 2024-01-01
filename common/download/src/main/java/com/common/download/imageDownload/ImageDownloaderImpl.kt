package com.common.download.imageDownload

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri

class ImageDownloaderImpl(
    context: Context
) : ImageDownloader {


    private val downloaderManager = context.getSystemService(DownloadManager::class.java)

    override fun downloadImage(url: String, fileName: String): Long {

        val request = DownloadManager.Request(url.toUri())
            .setMimeType("image/jpeg")
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setTitle("$fileName.jpg")
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "Pixabay/Images/$fileName.jpg")


        return downloaderManager.enqueue(request)


    }
}