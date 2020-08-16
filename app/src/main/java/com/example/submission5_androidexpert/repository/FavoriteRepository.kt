package com.example.submission5_androidexpert.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.submission5_androidexpert.ConstantVariable.Companion.MOVIE
import com.example.submission5_androidexpert.ConstantVariable.Companion.TVSHOW
import com.example.submission5_androidexpert.room.FavoriteMovie
import com.example.submission5_androidexpert.room.FavoriteMovieDao
import com.example.submission5_androidexpert.room.FavoriteTvShow
import com.example.submission5_androidexpert.room.FavoriteTvShowDao

class FavoriteRepository(private val favMovieDao: FavoriteMovieDao, private val favTvShowDao: FavoriteTvShowDao) {

    val allFavMovie: LiveData<List<FavoriteMovie>> = favMovieDao.getAllFavMovie()
    val allFavTvShow: LiveData<List<FavoriteTvShow>> = favTvShowDao.getAllFavTvShow()
    var favStatusOfMovie: MutableLiveData<Int>? = MutableLiveData()
    var favStatusOfTvShow: MutableLiveData<Int>? = MutableLiveData()

    fun favStatusOfMovie(id: Int?): MutableLiveData<Int>?{
        if (id !== null){
            favStatusOfMovie?.postValue(favMovieDao.getFavoriteStatusFromId(id))
        }
        return favStatusOfMovie
    }

    fun favStatusOfTvShow(id: Int?): MutableLiveData<Int>?{
        if (id !== null){
            favStatusOfTvShow?.postValue(favTvShowDao.getFavoriteStatusFromId(id))
        }
        return favStatusOfTvShow
    }

    suspend fun insertFavMovie(favoriteMovie: FavoriteMovie): String{
        favMovieDao.insertFavMovie(favoriteMovie)
        return MOVIE
    }

    suspend fun insertFavTvShow(favoriteTvShow: FavoriteTvShow): String{
        favTvShowDao.insertFavTvShow(favoriteTvShow)
        return TVSHOW
    }

    suspend fun deleteFavMovie(favoriteMovie: FavoriteMovie): String{
        favMovieDao.deleteFavMovie(favoriteMovie)
        return MOVIE
    }

    suspend fun deleteFavTvShow(favoriteTvShow: FavoriteTvShow): String{
        favTvShowDao.deleteFavTvShow(favoriteTvShow)
        return TVSHOW
    }
}