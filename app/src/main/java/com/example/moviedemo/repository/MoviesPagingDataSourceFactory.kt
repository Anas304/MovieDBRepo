package com.example.moviedemo.repository

import androidx.lifecycle.MutableLiveData
import com.example.moviedemo.api.TmDBApi
import com.example.moviedemo.model.Movie
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.sql.DataSource

// Factory class for loading pages of snapshot data into a PagedList.
class MoviesPagingDataSourceFactory(val apiService : TmDBApi, val compositeDisposable: CompositeDisposable) : androidx.paging.DataSource.Factory<Int,Movie>() {

    val moviesPagingLiveDataSource  = MutableLiveData<MoviesPagingDataSource>()

    override fun create(): androidx.paging.DataSource<Int, Movie> {

        val movieDataSource = MoviesPagingDataSource(apiService,compositeDisposable)
        moviesPagingLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}