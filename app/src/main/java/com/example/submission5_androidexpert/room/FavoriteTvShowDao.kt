package com.example.submission5_androidexpert.room

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteTvShowDao {

    @Query("SELECT * from $TVSHOW_TABLE_NAME")
    fun getAllFavTvShow(): LiveData<List<FavoriteTvShow>>

    @Query("SELECT * from $TVSHOW_TABLE_NAME")
    fun selectAllFavTvShow(): Cursor

    @Query("SELECT $TVSHOW_ID FROM $TVSHOW_TABLE_NAME WHERE $TVSHOW_ID = :varId")
    fun getFavoriteStatusFromId(varId: Int): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavTvShow(favTvShow: FavoriteTvShow)

    @Delete
    suspend fun deleteFavTvShow(favTvShow: FavoriteTvShow)
}