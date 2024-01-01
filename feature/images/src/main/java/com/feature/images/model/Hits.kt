package com.feature.images.model

data class Hits(
    val id: Int = 0,
    val pageUrl: String = "",
    val type: String = "",
    val tags: String = "",
    val imageURL: String = "",
    val fullHDURL: String = "",
    val largeImageURL: String = "",
    val user: String = "",
    val userImageURL: String = "",
    val imageWidth: Int = 0,
    val imageHeight: Int = 0,
)