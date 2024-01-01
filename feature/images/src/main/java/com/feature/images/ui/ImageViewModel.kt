package com.feature.images.ui

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.feature.images.API
import com.feature.images.data.ImageRepository
import com.feature.images.filters.Filters
import com.feature.images.model.Hits
import com.feature.images.model.Images
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ImageViewModel(
    private val imageRepository: ImageRepository,
): ViewModel() {


    private val imageList = MutableStateFlow(Images())
    val _imageList = imageList.asStateFlow()


    var previewImageHits = mutableStateOf(Hits())
        private set

    var filters = mutableStateOf(
        Filters()
    )
    private set


    var searchQuery = mutableStateOf(
        ""
    )
    private set


    init {
        viewModelScope.async(Dispatchers.IO) {
            imageList.value = imageRepository.getImages(
                key = API.KEY, //Add your own API KEy
                query = listOf(
                    "Flowers",
                    "Butterfly",
                    "Roses",
                    "Nature",
                    "Sky",
                    "Moon"
                ).random()
            ).execute()
                .body()!!

        }
    }

    fun changeQuery(query: String) {
        searchQuery.value = query
    }

    fun getImages() {

        viewModelScope.async(Dispatchers.IO) {
            imageList.value = imageRepository.getImages(
                key = API.KEY,  //Add your own API_KEY
                query = searchQuery.value
            ).execute()
                .body()!!
        }
    }

    fun showImage(hits: Hits) {
        previewImageHits.value = hits
    }



}