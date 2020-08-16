package com.example.submission5_androidexpert.room

import android.provider.BaseColumns
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val TVSHOW_TABLE_NAME = "favorite_tvshow_table"
const val TVSHOW_ID = BaseColumns._ID

@Entity(tableName = TVSHOW_TABLE_NAME)
data class FavoriteTvShow(
    @PrimaryKey @ColumnInfo(name = TVSHOW_ID) val _ID: Int?,
    val name: String?,
    val overview: String?,
    @ColumnInfo(name = "poster_path") val posterPath: String?,
    @ColumnInfo(name = "vote_average") val voteAverage: Float?,
    @ColumnInfo(name = "first_air_date")val firstAirDate: String?
)