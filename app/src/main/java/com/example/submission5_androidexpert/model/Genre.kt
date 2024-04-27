package com.example.submission5_androidexpert.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Genre(
    val id: Int,
    val name: String
): Parcelable
