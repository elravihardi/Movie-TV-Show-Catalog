package com.example.favoriteconsumerapp.helper

import android.database.Cursor
import com.example.favoriteconsumerapp.db.DatabaseContract.FavoriteMovieColumns.Companion.MOVIE_DATE
import com.example.favoriteconsumerapp.db.DatabaseContract.FavoriteMovieColumns.Companion.MOVIE_OVERVIEW
import com.example.favoriteconsumerapp.db.DatabaseContract.FavoriteMovieColumns.Companion.MOVIE_POSTER
import com.example.favoriteconsumerapp.db.DatabaseContract.FavoriteMovieColumns.Companion.MOVIE_TITLE
import com.example.favoriteconsumerapp.db.DatabaseContract.FavoriteMovieColumns.Companion.MOVIE_VOTE
import com.example.favoriteconsumerapp.db.DatabaseContract.FavoriteTvShowColumns.Companion.TVSHOW_DATE
import com.example.favoriteconsumerapp.db.DatabaseContract.FavoriteTvShowColumns.Companion.TVSHOW_NAME
import com.example.favoriteconsumerapp.db.DatabaseContract.FavoriteTvShowColumns.Companion.TVSHOW_OVERVIEW
import com.example.favoriteconsumerapp.db.DatabaseContract.FavoriteTvShowColumns.Companion.TVSHOW_POSTER
import com.example.favoriteconsumerapp.db.DatabaseContract.FavoriteTvShowColumns.Companion.TVSHOW_VOTE
import com.example.favoriteconsumerapp.model.FavoriteMovie
import com.example.favoriteconsumerapp.model.FavoriteTvShow
import java.util.*

object MappingHelper {

    fun mapFavMoviesCursorToArrayList(favMoviesCursor: Cursor?): ArrayList<FavoriteMovie> {
        val favMoviesList = ArrayList<FavoriteMovie>()
        if (favMoviesCursor != null){
            while (favMoviesCursor.moveToNext()) {
                val title = favMoviesCursor.getString(favMoviesCursor.getColumnIndexOrThrow(MOVIE_TITLE))
                val overview = favMoviesCursor.getString(favMoviesCursor.getColumnIndexOrThrow(MOVIE_OVERVIEW))
                val posterPath = favMoviesCursor.getString(favMoviesCursor.getColumnIndexOrThrow(MOVIE_POSTER))
                val voteAverage = favMoviesCursor.getFloat(favMoviesCursor.getColumnIndexOrThrow(MOVIE_VOTE))
                val releaseDate = favMoviesCursor.getString(favMoviesCursor.getColumnIndexOrThrow(MOVIE_DATE))
                favMoviesList.add(FavoriteMovie(title, overview, posterPath, voteAverage, releaseDate))
            }
        }
        return favMoviesList
    }

    fun mapFavTvShowsCursorToArrayList(favTvShowsCursor: Cursor?): ArrayList<FavoriteTvShow> {
        val favTvShowsList = ArrayList<FavoriteTvShow>()
        if (favTvShowsCursor != null){
            while (favTvShowsCursor.moveToNext()) {
                val name = favTvShowsCursor.getString(favTvShowsCursor.getColumnIndexOrThrow(TVSHOW_NAME))
                val overview = favTvShowsCursor.getString(favTvShowsCursor.getColumnIndexOrThrow(TVSHOW_OVERVIEW))
                val posterPath = favTvShowsCursor.getString(favTvShowsCursor.getColumnIndexOrThrow(TVSHOW_POSTER))
                val voteAverage = favTvShowsCursor.getFloat(favTvShowsCursor.getColumnIndexOrThrow(TVSHOW_VOTE))
                val firstAirDate = favTvShowsCursor.getString(favTvShowsCursor.getColumnIndexOrThrow(TVSHOW_DATE))
                favTvShowsList.add(FavoriteTvShow(name, overview, posterPath, voteAverage, firstAirDate))
            }
        }
        return favTvShowsList
    }
}
