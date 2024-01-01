package com.feature.videos.model

data class Videos(
    val total: Int = 0,
    val totalHits: Int = 0,
    val hits: List<Hits> = listOf()
)
