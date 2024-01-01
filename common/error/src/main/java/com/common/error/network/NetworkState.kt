package com.common.error.network

sealed interface NetworkState {

    data object Available: NetworkState

    data object UnAvailable: NetworkState

}