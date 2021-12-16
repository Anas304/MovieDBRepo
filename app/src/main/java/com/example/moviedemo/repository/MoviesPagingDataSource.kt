package com.example.moviedemo.repository

import android.telecom.Call
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.moviedemo.api.FIRST_PAGE
import com.example.moviedemo.api.TmDBApi
import com.example.moviedemo.model.Movie
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MoviesPagingDataSource(val apiService: TmDBApi, val compositDisposible: CompositeDisposable) :
    PageKeyedDataSource<Int, Movie>() {

    val page = FIRST_PAGE
    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {
        networkState.postValue(NetworkState.LOADING)
        compositDisposible.add(
            apiService.getPopularMovieDetails(page)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    callback.onResult(it.moviesList, null, page + 1)
                }, {
                        networkState.postValue(NetworkState.ERROR)
                    it.message?.let { it1 -> Log.e("MoviesPagingDataSource", it1) }
                })
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        networkState.postValue(NetworkState.LOADING)
        compositDisposible.add(
            //  params.key will increment the page number automatically
            apiService.getPopularMovieDetails(params.key)
                .subscribeOn(Schedulers.io())
                .subscribe({
                           if (it.totalPages >= params.key){
                               callback.onResult(it.moviesList, params.key+1)
                               networkState.postValue(NetworkState.LOADED)
                           }
                    else{
                        networkState.postValue(NetworkState.ENDOFLIST)
                    }
                }, {
                    networkState.postValue(NetworkState.ERROR)
                    it.message?.let { it1 -> Log.e("MoviesPagingDataSource", it1) }
                })
        )

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        TODO("Not yet implemented")
        // I am keeping it empty as loading of previous page is automatically handled by RecyclerView
    }


}