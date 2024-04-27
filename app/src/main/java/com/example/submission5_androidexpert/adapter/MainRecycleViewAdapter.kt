package com.example.submission5_androidexpert.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submission5_androidexpert.R
import com.example.submission5_androidexpert.databinding.ItemInRvBinding
import com.example.submission5_androidexpert.model.Movie
import com.example.submission5_androidexpert.model.TvShow
import com.example.submission5_androidexpert.retrofit.posterURL

interface OnMovieItemClickCallback {
    fun onItemClicked(data: Movie)
}

interface OnTvShowItemClickCallback {
    fun onItemClicked(data: TvShow)
}

class MainRecycleViewAdapter(private val index: Int):
    RecyclerView.Adapter<MainRecycleViewAdapter.MainListViewHolder>() {

    private lateinit var movieItemClickCallback: OnMovieItemClickCallback
    private lateinit var tvshowItemClickCallback: OnTvShowItemClickCallback
    private var movieList: ArrayList<Movie> = ArrayList()
    private var tvshowList: ArrayList<TvShow> = ArrayList()

    fun setOnMovieItemClickCallback(onMovieItemClickCallback: OnMovieItemClickCallback) {
        this.movieItemClickCallback = onMovieItemClickCallback
    }

    fun setOnTvShowItemClickCallback(onTvShowItemClickCallback: OnTvShowItemClickCallback) {
        this.tvshowItemClickCallback = onTvShowItemClickCallback
    }

    fun clearMoviesData() {
        movieList.clear()
        notifyDataSetChanged()
    }

    fun clearTvShowsData() {
        tvshowList.clear()
        notifyDataSetChanged()
    }

    fun setMoviesData(items: ArrayList<Movie>) {
        movieList.clear()
        movieList.addAll(items)
        notifyDataSetChanged()
    }

    fun setTvShowsData(items: ArrayList<TvShow>) {
        tvshowList.clear()
        tvshowList.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainListViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_in_rv, parent, false)
        return MainListViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return if (index == 0) {
            movieList.size
        } else {
            tvshowList.size
        }
    }

    override fun onBindViewHolder(holderMain: MainListViewHolder, position: Int) {
        if(index == 0){
            holderMain.bindMovies(movieList[position])
            holderMain.itemView.setOnClickListener {
                movieItemClickCallback.onItemClicked(movieList[position])
            }
        } else {
            holderMain.bindTvShows(tvshowList[position])
            holderMain.itemView.setOnClickListener {
                tvshowItemClickCallback.onItemClicked(tvshowList[position])
            }
        }
    }

    class MainListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemInRvBinding.bind(itemView)
        private val context = itemView.context
        fun bindMovies(movie: Movie) {
            with(binding) {
                btnDelete.visibility = View.INVISIBLE
                if (!movie.title.isNullOrBlank()) {
                    txtTitle.text = String.format(
                        context.resources.getString(R.string.list_contents),
                        movie.title)
                }
                if (!movie.releaseDate.isNullOrBlank()) {
                    txtYear.text = String.format(
                        context.resources.getString(R.string.list_year),
                        movie.releaseDate.substring(0, 4))
                }
                if (!movie.overview.isNullOrBlank()) {
                    txtOverview.text = String.format(
                        context.resources.getString(R.string.list_contents),
                        movie.overview)
                }
                if (movie.voteAverage != 0f) {
                    txtScore.text = String.format(
                        context.resources.getString(R.string.detail_vote),
                        movie.voteAverage.toString()
                    )
                }
                if (!movie.posterPath.isNullOrBlank()) {
                    lblImgNotAvailable.visibility = View.INVISIBLE
                    Glide.with(context)
                        .load(posterURL + movie.posterPath)
                        .into(imgPoster)
                } else {
                    Glide.with(context)
                        .load(R.drawable.shape_img_not_available)
                        .into(imgPoster)
                    lblImgNotAvailable.visibility = View.VISIBLE
                }
            }
        }

        fun bindTvShows(tvshow: TvShow) {
            with(binding) {
                btnDelete.visibility = View.INVISIBLE
                if (!tvshow.name.isNullOrBlank()) {
                    txtTitle.text = String.format(
                        context.resources.getString(R.string.list_contents),
                        tvshow.name)
                }
                if (!tvshow.firstAirDate.isNullOrBlank()) {
                    txtYear.text = String.format(
                        context.resources.getString(R.string.list_year),
                        tvshow.firstAirDate.substring(0, 4))
                }
                if (!tvshow.overview.isNullOrBlank()) {
                    txtOverview.text = String.format(
                        context.resources.getString(R.string.list_contents),
                        tvshow.overview)
                }
                if (tvshow.voteAverage != 0f) {
                    txtScore.text = String.format(
                        context.resources.getString(R.string.detail_vote),
                        tvshow.voteAverage.toString()
                    )
                }
                if (!tvshow.posterPath.isNullOrBlank()) {
                    lblImgNotAvailable.visibility = View.INVISIBLE
                    Glide.with(context)
                        .load(posterURL + tvshow.posterPath)
                        .into(imgPoster)
                } else {
                    Glide.with(context)
                        .load(R.drawable.shape_img_not_available)
                        .into(imgPoster)
                    lblImgNotAvailable.visibility = View.VISIBLE
                }
            }
        }
    }
}