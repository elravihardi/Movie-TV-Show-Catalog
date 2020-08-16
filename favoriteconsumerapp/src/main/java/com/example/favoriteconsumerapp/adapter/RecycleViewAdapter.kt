package com.example.favoriteconsumerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.favoriteconsumerapp.R
import com.example.favoriteconsumerapp.model.FavoriteMovie
import com.example.favoriteconsumerapp.model.FavoriteTvShow
import kotlinx.android.synthetic.main.item_in_rv.view.*

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
        fun bindMovies(movie: FavoriteMovie) {
            with(itemView) {
                if (!movie.title.isNullOrBlank()) {
                    txt_title.text = String.format(
                        context.resources.getString(R.string.list_contents),
                        movie.title)
                }
                if (!movie.releaseDate.isNullOrBlank()) {
                    txt_year.text = String.format(
                        context.resources.getString(R.string.list_year),
                        movie.releaseDate.substring(0, 4))
                }
                if (!movie.overview.isNullOrBlank()) {
                    txt_overview.text = String.format(
                        context.resources.getString(R.string.list_contents),
                        movie.overview)
                }
                if (movie.voteAverage != 0f) {
                    txt_score.text = String.format(
                        context.resources.getString(R.string.detail_vote),
                        movie.voteAverage.toString()
                    )
                }
                if (!movie.posterPath.isNullOrBlank()) {
                    lbl_img_not_available.visibility = View.GONE
                    Glide.with(context)
                        .load("https://image.tmdb.org/t/p/w185/" + movie.posterPath)
                        .into(img_poster)
                } else {
                    Glide.with(context)
                        .load(R.drawable.shape_img_not_available)
                        .into(img_poster)
                    lbl_img_not_available.visibility = View.VISIBLE
                }
            }
        }
        fun bindTvShows(tvshow: FavoriteTvShow) {
            with(itemView) {
                if (!tvshow.name.isNullOrBlank()) {
                    txt_title.text = String.format(
                        context.resources.getString(R.string.list_contents),
                        tvshow.name)
                }
                if (!tvshow.firstAirDate.isNullOrBlank()) {
                    txt_year.text = String.format(
                        context.resources.getString(R.string.list_year),
                        tvshow.firstAirDate.substring(0, 4))
                }
                if (!tvshow.overview.isNullOrBlank()) {
                    txt_overview.text = String.format(
                        context.resources.getString(R.string.list_contents),
                        tvshow.overview)
                }
                if (tvshow.voteAverage != 0f) {
                    txt_score.text = String.format(
                        context.resources.getString(R.string.detail_vote),
                        tvshow.voteAverage.toString()
                    )
                }
                if (!tvshow.posterPath.isNullOrBlank()) {
                    lbl_img_not_available.visibility = View.GONE
                    Glide.with(itemView.context)
                        .load("https://image.tmdb.org/t/p/w185/" + tvshow.posterPath)
                        .into(img_poster)
                } else {
                    Glide.with(context)
                        .load(R.drawable.shape_img_not_available)
                        .into(img_poster)
                    lbl_img_not_available.visibility = View.VISIBLE
                }
            }
        }
    }
}