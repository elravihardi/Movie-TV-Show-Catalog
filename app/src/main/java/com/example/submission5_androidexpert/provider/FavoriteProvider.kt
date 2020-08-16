package com.example.submission5_androidexpert.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.submission5_androidexpert.room.*

class FavoriteProvider : ContentProvider() {

    companion object {
        private const val FAVORITE_MOVIE = 1
        private const val FAVORITE_TVSHOW = 2
        const val AUTHORITY = "com.example.submission5_androidexpert.provider"
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        init {
            sUriMatcher.addURI(AUTHORITY,
                MOVIE_TABLE_NAME, FAVORITE_MOVIE)
            sUriMatcher.addURI(AUTHORITY,
                TVSHOW_TABLE_NAME, FAVORITE_TVSHOW)
        }
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        var cursor: Cursor? = null
        val context: Context? = context?.applicationContext

        if (context != null){
            if (sUriMatcher.match(uri) == FAVORITE_MOVIE){
                val favMoviesDao: FavoriteMovieDao = FavoriteRoomDatabase.getDatabase(context).favMovieDao()
                cursor = favMoviesDao.selectAllFavMovie()
            } else if (sUriMatcher.match(uri) == FAVORITE_TVSHOW){
                val favTvShowDao: FavoriteTvShowDao = FavoriteRoomDatabase.getDatabase(context).favTvShowDao()
                cursor = favTvShowDao.selectAllFavTvShow()
            }
        }
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return when (sUriMatcher.match(uri)) {
            FAVORITE_MOVIE -> "vnd.android.cursor.dir/$AUTHORITY.$MOVIE_TABLE_NAME"
            FAVORITE_TVSHOW -> "vnd.android.cursor.dir/$AUTHORITY.$TVSHOW_TABLE_NAME"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }
}
