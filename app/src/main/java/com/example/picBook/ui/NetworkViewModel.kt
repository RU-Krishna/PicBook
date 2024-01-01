package com.example.picBook.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.common.error.network.NetworkState

class NetworkViewModel: ViewModel() {

    companion object {

        var networkState: MutableState<NetworkState> = mutableStateOf(NetworkState.Available)
            private set

        fun networkAvailable() {
            networkState.value = NetworkState.Available
        }

        fun networkLost() {
            networkState.value = NetworkState.UnAvailable
        }
    }

}
