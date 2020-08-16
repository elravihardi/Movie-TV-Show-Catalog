package com.example.favoriteconsumerapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class FavoriteMovie(
    val title: String?,
    val overview: String?,
    val posterPath: String?,
    val voteAverage: Float?,
    val releaseDate: String?
): Parcelable