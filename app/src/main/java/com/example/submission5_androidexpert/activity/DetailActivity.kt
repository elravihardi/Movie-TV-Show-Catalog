package com.example.submission5_androidexpert.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.target.Target
import com.example.submission5_androidexpert.ConstantVariable.Companion.EXTRA_KIND_OF_CONTENT
import com.example.submission5_androidexpert.ConstantVariable.Companion.EXTRA_MOVIE_ID
import com.example.submission5_androidexpert.ConstantVariable.Companion.EXTRA_TVSHOW_ID
import com.example.submission5_androidexpert.ConstantVariable.Companion.MOVIE
import com.example.submission5_androidexpert.ConstantVariable.Companion.TVSHOW
import com.example.submission5_androidexpert.R
import com.example.submission5_androidexpert.databinding.ActivityDetailBinding
import com.example.submission5_androidexpert.model.Detail
import com.example.submission5_androidexpert.retrofit.backdropURL
import com.example.submission5_androidexpert.retrofit.posterURL
import com.example.submission5_androidexpert.viewmodel.DetailViewModel
import com.example.submission5_androidexpert.viewmodel.FavoriteViewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var detail: Detail
    private var movieID: Int? = null
    private var tvshowID: Int? = null
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        detailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        favoriteViewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title = null

        if (intent.getStringExtra(EXTRA_KIND_OF_CONTENT) == MOVIE) {
            movieID = intent.getIntExtra(EXTRA_MOVIE_ID, 0)
            detailViewModel.getMovieDetail(movieID).observe(this, contentDetailObserver)
            detailViewModel.favStatusOfMovie(movieID).observe(this, Observer { favStatusOfMovie ->
                if (favStatusOfMovie > 0) setBtnFavoriteOn()
            })
            detailViewModel.movieRequestError?.observe(this, Observer { errorCode ->
                if (errorCode != null) showErrorMessage(errorCode)
            })
        } else if (intent.getStringExtra(EXTRA_KIND_OF_CONTENT) == TVSHOW) {
            tvshowID = intent.getIntExtra(EXTRA_TVSHOW_ID, 0)
            detailViewModel.getTvShowDetail(tvshowID).observe(this, contentDetailObserver)
            detailViewModel.favStatusOfTvShow(tvshowID).observe(this, Observer { favStatusOfTVShow ->
                if (favStatusOfTVShow > 0) setBtnFavoriteOn()
            })
            detailViewModel.tvshowRequestError?.observe(this, Observer { errorCode ->
                if (errorCode != null) { showErrorMessage(errorCode) }
            })
        }
        binding.btnBack.setOnClickListener { finish() }
        binding.fabFav.setOnClickListener { setFavorite() }
    }

    private val contentDetailObserver = Observer<Detail> { detail ->
        if(detail != null){
            setItemsToVisible()
            this@DetailActivity.detail = detail
            with(detail){
                if(title != null) {
                    binding.txtTitle.text = String.format(
                        applicationContext.resources.getString(R.string.detail_title),
                        title)
                    if (!releaseDate.isNullOrBlank()) {
                        binding.txtYear.text = String.format(
                            applicationContext.resources.getString(R.string.list_year),
                            releaseDate.substring(0, 4))
                    }
                    if (!runtime.isNullOrBlank() && runtime != "0") {
                        binding.txtRuntime.text = String.format(
                            applicationContext.resources.getString(R.string.detail_runtime),
                            runtime)
                    }
                } else {
                    binding.txtTitle.text = String.format(
                        applicationContext.resources.getString(R.string.detail_title),
                        name)
                    if (!firstAirDate.isNullOrBlank()) {
                        binding.txtYear.text = String.format(
                            applicationContext.resources.getString(R.string.list_year),
                            firstAirDate.substring(0, 4))
                    }
                    binding.lblRuntime.text = applicationContext.resources.getString(R.string.lbl_episode_runtime)
                    episodeRuntime?.let {
                        if (episodeRuntime.size > 0) {
                            var mEpisodeRuntimes = ""
                            episodeRuntime.forEach{
                                mEpisodeRuntimes += "$it, "
                            }
                            binding.txtRuntime.text = String.format(
                                applicationContext.resources.getString(R.string.detail_runtime),
                                mEpisodeRuntimes.substringBeforeLast(','))
                        }
                    }
                }
                if (backdropPath.isNullOrBlank() && posterPath.isNullOrBlank())
                    binding.lblImgNotAvailable.visibility = View.VISIBLE
                if (!backdropPath.isNullOrBlank()) {
                    Glide.with(this@DetailActivity)
                        .load(backdropURL + backdropPath)
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .override(Target.SIZE_ORIGINAL)
                        .into(binding.imgBackDrop)
                } else {
                    Glide.with(this@DetailActivity)
                        .load(R.drawable.shape_img_not_available)
                        .into(binding.imgBackDrop)
                }

                if (!posterPath.isNullOrBlank()) {
                    Glide.with(this@DetailActivity)
                        .load(posterURL + posterPath)
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .override(Target.SIZE_ORIGINAL)
                        .into(binding.imgPoster)
                } else {
                    binding.imgPoster.visibility = View.INVISIBLE
                }
                if (voteAverage != 0f) {
                    binding.txtScore.text = String.format(
                        applicationContext.resources.getString(R.string.detail_vote),
                        voteAverage.toString())
                }
                if (!overview.isNullOrBlank()) binding.txtOverview.text = overview

                genres?.let {
                    if (genres.size > 0) {
                        var genre = ""
                        genres.forEach {
                            genre += "${it.name}, "
                        }
                        binding.txtGenre.text = String.format(
                            applicationContext.resources.getString(R.string.detail_genre),
                            genre.substringBeforeLast(','))
                    }
                }
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private fun setItemsToVisible(){
        binding.progressBar.visibility = View.INVISIBLE
        binding.icStar.visibility = View.VISIBLE
        binding.txtScore.visibility = View.VISIBLE
        binding.fabFav.visibility = View.VISIBLE
        binding.txtYear.visibility = View.VISIBLE
        binding.lblOverview.visibility = View.VISIBLE
        binding.lblRuntime.visibility = View.VISIBLE
        binding.lblGenre.visibility = View.VISIBLE
    }

    private fun showErrorMessage(errorCode: Int?) {
        binding.mainView.visibility = View.INVISIBLE
        when (errorCode) {
            1 -> Toast.makeText(this, getString(R.string.error_conn_message), Toast.LENGTH_LONG).show()
            401 -> Toast.makeText(this, getString(R.string.invalid_api_key), Toast.LENGTH_LONG).show()
            404 -> {
                when (detail.title){
                    null ->
                        Toast.makeText(this, getString(R.string.movie_error404_message), Toast.LENGTH_LONG).show()
                    else ->
                        Toast.makeText(this, getString(R.string.tvshow_error404_message), Toast.LENGTH_LONG).show()
                }

            }
        }
    }

    private fun setBtnFavoriteOn() {
        binding.fabFav.setImageResource(R.drawable.ic_favorite_fill_white_24dp)
        binding.fabFav.tag = "favorite_on"
    }

    private fun setBtnFavoriteOff() {
        binding.fabFav.setImageResource(R.drawable.ic_favorite_border_24dp)
        binding.fabFav.tag = "favorite_off"
    }

    private fun setFavorite() {
        if(binding.fabFav.tag.toString() == "favorite_off"){
            // The Favorite Button is Off
            setBtnFavoriteOn()
            if (detail.title != null) {
                // It's detail of Movie
                favoriteViewModel.insertFavMovie(detail)
                Toast.makeText(this, getString(R.string.insert_fav_movie_success), Toast.LENGTH_SHORT).show()
            } else {
                // It's detail of TV Show
                favoriteViewModel.insertFavTvShow(detail)
                Toast.makeText(this, getString(R.string.insert_fav_tvshow_success), Toast.LENGTH_SHORT).show()
            }
        } else {
            // The Favorite Button is On
            if (detail.title != null) {
                // It's detail of Movie
                val dialogBuilder = AlertDialog.Builder(this)
                dialogBuilder.setMessage(getString(R.string.fav_movie_delete_confirm))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.alert_proceed)) { _, _ ->
                        favoriteViewModel.deleteFavMovie(null, detail)
                        setBtnFavoriteOff()
                        Toast.makeText(this, getString(R.string.delete_fav_movie_success), Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton(getString(R.string.alert_cancel)) { dialog, _ -> dialog.cancel() }
                val alert = dialogBuilder.create()
                alert.setTitle(R.string.fav_movie_deletion_title)
                alert.show()
            } else {
                // It's detail of TV Show
                val dialogBuilder = AlertDialog.Builder(this)
                dialogBuilder.setMessage(getString(R.string.fav_tvshow_delete_confirm))
                    .setCancelable(false)
                    .setPositiveButton("Proceed") { _, _ ->
                        favoriteViewModel.deleteFavTvShow(null, detail)
                        setBtnFavoriteOff()
                        Toast.makeText(this, getString(R.string.delete_fav_tvshow_success), Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
                val alert = dialogBuilder.create()
                alert.setTitle(R.string.fav_tvshow_deletion_title)
                alert.show()
            }
        }
    }
}
