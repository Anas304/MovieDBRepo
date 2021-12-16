package com.example.moviedemo.repository

import androidx.lifecycle.LiveData
import com.example.moviedemo.api.TmDBApi
import com.example.moviedemo.model.SingleMovieDetails
import io.reactivex.rxjava3.disposables.CompositeDisposable
// An extra step for offline caching
class MovieDetailsRepo(val apiService : TmDBApi) {
    lateinit var fetchMovieDetails : FetchMovieDetails

    fun fetchSingleMovieDetails(compositeDisposable: CompositeDisposable, movieId : Int) : LiveData<SingleMovieDetails> {
        fetchMovieDetails = FetchMovieDetails(apiService,compositeDisposable)
        fetchMovieDetails.fetchDetails(movieId)
        return fetchMovieDetails.singleMovieResponse
    }

    fun getMovieNetworkState() : LiveData<NetworkState> {
        return fetchMovieDetails.networkState
    }
}