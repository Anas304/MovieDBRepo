package com.example.moviedemo.ui.popularmovies

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.moviedemo.api.POST_PER_PAGE
import com.example.moviedemo.api.TmDBApi
import com.example.moviedemo.model.Movie
import com.example.moviedemo.repository.MoviesPagingDataSource
import com.example.moviedemo.repository.MoviesPagingDataSourceFactory
import com.example.moviedemo.repository.NetworkState
import io.reactivex.rxjava3.disposables.CompositeDisposable

class MoviesPagedListRepo(val apiService : TmDBApi) {

    lateinit var moviePagedList  : LiveData<PagedList<Movie>>
    lateinit var moviesDataSourceFactory: MoviesPagingDataSourceFactory

    fun fetchLiveMoviePagedList(compositeDisposable: CompositeDisposable) : LiveData<PagedList<Movie>>{

        moviesDataSourceFactory = MoviesPagingDataSourceFactory(apiService,compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(moviesDataSourceFactory,config).build()
        return  moviePagedList

    }

    fun getNetworkState() : LiveData<NetworkState>{
        return Transformations.switchMap<MoviesPagingDataSource,NetworkState>(
            moviesDataSourceFactory.moviesPagingLiveDataSource,
            MoviesPagingDataSource::networkState)
    }

}