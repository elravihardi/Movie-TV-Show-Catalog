package com.example.favoriteconsumerapp.viewmodel

import android.app.Application
import android.database.Cursor
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.favoriteconsumerapp.db.DatabaseContract.FavoriteMovieColumns.Companion.MOVIE_DATE
import com.example.favoriteconsumerapp.db.DatabaseContract.FavoriteMovieColumns.Companion.MOVIE_OVERVIEW
import com.example.favoriteconsumerapp.db.DatabaseContract.FavoriteMovieColumns.Companion.MOVIE_POSTER
import com.example.favoriteconsumerapp.db.DatabaseContract.FavoriteMovieColumns.Companion.MOVIE_TITLE
import com.example.favoriteconsumerapp.db.DatabaseContract.FavoriteMovieColumns.Companion.MOVIE_VOTE
import com.example.favoriteconsumerapp.db.DatabaseContract.FavoriteMovieColumns.Companion.URI_FAVORITE_MOVIE
import com.example.favoriteconsumerapp.db.DatabaseContract.FavoriteTvShowColumns.Companion.TVSHOW_DATE
import com.example.favoriteconsumerapp.db.DatabaseContract.FavoriteTvShowColumns.Companion.TVSHOW_NAME
import com.example.favoriteconsumerapp.db.DatabaseContract.FavoriteTvShowColumns.Companion.TVSHOW_OVERVIEW
import com.example.favoriteconsumerapp.db.DatabaseContract.FavoriteTvShowColumns.Companion.TVSHOW_POSTER
import com.example.favoriteconsumerapp.db.DatabaseContract.FavoriteTvShowColumns.Companion.TVSHOW_VOTE
import com.example.favoriteconsumerapp.db.DatabaseContract.FavoriteTvShowColumns.Companion.URI_FAVORITE_TVSHOW
import com.example.favoriteconsumerapp.helper.MappingHelper
import com.example.favoriteconsumerapp.model.FavoriteMovie
import com.example.favoriteconsumerapp.model.FavoriteTvShow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

//@SuppressLint("Recycle")
class Viewmodel(application: Application): AndroidViewModel(application) {

    private var allFavMovies: MutableLiveData<ArrayList<FavoriteMovie>>? = MutableLiveData()
    private var allFavTvShows: MutableLiveData<ArrayList<FavoriteTvShow>>? = MutableLiveData()

    init {
        loadFavMoviesAsync(application)
        loadFavTvShowsAsync(application)
    }

    fun getAllFavMovies(): LiveData<ArrayList<FavoriteMovie>>? {
        return allFavMovies
    }

    fun getAllFavTvShows(): LiveData<ArrayList<FavoriteTvShow>>?{
        return allFavTvShows
    }

    private fun loadFavMoviesAsync(app: Application){
        viewModelScope.launch(Dispatchers.IO) {
            val deferredFavMovies = async {
                val cursor: Cursor? = app.contentResolver.query(
                    URI_FAVORITE_MOVIE,
                    arrayOf(
                        MOVIE_TITLE,
                        MOVIE_OVERVIEW,
                        MOVIE_VOTE,
                        MOVIE_POSTER,
                        MOVIE_DATE
                    ),
                    null,
                    null,
                    null
                )
                MappingHelper.mapFavMoviesCursorToArrayList(cursor)
            }
            allFavMovies?.postValue(deferredFavMovies.await())
        }
    }

    private fun loadFavTvShowsAsync(app: Application){
        viewModelScope.launch(Dispatchers.IO) {
            val deferredFavTvShows = async {
                val cursor: Cursor? = app.contentResolver.query(
                    URI_FAVORITE_TVSHOW,
                    arrayOf(
                        TVSHOW_NAME,
                        TVSHOW_OVERVIEW,
                        TVSHOW_VOTE,
                        TVSHOW_POSTER,
                        TVSHOW_DATE
                    ),
                    null,
                    null,
                    null
                )
                MappingHelper.mapFavTvShowsCursorToArrayList(cursor)
            }
            allFavTvShows?.postValue(deferredFavTvShows.await())
        }
    }
}