package com.example.moviedemo.api

import com.example.moviedemo.model.MoviesResponse
import com.example.moviedemo.model.SingleMovieDetails
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmDBApi {

    @GET("movie/popular")
    // we need to tell retrofit that page below is the page in the link [aka api URL ]
    fun getPopularMovieDetails(@Query("page")page : Int)  : Single<MoviesResponse>

    @GET("movie/{movie_id}")
    fun getDetails(@Path("movie_id")id : Int) : Single<SingleMovieDetails>
}