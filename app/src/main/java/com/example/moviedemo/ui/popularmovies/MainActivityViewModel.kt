package com.example.moviedemo.ui.popularmovies

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.moviedemo.model.Movie
import com.example.moviedemo.repository.NetworkState
import io.reactivex.rxjava3.disposables.CompositeDisposable

class MainActivityViewModel(val moviesPagedListRepo: MoviesPagedListRepo) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val moviepagedList : LiveData<PagedList<Movie>> by lazy {
        moviesPagedListRepo.fetchLiveMoviePagedList(compositeDisposable)
    }

    val networkState  : LiveData<NetworkState> by lazy {
        moviesPagedListRepo.getNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}