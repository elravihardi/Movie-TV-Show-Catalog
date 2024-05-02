package com.example.submission5_androidexpert.viewmodel

import android.app.Application
import android.os.Parcelable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.submission5_androidexpert.model.Movie
import com.example.submission5_androidexpert.model.TvShow
import com.example.submission5_androidexpert.repository.MainRepository

class MainViewModel(app: Application): AndroidViewModel(app) {

    private val repository: MainRepository = MainRepository()
    private val _movieList: MutableLiveData<ArrayList<Movie>>
    private val _tvshowList: MutableLiveData<ArrayList<TvShow>>
    private val _releaseMovieList: MutableLiveData<ArrayList<Movie>>
    private var _movieRequestError: MutableLiveData<Int>?
    private var _tvshowRequestError: MutableLiveData<Int>?
    private var _releaseMovieRequestError: MutableLiveData<Int>?
    // To keep the recycler view's scroll position when destination changed
    private var _movieLayoutManagerState: MutableLiveData<Parcelable>? = MutableLiveData()
    private var _tvshowLayoutManagerState: MutableLiveData<Parcelable>? = MutableLiveData()
    private var _releaseLayoutManagerState: MutableLiveData<Parcelable>? = MutableLiveData()

    init {
        repository.loadMovieList("en")
        _movieList = repository.movieList
        _tvshowList = repository.tvshowList
        _releaseMovieList = repository.releaseMovieList
        _movieRequestError = repository.movieRequestError
        _tvshowRequestError = repository.tvshowRequestError
        _releaseMovieRequestError = repository.releaseMovieRequestError
    }

    fun getMovieList(): LiveData<ArrayList<Movie>> {
        if (_movieList.value.isNullOrEmpty())
            repository.loadMovieList("en")
        return _movieList
    }

    fun getTvShowList(): LiveData<ArrayList<TvShow>> {
        if (_tvshowList.value.isNullOrEmpty())
            repository.loadTvShowsList("en")
        return _tvshowList
    }

    fun getReleaseMovieList(): LiveData<ArrayList<Movie>> {
        if (_releaseMovieList.value.isNullOrEmpty())
            repository.loadMovieReleaseList()
        return _releaseMovieList
    }

    fun setMovieLayoutManagerState(state: Parcelable?) {
        _movieLayoutManagerState?.value = state
    }

    fun setTvShowLayoutManagerState(state: Parcelable?) {
        _tvshowLayoutManagerState?.value = state
    }

    fun setReleaseLayoutManagerState(state: Parcelable?) {
        _releaseLayoutManagerState?.value = state
    }

    fun resetMovieLiveData() {
        _movieList.value = null
        _movieRequestError?.value = null
        _movieLayoutManagerState?.value = null
    }

    fun resetTvShowLiveData() {
        _tvshowList.value = null
        _tvshowRequestError?.value = null
        _tvshowLayoutManagerState?.value = null
    }

    fun resetReleaseLiveData() {
        _releaseMovieList.value = null
        _releaseMovieRequestError?.value = null
        _releaseLayoutManagerState?.value = null
    }

    val movieRequestError: LiveData<Int>?
        get() = _movieRequestError

    val tvshowRequestError: LiveData<Int>?
        get() = _tvshowRequestError

    val releaseMovieRequestError: LiveData<Int>?
        get() = _releaseMovieRequestError

    val movieLayoutManagerState: LiveData<Parcelable>?
        get() = _movieLayoutManagerState

    val tvshowLayoutManagerState: LiveData<Parcelable>?
        get() = _tvshowLayoutManagerState

    val releaseLayoutManagerState: LiveData<Parcelable>?
        get() = _releaseLayoutManagerState
}