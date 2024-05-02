package com.example.submission5_androidexpert.viewmodel

import android.app.Application
import android.content.ComponentName
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.submission5_androidexpert.ConstantVariable.Companion.MOVIE
import com.example.submission5_androidexpert.ConstantVariable.Companion.TVSHOW
import com.example.submission5_androidexpert.R
import com.example.submission5_androidexpert.model.Detail
import com.example.submission5_androidexpert.repository.FavoriteRepository
import com.example.submission5_androidexpert.room.FavoriteMovie
import com.example.submission5_androidexpert.room.FavoriteRoomDatabase
import com.example.submission5_androidexpert.room.FavoriteTvShow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteViewModel(private val appContext: Application): AndroidViewModel(appContext) {
    private val favRepository: FavoriteRepository
    private val allFavMovies: LiveData<List<FavoriteMovie>>
    private val allFavTvShow: LiveData<List<FavoriteTvShow>>

    init {
        val favMoviesDao = FavoriteRoomDatabase.getDatabase(appContext).favMovieDao()
        val favTvShowDao = FavoriteRoomDatabase.getDatabase(appContext).favTvShowDao()
        favRepository = FavoriteRepository(favMoviesDao, favTvShowDao)
        allFavMovies = favRepository.allFavMovie
        allFavTvShow = favRepository.allFavTvShow
    }

    fun getAllFavMovies(): LiveData<List<FavoriteMovie>>{
        return allFavMovies
    }

    fun getAllFavTvShows(): LiveData<List<FavoriteTvShow>>{
        return allFavTvShow
    }

    private fun updateWidget(kindOfContent: String){

    }

    fun insertFavMovie(detail: Detail) = viewModelScope.launch(Dispatchers.IO) {
        val favMovie =
            FavoriteMovie(
                detail.id,
                detail.title,
                detail.overview,
                detail.posterPath,
                detail.voteAverage,
                detail.releaseDate
            )
        val insertMovieJob = async { favRepository.insertFavMovie(favMovie) }
        updateWidget(insertMovieJob.await())
    }

    fun insertFavTvShow(detail: Detail) = viewModelScope.launch(Dispatchers.IO) {
        val favTvShow =
            FavoriteTvShow(
                detail.id,
                detail.name,
                detail.overview,
                detail.posterPath,
                detail.voteAverage,
                detail.firstAirDate
            )
        val insertTvShowJob = async { favRepository.insertFavTvShow(favTvShow) }
        updateWidget(insertTvShowJob.await())
    }

    fun deleteFavMovie(favMovie: FavoriteMovie?, detail: Detail?)
            = viewModelScope.launch(Dispatchers.IO) {
        // The deletion come from Favorite Tab Fragment
        var mFavMovie = favMovie
        // The deletion came from Activity Detail. Because DAO need an object to delete a record
        // and Activity Detail have no Favorite Movie object, so the object has to be created
        if (detail != null){
            mFavMovie = FavoriteMovie(
                detail.id,
                detail.title,
                detail.overview,
                detail.posterPath,
                detail.voteAverage,
                detail.releaseDate
            )
        }
        mFavMovie?.let {
            val deleteMovieJob = async { favRepository.deleteFavMovie(it) }
            updateWidget(deleteMovieJob.await())
        }
    }

    fun deleteFavTvShow(favTvShow: FavoriteTvShow?, detail: Detail?)
            = viewModelScope.launch(Dispatchers.IO) {
        // The deletion come from Favorite Tab Fragment
        var mFavTvShow = favTvShow
        // The deletion came from Activity Detail. Because DAO need an object to delete a record
        // and Activity Detail have no Favorite Movie object, so the object has to be created
        if (detail != null){
            mFavTvShow = FavoriteTvShow(
                detail.id,
                detail.name,
                detail.overview,
                detail.posterPath,
                detail.voteAverage,
                detail.firstAirDate
            )
        }
        mFavTvShow?.let {
            val deleteTvShowJob = async { favRepository.deleteFavTvShow(it) }
            updateWidget(deleteTvShowJob.await())
        }
    }
}