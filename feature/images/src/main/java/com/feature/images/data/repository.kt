package com.feature.images.data

import com.feature.images.api.ImageApiService
import com.feature.images.model.Images
import retrofit2.Call

interface ImageRepository {

    fun getImages(
        key: String,
        query: String
    ): Call<Images>

    fun getFilteredImages(
        key: String,
        query: String,
        imageType: String,
        orientation: String,
        editorsChoice: Boolean,
        category: String,
        color: String,
        order: String
    ): Call<Images>

}

internal class ImageRepositoryImpl(
    private val api: ImageApiService
): ImageRepository {

    override fun getImages(key: String, query: String) = api.getImages(key, query)


    override fun getFilteredImages(
        key: String,
        query: String,
        imageType: String,
        orientation: String,
        editorsChoice: Boolean,
        category: String,
        color: String,
        order: String
    ) = api.getFilteredImages(
        key = key,
        query = query,
        imageType = imageType,
        orientation = orientation,
        editorsChoice = editorsChoice,
        category = category,
        color = color,
        order = order
    )

}