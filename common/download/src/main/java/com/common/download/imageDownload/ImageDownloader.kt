package com.common.download.imageDownload

interface ImageDownloader {

    fun downloadImage(url: String, fileName: String): Long
}