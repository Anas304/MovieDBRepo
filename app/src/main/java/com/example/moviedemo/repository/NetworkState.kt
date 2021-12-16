package com.example.moviedemo.repository

enum class State{
    RUNNING,
    SUCCESS,
    FAILED
}


class NetworkState(val state: State, val message: String) {
    companion object {
        val LOADING: NetworkState
        val LOADED: NetworkState
        val ERROR: NetworkState
        val ENDOFLIST : NetworkState

        init {
            LOADING = NetworkState(State.RUNNING, "Running")
            LOADED = NetworkState(State.SUCCESS, "Success")
            ERROR = NetworkState(State.FAILED, "Something wrong with Network")
            ENDOFLIST =  NetworkState(State.FAILED, "The list is ended!")
        }
    }
}