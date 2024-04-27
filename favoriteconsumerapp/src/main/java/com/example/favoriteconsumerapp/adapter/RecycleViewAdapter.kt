package com.example.favoriteconsumerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.favoriteconsumerapp.R
import com.example.favoriteconsumerapp.databinding.ItemInRvBinding
import com.example.favoriteconsumerapp.model.FavoriteMovie
import com.example.favoriteconsumerapp.model.FavoriteTvShow

class RecycleViewAdapter(private val index: Int): RecyclerView.Adapter<RecycleViewAdapter.ListFavoriteViewHolder>() {

    private var favMovieList: ArrayList<FavoriteMovie> = ArrayList()
    private var favTvShowList: ArrayList<FavoriteTvShow> = ArrayList()

    fun setFavMoviesData(items: ArrayList<FavoriteMovie>) {
        favMovieList.clear()
        favMovieList.addAll(items)
        notifyDataSetChanged()
    }

    fun setFavTvShowsData(items: ArrayList<FavoriteTvShow>) {
        favTvShowList.clear()
        favTvShowList.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListFavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_in_rv, parent, false)
        return ListFavoriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return when (index) {
            0 -> favMovieList.size
            else -> favTvShowList.size
        }
    }

    override fun onBindViewHolder(holder: ListFavoriteViewHolder, position: Int) {
        when (index) {
            0 -> holder.bindMovies(favMovieList[position])
            else -> holder.bindTvShows(favTvShowList[position])
        }
    }

    class ListFavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemInRvBinding.bind(itemView)
        private val context = itemView.context
        fun bindMovies(movie: FavoriteMovie) {
            with(binding) {
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
                    lblImgNotAvailable.visibility = View.GONE
                    Glide.with(context)
                        .load("https://image.tmdb.org/t/p/w185/" + movie.posterPath)
                        .into(imgPoster)
                } else {
                    Glide.with(context)
                        .load(R.drawable.shape_img_not_available)
                        .into(imgPoster)
                    lblImgNotAvailable.visibility = View.VISIBLE
                }
            }
        }
        fun bindTvShows(tvshow: FavoriteTvShow) {
            with(binding) {
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
                    lblImgNotAvailable.visibility = View.GONE
                    Glide.with(itemView.context)
                        .load("https://image.tmdb.org/t/p/w185/" + tvshow.posterPath)
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