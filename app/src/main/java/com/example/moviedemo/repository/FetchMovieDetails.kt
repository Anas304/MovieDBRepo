package com.example.moviedemo.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviedemo.api.TmDBApi
import com.example.moviedemo.model.SingleMovieDetails
import io.reactivex.rxjava3.disposables.CompositeDisposable

class FetchMovieDetails(val api: TmDBApi, val compositDisposible: CompositeDisposable) {
    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _movieResponse = MutableLiveData<SingleMovieDetails>()
    val singleMovieResponse: LiveData<SingleMovieDetails>
        get() = _movieResponse


    fun fetchDetails(movieId: Int) {
        _networkState.postValue(NetworkState.LOADING)


        try {
            // RxJava thread to make network calls
            compositDisposible.add(
                api.getDetails(movieId)
                    .subscribeOn(io.reactivex.rxjava3.schedulers.Schedulers.io())
                    .subscribe({
                        _movieResponse.postValue(it)
                        _networkState.postValue(NetworkState.LOADED)
                    }, {
                        _networkState.postValue(NetworkState.ERROR)
                        it.message?.let { it1 -> Log.e("FetchMovieDetails", it1) }
                    })
            )

        } catch (e: Exception) {
            e.message?.let { Log.e("FetchMovieDetails", it) }
        }
    }
}