package com.example.picBook.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.picBook.MyApplication
import com.feature.images.ui.ImageViewModel
import com.feature.videos.ui.VideoViewModel

val imageViewModelFactory = object: ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T{

        val application = checkNotNull(extras[APPLICATION_KEY]) as MyApplication

        val repository = application.imageContainer.imageRepository

        return ImageViewModel(
            imageRepository = repository
        ) as T
    }
}

val videoViewModelFactory = object: ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {

        val application = checkNotNull(extras[APPLICATION_KEY]) as MyApplication

        val repository = application.videoContainer.repository

        return VideoViewModel(
            repository = repository
        ) as T
    }
}