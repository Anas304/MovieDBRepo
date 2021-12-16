package com.example.moviedemo.model

import com.google.gson.annotations.SerializedName

data class SingleMovieDetails(
    @SerializedName("adult")
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_title")
    val originalTitle: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("popularity")
    val popularity: Double,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: String,
    val revenue : Long,
    val runtime : Int,
    val budget : Int,
    val title : String,
    val tagline : String,
    val video :  Boolean,
    @SerializedName("vote_average")
    val rating: Double,
)