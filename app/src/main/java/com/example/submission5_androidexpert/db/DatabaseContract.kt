package com.example.submission5_androidexpert.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.example.submission5_androidexpert.provider"

    class FavoriteMovieColumns {
        companion object {
            private const val MOVIE_TABLE_NAME = "favorite_movie_table"
            const val MOVIE_ID = BaseColumns._ID
            const val MOVIE_TITLE = "title"
            const val MOVIE_OVERVIEW = "overview"
            const val MOVIE_POSTER = "poster_path"
            const val MOVIE_VOTE = "vote_average"
            const val MOVIE_DATE = "release_date"

            val URI_FAVORITE_MOVIE: Uri = Uri.parse("content://$AUTHORITY/$MOVIE_TABLE_NAME")
        }
    }

    class FavoriteTvShowColumns {
        companion object {
            private const val TVSHOW_TABLE_NAME = "favorite_tvshow_table"
            const val TVSHOW_ID = BaseColumns._ID
            const val TVSHOW_NAME = "name"
            const val TVSHOW_OVERVIEW = "overview"
            const val TVSHOW_POSTER = "poster_path"
            const val TVSHOW_VOTE = "vote_average"
            const val TVSHOW_DATE = "first_air_date"

            val URI_FAVORITE_TVSHOW: Uri = Uri.parse("content://$AUTHORITY/$TVSHOW_TABLE_NAME")
        }
    }
}
