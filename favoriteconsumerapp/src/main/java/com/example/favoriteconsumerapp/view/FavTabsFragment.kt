package com.example.favoriteconsumerapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favoriteconsumerapp.R
import com.example.favoriteconsumerapp.adapter.RecycleViewAdapter
import com.example.favoriteconsumerapp.viewmodel.Viewmodel
import kotlinx.android.synthetic.main.fragment_tabs.*
import kotlinx.android.synthetic.main.fragment_tabs.view.*

class FavTabsFragment: Fragment() {

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

    private lateinit var favMovieAdapter: RecycleViewAdapter
    private lateinit var favTvShowAdapter: RecycleViewAdapter
    private lateinit var viewModel: Viewmodel

    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tabs, container, false)
        viewModel = ViewModelProvider(this).get(Viewmodel::class.java)
        if (arguments != null) {
            val index = arguments?.getInt(FAV_ARG_SECTION_NUMBER, 0) as Int
            if (index == 0) {
                favMovieAdapter = RecycleViewAdapter(index)
                viewModel.getAllFavMovies()?.observe(viewLifecycleOwner, Observer { favMovies ->
                    if (!favMovies.isNullOrEmpty()){
                        favMovieAdapter.setFavMoviesData(favMovies)
                        progress_bar.visibility = View.INVISIBLE
                        txt_error_msg.visibility = View.INVISIBLE
                    } else {
                        progress_bar.visibility = View.INVISIBLE
                        txt_error_msg.visibility = View.VISIBLE
                        txt_error_msg.text = activity?.resources?.getString(R.string.fav_movie_empty)
                    }
                })
                view.recycle_view.adapter = favMovieAdapter
                view.recycle_view.layoutManager = LinearLayoutManager(view.context)
            } else {
                favTvShowAdapter = RecycleViewAdapter(index)
                viewModel.getAllFavTvShows()?.observe(viewLifecycleOwner, Observer { favTvShows ->
                    if (!favTvShows.isNullOrEmpty()){
                        favTvShowAdapter.setFavTvShowsData(favTvShows)
                        progress_bar.visibility = View.INVISIBLE
                        txt_error_msg.visibility = View.INVISIBLE
                    } else {
                        progress_bar.visibility = View.INVISIBLE
                        txt_error_msg.visibility = View.VISIBLE
                        txt_error_msg.text = activity?.resources?.getString(R.string.fav_tvshow_empty)
                    }
                })
                view.recycle_view.adapter = favTvShowAdapter
                view.recycle_view.layoutManager = LinearLayoutManager(view.context)
            }
        }
        return view
    }
}