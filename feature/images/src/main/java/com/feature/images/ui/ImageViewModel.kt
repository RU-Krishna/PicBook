package com.feature.images.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feature.images.API
import com.feature.images.data.ImageRepository
import com.feature.images.filters.Category
import com.feature.images.filters.Filters
import com.feature.images.model.Hits
import com.feature.images.model.Images
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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
        viewModelScope.launch(Dispatchers.IO) {
            imageList.value = imageRepository.getImages(
                key = API.KEY, //Add your own API KEy
                query = Category.entries.random().cname
            ).execute()
                .body()!!

        }
    }

    fun changeQuery(query: String) {
        searchQuery.value = query
    }

    fun getImages() {

        viewModelScope.launch(Dispatchers.IO) {
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



    fun applyFilter() {
        viewModelScope.launch(Dispatchers.IO) {
            imageList.value = imageRepository.getFilteredImages(
                key = API.KEY,
                query = searchQuery.value,
                imageType = filters.value.imageType,
                category = filters.value.category,
                orientation = filters.value.orientation,
                color = filters.value.color,
                order = filters.value.order,
                editorsChoice = filters.value.editorsChoice
            ).execute()
                .body()!!
        }
    }

    fun resetFilter() {
        filters.value = Filters()
    }

    fun addMoreImages() {
        viewModelScope.launch(Dispatchers.IO) {
            imageList.value.hits += imageRepository.getFilteredImages(
                key = API.KEY,
                query = searchQuery.value,
                imageType = filters.value.imageType,
                category = filters.value.category,
                orientation = filters.value.orientation,
                color = filters.value.color,
                order = filters.value.order,
                editorsChoice = filters.value.editorsChoice
            ).execute().body()!!.hits
        }
    }




}