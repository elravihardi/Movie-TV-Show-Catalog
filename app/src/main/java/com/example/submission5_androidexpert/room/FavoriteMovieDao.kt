package com.example.submission5_androidexpert.room

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteMovieDao {

    @Query("SELECT * from $MOVIE_TABLE_NAME")
    fun getAllFavMovie(): LiveData<List<FavoriteMovie>>

    @Query("SELECT * from $MOVIE_TABLE_NAME")
    fun selectAllFavMovie(): Cursor

    @Query("SELECT $MOVIE_ID FROM $MOVIE_TABLE_NAME WHERE $MOVIE_ID = :varId")
    fun getFavoriteStatusFromId(varId: Int): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavMovie(favMovie: FavoriteMovie)

    @Delete
    suspend fun deleteFavMovie(favMovie: FavoriteMovie)
}