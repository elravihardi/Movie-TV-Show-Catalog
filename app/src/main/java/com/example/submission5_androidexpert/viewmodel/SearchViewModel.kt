package com.example.submission5_androidexpert.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submission5_androidexpert.model.Movie
import com.example.submission5_androidexpert.model.TvShow
import com.example.submission5_androidexpert.repository.MainRepository

class SearchViewModel: ViewModel() {

    private val repository: MainRepository = MainRepository()
    private var _movieSearchResult: MutableLiveData<ArrayList<Movie>>
    private var _tvshowSearchResult: MutableLiveData<ArrayList<TvShow>>
    private var _searchError: MutableLiveData<Int>?

    init {
        _movieSearchResult = repository.movieSearchResult
        _tvshowSearchResult = repository.tvshowSearchResult
        _searchError = repository.searchRequestError
    }

    fun searchMovie(query: String): LiveData<ArrayList<Movie>> {
        if (_movieSearchResult.value.isNullOrEmpty())
            repository.searchMovie(query)
        return _movieSearchResult
    }

    fun searchTvShow(query: String): LiveData<ArrayList<TvShow>> {
        if (_tvshowSearchResult.value.isNullOrEmpty())
            repository.searchTvShow(query)
        return _tvshowSearchResult
    }

    // Reset livedata when user leave Search Fragment or refresh the page
    fun resetLiveData() {
        _movieSearchResult.value = null
        _tvshowSearchResult.value = null
        _searchError?.value = null
    }

    val searchError: LiveData<Int>?
        get() = _searchError
}