package com.example.submission5_androidexpert.model

data class GetTvShowListResponse(
    var page: Int,
    val results: ArrayList<TvShow>,
    val totalResult: Int,
    val totalPage: Int
)