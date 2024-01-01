package com.common.download.videoDownload

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri

class VideoDownloaderImpl(
    context: Context
): VideoDownloader {

    private val downloadManager = context.getSystemService(DownloadManager::class.java)

    override fun downloadVideo(url: String, fileName: String): Long {

        val request = DownloadManager.Request(url.toUri())
            .setMimeType("video/mp4")
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setTitle("$fileName.mp4")
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "PicBook/Videos/$fileName.mp4")

        return downloadManager.enqueue(request)

    }
}