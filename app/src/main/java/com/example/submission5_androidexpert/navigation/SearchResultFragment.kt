package com.example.submission5_androidexpert.navigation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission5_androidexpert.ConstantVariable.Companion.EXTRA_KIND_OF_CONTENT
import com.example.submission5_androidexpert.ConstantVariable.Companion.EXTRA_MOVIE_ID
import com.example.submission5_androidexpert.ConstantVariable.Companion.EXTRA_TVSHOW_ID
import com.example.submission5_androidexpert.ConstantVariable.Companion.MOVIE
import com.example.submission5_androidexpert.ConstantVariable.Companion.SEARCH_QUERY
import com.example.submission5_androidexpert.ConstantVariable.Companion.TVSHOW
import com.example.submission5_androidexpert.R
import com.example.submission5_androidexpert.activity.DetailActivity
import com.example.submission5_androidexpert.adapter.MainRecycleViewAdapter
import com.example.submission5_androidexpert.adapter.OnMovieItemClickCallback
import com.example.submission5_androidexpert.adapter.OnTvShowItemClickCallback
import com.example.submission5_androidexpert.model.Movie
import com.example.submission5_androidexpert.model.TvShow
import com.example.submission5_androidexpert.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.activity_reminder_setting.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*

class SearchResultFragment : Fragment() {
    private lateinit var recycleViewAdapter: MainRecycleViewAdapter
    private val viewModel: SearchViewModel by activityViewModels()
    private var kindOfContent: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        kindOfContent  = arguments?.getString(EXTRA_KIND_OF_CONTENT)
        val searchQuery = arguments?.getString(SEARCH_QUERY)
        activity?.toolbar?.title = searchQuery
        when (kindOfContent) {
            MOVIE -> {
                recycleViewAdapter = MainRecycleViewAdapter(0)
                view.recycle_view.adapter = recycleViewAdapter
                searchQuery?.let {
                    viewModel.searchMovie(it).observe(viewLifecycleOwner, moviesObserver)
                    viewModel.searchError?.observe(viewLifecycleOwner, errorMessageObserver)
                }
                view.swipeContainer.setOnRefreshListener {
                    viewModel.resetLiveData()
                    searchQuery?.let { refreshMovieData(it) }
                }
            }
            TVSHOW -> {
                recycleViewAdapter = MainRecycleViewAdapter(1)
                view.recycle_view.adapter = recycleViewAdapter
                searchQuery?.let {
                    viewModel.searchTvShow(it).observe(viewLifecycleOwner, tvshowObserver)
                    viewModel.searchError?.observe(viewLifecycleOwner, errorMessageObserver)
                }
                view.swipeContainer.setOnRefreshListener {
                    viewModel.resetLiveData()
                    searchQuery?.let { refreshTvShowData(it) }
                }
            }
        }
        view.swipeContainer.setColorSchemeResources(R.color.colorAccent, R.color.colorGray)
        view.recycle_view.layoutManager = LinearLayoutManager(view.context)
        return view
    }

    private fun refreshMovieData(query: String) {
        progress_bar.visibility = View.VISIBLE
        txt_error_message.visibility = View.INVISIBLE
        recycleViewAdapter.clearMoviesData()
        recycle_view.Recycler().clear()
        recycle_view.recycledViewPool.clear()
        recycle_view.layoutManager?.removeAllViews()
        viewModel.searchMovie(query).observe(viewLifecycleOwner, moviesObserver)
        viewModel.searchError?.observe(viewLifecycleOwner, errorMessageObserver)
        swipeContainer.isRefreshing = false
    }

    private fun refreshTvShowData(query: String) {
        progress_bar.visibility = View.VISIBLE
        txt_error_message.visibility = View.INVISIBLE
        recycleViewAdapter.clearTvShowsData()
        recycle_view.Recycler().clear()
        recycle_view.recycledViewPool.clear()
        recycle_view.layoutManager?.removeAllViews()
        viewModel.searchTvShow(query).observe(viewLifecycleOwner, tvshowObserver)
        viewModel.searchError?.observe(viewLifecycleOwner, errorMessageObserver)
        swipeContainer.isRefreshing = false
    }

    private val moviesObserver = Observer<ArrayList<Movie>> { movieList ->
        if (!movieList.isNullOrEmpty()) {
            recycleViewAdapter.setMoviesData(movieList)
            progress_bar.visibility = View.INVISIBLE
            txt_error_message.visibility = View.INVISIBLE
        }
    }

    private val tvshowObserver = Observer<ArrayList<TvShow>> { tvShowList ->
        if (!tvShowList.isNullOrEmpty()) {
            recycleViewAdapter.setTvShowsData(tvShowList)
            progress_bar.visibility = View.INVISIBLE
            txt_error_message.visibility = View.INVISIBLE
        }
    }

    private val errorMessageObserver = Observer<Int> { errorCode ->
        if (errorCode != null) {
            progress_bar.visibility = View.INVISIBLE
            when (errorCode){
                1 -> txt_error_message.text = activity?.resources?.getString(R.string.error_conn_message)
                200 -> txt_error_message.text = activity?.resources?.getString(R.string.search_not_found)
                401 -> txt_error_message.text = activity?.resources?.getString(R.string.invalid_api_key)
                else -> txt_error_message.text = activity?.resources?.getString(R.string.server_error)
            }
            txt_error_message.visibility = View.VISIBLE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (kindOfContent) {
            MOVIE -> {
                recycleViewAdapter.setOnMovieItemClickCallback(object: OnMovieItemClickCallback {
                    override fun onItemClicked(data: Movie) {
                        val intentToDetailActivity = Intent(view.context, DetailActivity::class.java)
                        intentToDetailActivity.putExtra(EXTRA_MOVIE_ID, data.id)
                        intentToDetailActivity.putExtra(EXTRA_KIND_OF_CONTENT, MOVIE)
                        startActivity(intentToDetailActivity)
                    }
                })
            }
            TVSHOW -> {
                recycleViewAdapter.setOnTvShowItemClickCallback(object: OnTvShowItemClickCallback {
                    override fun onItemClicked(data: TvShow) {
                        val intentToDetailActivity = Intent(view.context, DetailActivity::class.java)
                        intentToDetailActivity.putExtra(EXTRA_TVSHOW_ID, data.id)
                        intentToDetailActivity.putExtra(EXTRA_KIND_OF_CONTENT, TVSHOW)
                        startActivity(intentToDetailActivity)
                    }
                })
            }
        }
    }
}
