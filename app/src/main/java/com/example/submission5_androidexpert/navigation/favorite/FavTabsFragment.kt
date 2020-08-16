package com.example.submission5_androidexpert.navigation.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission5_androidexpert.ConstantVariable.Companion.EXTRA_KIND_OF_CONTENT
import com.example.submission5_androidexpert.ConstantVariable.Companion.EXTRA_MOVIE_ID
import com.example.submission5_androidexpert.ConstantVariable.Companion.EXTRA_TVSHOW_ID
import com.example.submission5_androidexpert.ConstantVariable.Companion.MOVIE
import com.example.submission5_androidexpert.ConstantVariable.Companion.TVSHOW
import com.example.submission5_androidexpert.R
import com.example.submission5_androidexpert.activity.DetailActivity
import com.example.submission5_androidexpert.adapter.*
import com.example.submission5_androidexpert.room.FavoriteMovie
import com.example.submission5_androidexpert.room.FavoriteTvShow
import com.example.submission5_androidexpert.viewmodel.FavoriteViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*

class FavTabsFragment: Fragment() {

    private var favoriteRecycleViewAdapter: FavoriteRecycleViewAdapter? = null
    private val favoriteViewModel: FavoriteViewModel by activityViewModels()

    companion object {
        private const val FAV_ARG_SECTION_NUMBER = "section_number"
        fun newFavInstance (index: Int): FavTabsFragment {
            val fragment = FavTabsFragment()
            val bundle = Bundle()
            bundle.putInt(FAV_ARG_SECTION_NUMBER, index)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        view.swipeContainer.setOnRefreshListener {
            view.swipeContainer.isRefreshing = false
        }
        view.swipeContainer.setColorSchemeResources(R.color.colorAccent, R.color.colorGray)
        if (arguments != null) {
            val index = arguments?.getInt(FAV_ARG_SECTION_NUMBER, 0) as Int
            if (index == 0) {
                favoriteRecycleViewAdapter = FavoriteRecycleViewAdapter(index)
                favoriteViewModel.getAllFavMovies().observe(viewLifecycleOwner, favMoviesObserver)
                view.recycle_view.adapter = favoriteRecycleViewAdapter
                view.recycle_view.layoutManager = LinearLayoutManager(view.context)
            } else {
                favoriteViewModel.getAllFavTvShows().observe(viewLifecycleOwner, favTvShowsObserver)
                favoriteRecycleViewAdapter = FavoriteRecycleViewAdapter(index)
                view.recycle_view.adapter = favoriteRecycleViewAdapter
                view.recycle_view.layoutManager = LinearLayoutManager(view.context)
            }
        }
        return view
    }

    private val favMoviesObserver = Observer<List<FavoriteMovie>> { favMovieList ->
        favoriteRecycleViewAdapter?.setFavMoviesData(favMovieList)
        if (!favMovieList.isNullOrEmpty()) {
            progress_bar.visibility = View.INVISIBLE
            txt_error_message.visibility = View.INVISIBLE
        } else {
            progress_bar.visibility = View.INVISIBLE
            txt_error_message.text = activity?.resources?.getString(R.string.fav_movie_empty)
            txt_error_message.visibility = View.VISIBLE
        }
    }

    private val favTvShowsObserver = Observer<List<FavoriteTvShow>> { favTvShowList ->
        favoriteRecycleViewAdapter?.setFavTvShowsData(favTvShowList)
        if (!favTvShowList.isNullOrEmpty()) {
            progress_bar.visibility = View.INVISIBLE
            txt_error_message.visibility = View.INVISIBLE
        } else {
            progress_bar.visibility = View.INVISIBLE
            txt_error_message.text = activity?.resources?.getString(R.string.fav_tvshow_empty)
            txt_error_message.visibility = View.VISIBLE
        }
    }

    override fun onViewCreated (view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            val index = arguments?.getInt(FAV_ARG_SECTION_NUMBER, 0) as Int
            if (index == 0) {
                // It's fragment for Fav Movie
                favoriteRecycleViewAdapter?.setOnMovieItemClickCallback(object: OnFavMovieItemClickCallback {
                    override fun onItemClicked(favMovie: FavoriteMovie) {
                        val intentToDetailActivity = Intent(view.context, DetailActivity::class.java)
                        intentToDetailActivity.putExtra(EXTRA_MOVIE_ID, favMovie._ID)
                        intentToDetailActivity.putExtra(EXTRA_KIND_OF_CONTENT, MOVIE)
                        startActivity(intentToDetailActivity)
                    }
                })
                favoriteRecycleViewAdapter?.setOnDeleteMovieClickCallback(object: OnDeleteMovieClickCallback {
                    override fun onItemClicked(favMovie: FavoriteMovie) {
                        val dialogBuilder = AlertDialog.Builder(view.context)
                        dialogBuilder.setMessage(getString(R.string.fav_movie_delete_confirm))
                            .setCancelable(false)
                            .setPositiveButton("Proceed") { _, _ ->
                                favoriteViewModel.deleteFavMovie(favMovie, null)
                                Toast.makeText(view.context, getString(R.string.delete_fav_movie_success),
                                    Toast.LENGTH_SHORT).show()
                            }
                            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
                        val alert = dialogBuilder.create()
                        alert.setTitle(getString(R.string.fav_movie_deletion_title))
                        alert.show()
                    }
                })
            } else {
                // It's fragment for Fav TV Show
                favoriteRecycleViewAdapter?.setOnTvShowItemClickCallback(object: OnFavTvShowItemClickCallback {
                    override fun onItemClicked(favTvShow: FavoriteTvShow) {
                        val intentToDetailActivity = Intent(view.context, DetailActivity::class.java)
                        intentToDetailActivity.putExtra(EXTRA_TVSHOW_ID, favTvShow._ID)
                        intentToDetailActivity.putExtra(EXTRA_KIND_OF_CONTENT, TVSHOW)
                        startActivity(intentToDetailActivity)
                    }
                })
                favoriteRecycleViewAdapter?.setOnDeleteTvShowClickCallback(object: OnDeleteTvShowClickCallback {
                    override fun onItemClicked(favTvShow: FavoriteTvShow) {
                        val dialogBuilder = AlertDialog.Builder(view.context)
                        dialogBuilder.setMessage(getString(R.string.fav_tvshow_delete_confirm))
                            .setCancelable(false)
                            .setPositiveButton(R.string.alert_proceed) { _, _ ->
                                favoriteViewModel.deleteFavTvShow(favTvShow, null)
                                Toast.makeText(view.context, getString(R.string.delete_fav_tvshow_success),
                                    Toast.LENGTH_SHORT).show()
                            }
                            .setNegativeButton(R.string.alert_cancel) { dialog, _ -> dialog.cancel() }
                        val alert = dialogBuilder.create()
                        alert.setTitle(getString(R.string.fav_tvshow_deletion_title))
                        alert.show()
                    }
                })
            }
        }
    }
}