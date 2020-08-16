package com.example.submission5_androidexpert.room

import android.provider.BaseColumns
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val MOVIE_TABLE_NAME = "favorite_movie_table"
const val MOVIE_ID = BaseColumns._ID

@Entity(tableName = MOVIE_TABLE_NAME)
data class FavoriteMovie(
    @PrimaryKey @ColumnInfo(name = MOVIE_ID) val _ID: Int?,
    val title: String?,
    val overview: String?,
    @ColumnInfo(name = "poster_path") val posterPath: String?,
    @ColumnInfo(name = "vote_average") val voteAverage: Float?,
    @ColumnInfo(name = "release_date")val releaseDate: String?
)