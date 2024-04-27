package com.example.submission5_androidexpert.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submission5_androidexpert.R
import com.example.submission5_androidexpert.databinding.ItemInRvBinding
import com.example.submission5_androidexpert.retrofit.posterURL
import com.example.submission5_androidexpert.room.FavoriteMovie
import com.example.submission5_androidexpert.room.FavoriteTvShow

interface OnFavMovieItemClickCallback {
    fun onItemClicked (favMovie: FavoriteMovie)
}

interface OnDeleteMovieClickCallback {
    fun onItemClicked (favMovie: FavoriteMovie)
}

interface OnFavTvShowItemClickCallback {
    fun onItemClicked(favTvShow: FavoriteTvShow)
}

interface OnDeleteTvShowClickCallback {
    fun onItemClicked(favTvShow: FavoriteTvShow)
}

class FavoriteRecycleViewAdapter(private val index: Int): RecyclerView.Adapter<FavoriteRecycleViewAdapter.FavoriteListViewHolder>() {

    private lateinit var movieItemClickCallback: OnFavMovieItemClickCallback
    private lateinit var deleteMovieClickCallback: OnDeleteMovieClickCallback
    private lateinit var tvshowItemClickCallback: OnFavTvShowItemClickCallback
    private lateinit var deleteTvShowClickCallback: OnDeleteTvShowClickCallback
    private var favMovieList: List<FavoriteMovie> = emptyList()
    private var favTvShowList: List<FavoriteTvShow> = emptyList()

    fun setOnMovieItemClickCallback(onFavMovieItemClickCallback: OnFavMovieItemClickCallback) {
        this.movieItemClickCallback = onFavMovieItemClickCallback
    }

    fun setOnDeleteMovieClickCallback(onDeleteMovieClickCallback: OnDeleteMovieClickCallback) {
        this.deleteMovieClickCallback = onDeleteMovieClickCallback
    }

    fun setOnTvShowItemClickCallback(onFavTvShowItemClickCallback: OnFavTvShowItemClickCallback) {
        this.tvshowItemClickCallback = onFavTvShowItemClickCallback
    }

    fun setOnDeleteTvShowClickCallback(onDeleteTvShowClickCallback: OnDeleteTvShowClickCallback) {
        this.deleteTvShowClickCallback = onDeleteTvShowClickCallback
    }

    fun setFavMoviesData(items: List<FavoriteMovie>) {
        favMovieList = items
        notifyDataSetChanged()
    }

    fun setFavTvShowsData(items: List<FavoriteTvShow>) {
        favTvShowList = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteListViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_in_rv, parent, false)
        return FavoriteListViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return if (index == 0) {
            favMovieList.size
        } else {
            favTvShowList.size
        }
    }

    override fun onBindViewHolder(holderFavorite: FavoriteListViewHolder, position: Int) {
        if(index == 0 && !favMovieList.isNullOrEmpty()) {
            holderFavorite.bindMovies(favMovieList[position])
            holderFavorite.itemView.setOnClickListener {
                movieItemClickCallback.onItemClicked(favMovieList[position])
            }
            holderFavorite.itemView.findViewById<ImageView>(R.id.btn_delete).setOnClickListener{
                deleteMovieClickCallback.onItemClicked(favMovieList[position])
            }
        } else if (index == 1 && !favTvShowList.isNullOrEmpty()) {
            holderFavorite.bindTvShow(favTvShowList[position])
            holderFavorite.itemView.setOnClickListener {
                tvshowItemClickCallback.onItemClicked(favTvShowList[position])
            }
            holderFavorite.itemView.findViewById<ImageView>(R.id.btn_delete).setOnClickListener{
                deleteTvShowClickCallback.onItemClicked(favTvShowList[position])
            }
        }
    }

    class FavoriteListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
        fun bindTvShow(tvshow: FavoriteTvShow) {
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