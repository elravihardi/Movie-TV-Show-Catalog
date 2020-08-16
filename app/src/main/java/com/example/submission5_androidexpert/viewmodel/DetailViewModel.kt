package com.example.submission5_androidexpert.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.submission5_androidexpert.model.Detail
import com.example.submission5_androidexpert.repository.FavoriteRepository
import com.example.submission5_androidexpert.repository.MainRepository
import com.example.submission5_androidexpert.room.FavoriteRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailViewModel(application: Application): AndroidViewModel(application) {

    private val mainRepository: MainRepository = MainRepository()
    private val favRepository: FavoriteRepository
    private var movieLiveData: LiveData<Detail>? = null
    private var tvshowLiveData: LiveData<Detail>? = null
    private var _movieRequestError: LiveData<Int>? = null
    private var _tvshowRequestError: LiveData<Int>? = null
    private var favStatusOfMovie: LiveData<Int>? = null
    private var favStatusOfTvShow: LiveData<Int>? = null

    init {
        _movieRequestError = mainRepository.movieDetailRequestError
        _tvshowRequestError = mainRepository.tvshowDetailRequestError
        val favMoviesDao = FavoriteRoomDatabase.getDatabase(application).favMovieDao()
        val favTvShowDao = FavoriteRoomDatabase.getDatabase(application).favTvShowDao()
        favRepository = FavoriteRepository(favMoviesDao, favTvShowDao)
        favStatusOfMovie = favRepository.favStatusOfMovie
        favStatusOfTvShow = favRepository.favStatusOfTvShow
    }

    val movieRequestError: LiveData<Int>?
        get() = _movieRequestError

    val tvshowRequestError: LiveData<Int>?
        get() = _tvshowRequestError

    fun favStatusOfMovie(id: Int?): LiveData<Int> {
        viewModelScope.launch(Dispatchers.IO) {
            val checkFavStatus = async { favRepository.favStatusOfMovie(id) }
            withContext(Dispatchers.Main){
                favStatusOfMovie = checkFavStatus.await()
            }
        }
        return favStatusOfMovie as LiveData<Int>
    }

    fun favStatusOfTvShow(id: Int?): LiveData<Int> {
        viewModelScope.launch(Dispatchers.IO) {
            val checkFavStatus = async { favRepository.favStatusOfTvShow(id) }
            withContext(Dispatchers.Main){
                favStatusOfTvShow = checkFavStatus.await()
            }
        }
        return favStatusOfTvShow as LiveData<Int>
    }

    fun getMovieDetail(id: Int?): LiveData<Detail> {
        if (movieLiveData == null && id != null) {
            mainRepository.loadMovieDetail(id)
            movieLiveData = mainRepository.movieDetail
        }
        return movieLiveData as LiveData<Detail>
    }

    fun getTvShowDetail(id: Int?): LiveData<Detail> {
        if (tvshowLiveData == null && id != null) {
            mainRepository.loadTvShowDetail(id)
            tvshowLiveData = mainRepository.tvshowDetail
        }
        return tvshowLiveData as LiveData<Detail>
    }
}