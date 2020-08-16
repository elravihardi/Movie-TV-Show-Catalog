package com.example.submission5_androidexpert.retrofit

import com.example.submission5_androidexpert.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val baseURL = "https://api.themoviedb.org/3/"
const val posterURL = "https://image.tmdb.org/t/p/w185/"
const val backdropURL = "https://image.tmdb.org/t/p/w780/"

class TmdbApiClient {
    companion object {
        const val apiKey = BuildConfig.TMDB_API_KEY

        private val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(run {
                val httpLoggingInterceptor = HttpLoggingInterceptor()
                httpLoggingInterceptor.apply { httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY }
            })
            .build()

        fun getClient(): Retrofit {
            return Retrofit.Builder().baseUrl(baseURL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}
