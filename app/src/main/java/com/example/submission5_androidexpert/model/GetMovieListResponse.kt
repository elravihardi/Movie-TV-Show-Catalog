package com.example.submission5_androidexpert.model

data class GetMovieListResponse(
    var page: Int,
    val results: ArrayList<Movie>,
    val totalResult: Int,
    val totalPage: Int
)