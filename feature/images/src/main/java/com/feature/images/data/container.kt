package com.feature.images.data

import com.feature.images.api.ImageApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface ImageContainer {
    val imageRepository: ImageRepository

}

class DefaultImageContainer : ImageContainer {

    private val BASE_URL = "https://pixabay.com/"

    val retrofit = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val imageService: ImageApiService by lazy {
        retrofit.create(ImageApiService::class.java)
    }

    override val imageRepository: ImageRepository by lazy {
            ImageRepositoryImpl(imageService)
    }

}