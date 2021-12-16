package com.example.moviedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviedemo.api.TmDBApi
import com.example.moviedemo.api.TmDBClient
import com.example.moviedemo.repository.NetworkState
import com.example.moviedemo.ui.popularmovies.MainActivityViewModel
import com.example.moviedemo.ui.popularmovies.MoviesPagedListRepo
import com.example.moviedemo.ui.popularmovies.PopularMoviePagedListAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel : MainActivityViewModel
     lateinit var moviePagedListRepo : MoviesPagedListRepo


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiService  : TmDBApi = TmDBClient.getClient()
        moviePagedListRepo = MoviesPagedListRepo(apiService)

        viewModel = getViewModel()

        val movieAdapter = PopularMoviePagedListAdapter(this)

        val gridLayoutManager = GridLayoutManager(this,3)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                val viewType = movieAdapter.getItemViewType(position)

                if (viewType == movieAdapter.MOVIES_VIEW_TYPE) return 1
                else return 3
            }

        }

        movies_list_popular.layoutManager = gridLayoutManager
        movies_list_popular.setHasFixedSize(true)
        movies_list_popular.adapter = movieAdapter

        // If anything new happens in pagedList then this will observe and submit the new list inside
        //adapter
        viewModel.moviepagedList.observe(this, Observer {
            movieAdapter.submitList(it)
        })
        //Similarly the network state
        viewModel.networkState.observe(this, Observer {
           progress_bar_popular.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error_popular.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE

        })


    }

    private fun getViewModel(): MainActivityViewModel {
        return  ViewModelProviders.of(this,object  : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MainActivityViewModel(moviePagedListRepo) as T
            }
        })[MainActivityViewModel::class.java]

    }
}