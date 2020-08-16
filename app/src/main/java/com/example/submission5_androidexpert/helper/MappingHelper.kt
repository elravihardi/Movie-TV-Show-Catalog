package com.example.submission5_androidexpert.helper

import android.database.Cursor
import com.example.submission5_androidexpert.db.DatabaseContract.FavoriteMovieColumns.Companion.MOVIE_DATE
import com.example.submission5_androidexpert.db.DatabaseContract.FavoriteMovieColumns.Companion.MOVIE_ID
import com.example.submission5_androidexpert.db.DatabaseContract.FavoriteMovieColumns.Companion.MOVIE_OVERVIEW
import com.example.submission5_androidexpert.db.DatabaseContract.FavoriteMovieColumns.Companion.MOVIE_POSTER
import com.example.submission5_androidexpert.db.DatabaseContract.FavoriteMovieColumns.Companion.MOVIE_TITLE
import com.example.submission5_androidexpert.db.DatabaseContract.FavoriteMovieColumns.Companion.MOVIE_VOTE
import com.example.submission5_androidexpert.db.DatabaseContract.FavoriteTvShowColumns.Companion.TVSHOW_DATE
import com.example.submission5_androidexpert.db.DatabaseContract.FavoriteTvShowColumns.Companion.TVSHOW_ID
import com.example.submission5_androidexpert.db.DatabaseContract.FavoriteTvShowColumns.Companion.TVSHOW_NAME
import com.example.submission5_androidexpert.db.DatabaseContract.FavoriteTvShowColumns.Companion.TVSHOW_OVERVIEW
import com.example.submission5_androidexpert.db.DatabaseContract.FavoriteTvShowColumns.Companion.TVSHOW_POSTER
import com.example.submission5_androidexpert.db.DatabaseContract.FavoriteTvShowColumns.Companion.TVSHOW_VOTE
import com.example.submission5_androidexpert.room.FavoriteMovie
import com.example.submission5_androidexpert.room.FavoriteTvShow
import java.util.*

object MappingHelper {

    fun mapFavMoviesCursorToArrayList(favMoviesCursor: Cursor?): ArrayList<FavoriteMovie> {
        val favMovieList = ArrayList<FavoriteMovie>()
        if (favMoviesCursor != null){
            while (favMoviesCursor.moveToNext()) {
                val id = favMoviesCursor.getInt(favMoviesCursor.getColumnIndexOrThrow(MOVIE_ID))
                val title = favMoviesCursor.getString(favMoviesCursor.getColumnIndexOrThrow(MOVIE_TITLE))
                val overview = favMoviesCursor.getString(favMoviesCursor.getColumnIndexOrThrow(MOVIE_OVERVIEW))
                val posterPath = favMoviesCursor.getString(favMoviesCursor.getColumnIndexOrThrow(MOVIE_POSTER))
                val voteAverage = favMoviesCursor.getFloat(favMoviesCursor.getColumnIndexOrThrow(MOVIE_VOTE))
                val releaseDate = favMoviesCursor.getString(favMoviesCursor.getColumnIndexOrThrow(MOVIE_DATE))
                favMovieList.add(
                    FavoriteMovie(
                        id,
                        title,
                        overview,
                        posterPath,
                        voteAverage,
                        releaseDate
                    )
                )
            }
        }
        return favMovieList
    }

    fun mapFavTvShowsCursorToArrayList(favTvShowsCursor: Cursor?): ArrayList<FavoriteTvShow> {
        val favTvShowList = ArrayList<FavoriteTvShow>()
        if (favTvShowsCursor != null){
            while (favTvShowsCursor.moveToNext()) {
                val id = favTvShowsCursor.getInt(favTvShowsCursor.getColumnIndexOrThrow(TVSHOW_ID))
                val name = favTvShowsCursor.getString(favTvShowsCursor.getColumnIndexOrThrow(TVSHOW_NAME))
                val overview = favTvShowsCursor.getString(favTvShowsCursor.getColumnIndexOrThrow(TVSHOW_OVERVIEW))
                val posterPath = favTvShowsCursor.getString(favTvShowsCursor.getColumnIndexOrThrow(TVSHOW_POSTER))
                val voteAverage = favTvShowsCursor.getFloat(favTvShowsCursor.getColumnIndexOrThrow(TVSHOW_VOTE))
                val firstAirDate = favTvShowsCursor.getString(favTvShowsCursor.getColumnIndexOrThrow(TVSHOW_DATE))
                favTvShowList.add(
                    FavoriteTvShow(
                        id,
                        name,
                        overview,
                        posterPath,
                        voteAverage,
                        firstAirDate
                    )
                )
            }
        }
        return favTvShowList
    }
}
