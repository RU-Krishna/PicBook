package com.feature.images.filters

data class Filters(
    val imageType: String = "all",
    val orientation: String = "all",
    val editorsChoice: Boolean = false,
    val category: String = ""

)
