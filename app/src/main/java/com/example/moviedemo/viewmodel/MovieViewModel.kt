package com.example.moviedemo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.moviedemo.model.SingleMovieDetails
import com.example.moviedemo.repository.MovieDetailsRepo
import com.example.moviedemo.repository.NetworkState

//
class MovieViewModel (val movieDetailsRepo: MovieDetailsRepo, movieId : Int) : ViewModel() {

    private val compositeDisposable = io.reactivex.rxjava3.disposables.CompositeDisposable()

    val singleMovieDetails : LiveData<SingleMovieDetails> by lazy {
        movieDetailsRepo.fetchSingleMovieDetails(compositeDisposable,movieId)
    }

    val networkDetails : LiveData<NetworkState> by lazy {
        movieDetailsRepo.getMovieNetworkState()
    }

    // disposing thread when Host get destroyed to avoid memory-leaks
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}

