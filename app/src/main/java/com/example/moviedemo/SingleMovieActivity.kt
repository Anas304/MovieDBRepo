package com.example.moviedemo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.moviedemo.api.POSTER_BASE_URL
import com.example.moviedemo.api.TmDBApi
import com.example.moviedemo.api.TmDBClient
import com.example.moviedemo.databinding.ActivitySingleMovieBinding
import com.example.moviedemo.model.SingleMovieDetails
import com.example.moviedemo.repository.MovieDetailsRepo
import com.example.moviedemo.viewmodel.MovieViewModel
import kotlinx.android.synthetic.main.activity_single_movie.*
import java.text.NumberFormat
import java.util.*

class SingleMovieActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySingleMovieBinding
    private lateinit var viewModel: MovieViewModel
    private lateinit var movieDetailsRepo: MovieDetailsRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movieId : Int = intent.getIntExtra("id",1)
        val apiService : TmDBApi = TmDBClient.getClient()
        movieDetailsRepo = MovieDetailsRepo(apiService)

        viewModel = getViewModel(movieId)

        viewModel.singleMovieDetails.observe(this, Observer {
            bindUI(it)
        })

    }

    @SuppressLint("SetTextI18n", "ShowToast")
    private fun bindUI(it : SingleMovieDetails) {
        movie_title.text = it.originalTitle
        movie_tagline.text = it.tagline
        movie_runtime.text = it.runtime.toString() + "minutes"
        movie_overview.text = it.overview

        val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
        movie_budget.text = formatCurrency.format(it.budget)
        movie_revenue.text = formatCurrency.format(it.revenue)

        val moviePosterUrl = POSTER_BASE_URL + it.posterPath
        Glide.with(this)
            .load(moviePosterUrl)
            .into(movie_poster)

        fab_movie.setOnClickListener{
            Toast.makeText(this,"Your movie has been Booked",Toast.LENGTH_SHORT).show()
        }

    }
    
    private fun getViewModel(movieId: Int): MovieViewModel {
        return  ViewModelProviders.of(this,object  : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MovieViewModel(movieDetailsRepo,movieId) as T
            }
        })[MovieViewModel::class.java]

    }

}
