package com.common.download.videoDownload

interface VideoDownloader {

    fun downloadVideo(url: String, fileName: String ): Long
}