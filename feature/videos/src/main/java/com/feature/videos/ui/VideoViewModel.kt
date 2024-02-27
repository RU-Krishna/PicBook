package com.feature.videos.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feature.videos.API
import com.feature.videos.data.VideoRepository
import com.feature.videos.filter.Category
import com.feature.videos.filter.Filters
import com.feature.videos.model.Hits
import com.feature.videos.model.Videos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VideoViewModel(
    private val repository: VideoRepository): ViewModel() {

    var searchQuery = mutableStateOf("")
    private set

    private val videos = MutableStateFlow(Videos())
    val _videos = videos.asStateFlow()

    var videoData = mutableStateOf(Hits())
    private set

    var filter = mutableStateOf(Filters())
    private set

    var fullScreenMode = mutableStateOf(false)
    private set

    init {
        viewModelScope.launch(
            Dispatchers.IO
        ) {
            videos.value = repository.getVideos(
               key = API.KEY,  //ADd your own API KEY
                q = Category.entries.random().cname
            ).execute().body()!!
        }
    }

    fun getVideos() {
        viewModelScope.launch(Dispatchers.IO) {
            videos.value = repository.getVideos(
                key = API.KEY, //Add your own API KEY here...
                q = searchQuery.value
            ).execute().body()!!
        }
    }

    fun appplyFilter() {
        viewModelScope.launch(Dispatchers.IO) {
            videos.value = repository.getFilteredVideos(
                key = API.KEY,
                q = searchQuery.value,
                category = filter.value.category,
                editorsChoice = filter.value.editorsChoice,
                videoType = filter.value.videoType,
                order = filter.value.order
            ).execute()
                .body()!!
        }
    }

    fun resetFilter() {
        filter.value = Filters()
    }

    fun changeQuery(query: String) {
        searchQuery.value = query
    }

    fun playVideo(data: Hits) {
        videoData.value = data
    }

    fun alterFullScreenMode() {
        fullScreenMode.value = !fullScreenMode.value
    }

}