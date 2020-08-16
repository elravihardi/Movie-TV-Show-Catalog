package com.example.submission5_androidexpert.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteMovie::class, FavoriteTvShow::class], version = 1, exportSchema = false)
abstract class FavoriteRoomDatabase: RoomDatabase() {

    abstract fun favMovieDao(): FavoriteMovieDao
    abstract fun favTvShowDao(): FavoriteTvShowDao

    companion object {
        @Volatile
        private var INSTANCE: FavoriteRoomDatabase? = null

        fun getDatabase(context: Context): FavoriteRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteRoomDatabase::class.java,
                    "favorite_database")
                    .addCallback(FavoriteDatabaseCallback())
                    .build()
                INSTANCE = instance
                return instance
            }
        }

        private class FavoriteDatabaseCallback : RoomDatabase.Callback()
    }
}