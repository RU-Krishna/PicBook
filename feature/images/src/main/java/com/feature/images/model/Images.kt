package com.feature.images.model

data class Images(
    val total: Int = 0,
    val totalHits: Int = 0,
    val hits: List<Hits> = listOf()
)