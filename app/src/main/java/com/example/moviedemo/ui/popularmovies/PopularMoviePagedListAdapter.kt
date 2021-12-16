package com.example.moviedemo.ui.popularmovies

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviedemo.R
import com.example.moviedemo.SingleMovieActivity
import com.example.moviedemo.api.POSTER_BASE_URL
import com.example.moviedemo.model.Movie
import com.example.moviedemo.repository.NetworkState
import kotlinx.android.synthetic.main.activity_single_movie.view.*
import kotlinx.android.synthetic.main.item_movie.view.*
import kotlinx.android.synthetic.main.item_network_state.view.*

class PopularMoviePagedListAdapter(val context: Context) :
    PagedListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffCallback()) {


    val MOVIES_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2
    var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View

        if (viewType == MOVIES_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.item_movie, parent, false)
            return MovieDiffCallback.MovieItemViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.item_network_state, parent, false)
            return MovieDiffCallback.NetworkStateItemViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == MOVIES_VIEW_TYPE) {
            (holder as MovieDiffCallback.MovieItemViewHolder).bind(getItem(position), context)
        } else {
            (holder as MovieDiffCallback.NetworkStateItemViewHolder).bind(networkState)
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else {
            MOVIES_VIEW_TYPE
        }
    }

    @JvmName("setNetworkState1")
    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow: Boolean = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow: Boolean = hadExtraRow

        if (hadExtraRow != hasExtraRow) {
            if ((hadExtraRow)) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        }
        // hasExtra and hadExtra is true
        else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }


    }

    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }

        class MovieItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            fun bind(movie: Movie?, context: Context) {
                itemView.txt_movie_title_popular.text = movie?.title
                itemView.txt_movie_releaseDate_popular.text = movie?.releaseDate

                val moviePosterUrl = POSTER_BASE_URL + movie?.posterPath
                Glide.with(itemView.context)
                    .load(moviePosterUrl)
                    .into(itemView.movie_poster_popular)

                itemView.setOnClickListener {
                    val intent = Intent(context, SingleMovieActivity::class.java)
                    intent.putExtra("id", movie?.id)
                    context.startActivity(intent)
                }
            }
        }

        class NetworkStateItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            fun bind(networkState: NetworkState?) {
                if (networkState != null && networkState == NetworkState.LOADING) {
                    itemView.item_progressbar.visibility = View.VISIBLE
                } else {
                    itemView.item_progressbar.visibility = View.GONE
                }

                if (networkState != null && networkState == NetworkState.ERROR) {
                    itemView.error_message.visibility = View.VISIBLE
                    itemView.error_message.text = networkState.message
                } else if (networkState != null && networkState == NetworkState.ENDOFLIST) {
                    itemView.error_message.visibility = View.VISIBLE
                    itemView.error_message.text = networkState.message
                } else {
                    itemView.error_message.visibility = View.GONE
                }
            }
        }


    }
}
