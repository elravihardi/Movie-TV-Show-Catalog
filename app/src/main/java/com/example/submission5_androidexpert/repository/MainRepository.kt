package com.example.submission5_androidexpert.repository

import androidx.lifecycle.MutableLiveData
import com.example.submission5_androidexpert.model.*
import com.example.submission5_androidexpert.retrofit.TmdbApiClient
import com.example.submission5_androidexpert.retrofit.TmdbApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainRepository {
    private val tmdbApi: TmdbApiInterface = TmdbApiClient.getClient().create(TmdbApiInterface::class.java)

    // For MainViewModel
    var movieList: MutableLiveData<ArrayList<Movie>> = MutableLiveData()
    var tvshowList: MutableLiveData<ArrayList<TvShow>> = MutableLiveData()
    var releaseMovieList: MutableLiveData<ArrayList<Movie>> = MutableLiveData()
    var movieSearchResult: MutableLiveData<ArrayList<Movie>> = MutableLiveData()
    var tvshowSearchResult: MutableLiveData<ArrayList<TvShow>> = MutableLiveData()
    var movieRequestError: MutableLiveData<Int>? = MutableLiveData()
    var tvshowRequestError: MutableLiveData<Int>? = MutableLiveData()
    var releaseMovieRequestError: MutableLiveData<Int>? = MutableLiveData()
    var searchRequestError: MutableLiveData<Int>? = MutableLiveData()

    // For DetailViewModel
    var movieDetail: MutableLiveData<Detail>? = MutableLiveData()
    var tvshowDetail: MutableLiveData<Detail>? = MutableLiveData()
    var movieDetailRequestError: MutableLiveData<Int>? = MutableLiveData()
    var tvshowDetailRequestError: MutableLiveData<Int>? = MutableLiveData()

    fun loadMovieList(language: String) {
        val movieListCall: Call<GetMovieListResponse> = tmdbApi.getMovieList(TmdbApiClient.apiKey, language)
        val movieList: ArrayList<Movie> = ArrayList()
        movieListCall.enqueue(
            object: Callback<GetMovieListResponse> {
                override fun onFailure(call: Call<GetMovieListResponse>?, t: Throwable?) {
                    movieRequestError?.postValue(1)
                }

                override fun onResponse(call: Call<GetMovieListResponse>?, response: Response<GetMovieListResponse>?) {
                    val body = response?.body()
                    if (body != null && body.results.isNotEmpty()) {
                        for (i in body.results.iterator()) {
                            movieList.add(
                                Movie(i.id, i.title, i.overview, i.posterPath, i.voteAverage, i.releaseDate)
                            )
                        }
                        this@MainRepository.movieList.postValue(movieList)
                    } else{
                        movieRequestError?.postValue(response?.code())
                    }
                }
            }
        )
    }

    fun loadTvShowsList(language: String) {
        val tvshowListCall: Call<GetTvShowListResponse> = tmdbApi.getTvShowList(TmdbApiClient.apiKey, language)
        val tvshowList: ArrayList<TvShow> = ArrayList()
        tvshowListCall.enqueue(
            object: Callback<GetTvShowListResponse> {
                override fun onFailure(call: Call<GetTvShowListResponse>?, t: Throwable?) {
                    tvshowRequestError?.postValue(1)
                }

                override fun onResponse(call: Call<GetTvShowListResponse>?, response: Response<GetTvShowListResponse>?) {
                    val body = response?.body()
                    if (body != null && body.results.isNotEmpty()) {
                        for (i in body.results.iterator()) {
                            tvshowList.add(
                                TvShow(i.id, i.name, i.overview, i.posterPath, i.voteAverage, i.firstAirDate)
                            )
                        }
                        this@MainRepository.tvshowList.postValue(tvshowList)
                    } else{
                        tvshowRequestError?.postValue(response?.code())
                    }
                }
            }
        )
    }

    fun loadMovieReleaseList() {
        val todayDate = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val movieListCall: Call<GetMovieListResponse> = tmdbApi.getMovieReleaseToday(
            TmdbApiClient.apiKey,
            dateFormat.format(todayDate.time),
            dateFormat.format(todayDate.time)
        )
        val movieList: ArrayList<Movie> = ArrayList()
        movieListCall.enqueue(
            object: Callback<GetMovieListResponse> {
                override fun onFailure(call: Call<GetMovieListResponse>?, t: Throwable?) {
                    releaseMovieRequestError?.postValue(1)
                }
                override fun onResponse(call: Call<GetMovieListResponse>?, response: Response<GetMovieListResponse>?) {
                    val body = response?.body()
                    if (body != null && body.results.isNotEmpty()) {
                        for (i in body.results.iterator()) {
                            movieList.add(
                                Movie(i.id, i.title, i.overview, i.posterPath, i.voteAverage, i.releaseDate)
                            )
                        }
                        movieList.sortBy { it.title }
                        releaseMovieList.postValue(movieList)
                    } else {
                        releaseMovieRequestError?.postValue(response?.code())
                    }
                }
            }
        )
    }

    fun searchMovie(query: String) {
        val movieListCall: Call<GetMovieListResponse> = tmdbApi.searchMovie(
            TmdbApiClient.apiKey,
            "en-US",
            query
        )
        val movieList: ArrayList<Movie> = ArrayList()
        movieListCall.enqueue(
            object: Callback<GetMovieListResponse> {
                override fun onFailure(call: Call<GetMovieListResponse>?, t: Throwable?) {
                    searchRequestError?.value = 1
                }

                override fun onResponse(call: Call<GetMovieListResponse>?, response: Response<GetMovieListResponse>?) {
                    val body = response?.body()
                    if (body != null && body.results.isNotEmpty()) {
                        for (i in body.results.iterator()) {
                            movieList.add(
                                Movie(i.id, i.title, i.overview, i.posterPath, i.voteAverage, i.releaseDate)
                            )
                        }
                        movieSearchResult.postValue(movieList)
                    } else{
                        searchRequestError?.postValue(response?.code())
                    }
                }
            }
        )
    }

    fun searchTvShow(query: String) {
        val tvshowListCall: Call<GetTvShowListResponse> = tmdbApi.searchTvShow(
            TmdbApiClient.apiKey,
            "en-US",
            query
        )
        val tvshowList: ArrayList<TvShow> = ArrayList()
        tvshowListCall.enqueue(
            object: Callback<GetTvShowListResponse> {
                override fun onFailure(call: Call<GetTvShowListResponse>?, t: Throwable?) {
                    searchRequestError?.postValue(1)
                }

                override fun onResponse(call: Call<GetTvShowListResponse>?, response: Response<GetTvShowListResponse>?) {
                    val body = response?.body()
                    if (body != null && body.results.isNotEmpty()) {
                        for (i in body.results.iterator()) {
                            tvshowList.add(
                                TvShow(i.id, i.name, i.overview, i.posterPath, i.voteAverage, i.firstAirDate)
                            )
                        }
                        tvshowSearchResult.postValue(tvshowList)
                    } else{
                        searchRequestError?.postValue(response?.code())
                    }
                }
            }
        )
    }

    fun loadMovieDetail(id: Int?) {
        val movieDetailCall: Call<Detail> = tmdbApi.getMovieDetail(id, TmdbApiClient.apiKey)
        movieDetailCall.enqueue(object : Callback<Detail> {
            override fun onFailure(call: Call<Detail>?, t: Throwable?) {
                movieDetailRequestError?.postValue(1)
            }
            override fun onResponse(call: Call<Detail>?, response: Response<Detail>?) {
                val body = response?.body()
                val movieDetail: Detail?
                if (body != null) {
                    movieDetail = Detail(
                        body.id,
                        body.title,
                        null,
                        body.overview,
                        body.posterPath,
                        body.backdropPath,
                        body.releaseDate,
                        null,
                        body.voteAverage,
                        body.runtime,
                        null,
                        body.genres)
                    this@MainRepository.movieDetail?.postValue(movieDetail)
                } else{
                    movieDetailRequestError?.postValue(response?.code())
                }
            }
        })
    }

    fun loadTvShowDetail(id: Int?) {
        val tvshowDetailCall: Call<Detail> = tmdbApi.getTvShowDetail(id, TmdbApiClient.apiKey)
        tvshowDetailCall.enqueue(object : Callback<Detail> {
            override fun onFailure(call: Call<Detail>?, t: Throwable?) {
                tvshowDetailRequestError?.postValue(1)
            }

            override fun onResponse(call: Call<Detail>?, response: Response<Detail>?) {
                val body = response?.body()
                val tvshowDetail: Detail?
                if (body != null) {
                    tvshowDetail = Detail(
                        body.id,
                        null,
                        body.name,
                        body.overview,
                        body.posterPath,
                        body.backdropPath,
                        null,
                        body.firstAirDate,
                        body.voteAverage,
                        null,
                        body.episodeRuntime,
                        body.genres)
                    this@MainRepository.tvshowDetail?.postValue(tvshowDetail)
                } else{
                    tvshowDetailRequestError?.postValue(response?.code())
                }
            }
        })
    }
}