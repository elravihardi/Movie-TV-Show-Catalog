package com.example.favoriteconsumerapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class FavoriteTvShow(
    val name: String?,
    val overview: String?,
    val posterPath: String?,
    val voteAverage: Float?,
    val firstAirDate: String?
): Parcelable