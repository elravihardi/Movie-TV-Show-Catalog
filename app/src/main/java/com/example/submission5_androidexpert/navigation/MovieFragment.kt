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
import com.example.submission5_androidexpert.ConstantVariable.Companion.MOVIE
import com.example.submission5_androidexpert.R
import com.example.submission5_androidexpert.activity.DetailActivity
import com.example.submission5_androidexpert.adapter.MainRecycleViewAdapter
import com.example.submission5_androidexpert.adapter.OnMovieItemClickCallback
import com.example.submission5_androidexpert.model.Movie
import com.example.submission5_androidexpert.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*

class MovieFragment : Fragment() {
    private val recycleViewAdapter: MainRecycleViewAdapter = MainRecycleViewAdapter(0)
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        viewModel.getMovieList().observe(viewLifecycleOwner, moviesObserver)
        viewModel.movieRequestError?.observe(viewLifecycleOwner, errorMessageObserver)
        view.swipeContainer.setOnRefreshListener {
            viewModel.resetMovieLiveData()
            refreshMovieData()
        }
        view.swipeContainer.setColorSchemeResources(R.color.colorAccent, R.color.colorGray)
        view.recycle_view.layoutManager = LinearLayoutManager(view.context)
        view.recycle_view.adapter = recycleViewAdapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.movieLayoutManagerState?.observe(viewLifecycleOwner, Observer { state ->
            if (state != null)
                this.recycle_view.layoutManager?.onRestoreInstanceState(state)
        })
        recycleViewAdapter.setOnMovieItemClickCallback(object: OnMovieItemClickCallback {
            override fun onItemClicked(data: Movie) {
                val intentToDetailActivity = Intent(view.context, DetailActivity::class.java)
                intentToDetailActivity.putExtra(EXTRA_MOVIE_ID, data.id)
                intentToDetailActivity.putExtra(EXTRA_KIND_OF_CONTENT, MOVIE)
                startActivity(intentToDetailActivity)
            }
        })
    }

    private fun refreshMovieData() {
        progress_bar.visibility = View.VISIBLE
        txt_error_message.visibility = View.INVISIBLE
        recycleViewAdapter.clearMoviesData()
        recycle_view.Recycler().clear()
        recycle_view.recycledViewPool.clear()
        recycle_view.layoutManager?.removeAllViews()
        viewModel.getMovieList().observe(viewLifecycleOwner, moviesObserver)
        viewModel.movieRequestError?.observe(viewLifecycleOwner, errorMessageObserver)
        swipeContainer.isRefreshing = false
    }

    private val moviesObserver = Observer<ArrayList<Movie>> { movieList ->
        if (!movieList.isNullOrEmpty()) {
            recycleViewAdapter.setMoviesData(movieList)
            progress_bar.visibility = View.INVISIBLE
            txt_error_message.visibility = View.INVISIBLE
        }
    }

    private val errorMessageObserver = Observer<Int> { errorCode ->
        if (errorCode != null) {
            progress_bar.visibility = View.INVISIBLE
            txt_error_message.visibility = View.VISIBLE
            when (errorCode){
                1 -> txt_error_message.text = activity?.resources?.getString(R.string.error_conn_message)
                401 -> txt_error_message.text = activity?.resources?.getString(R.string.invalid_api_key)
                404 -> txt_error_message.text = activity?.resources?.getString(R.string.movie_error404_message)
                else -> txt_error_message.text = activity?.resources?.getString(R.string.server_error)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Save scroll position on Recycler View
        val layoutManagerState = this.recycle_view.layoutManager?.onSaveInstanceState()
        viewModel.setMovieLayoutManagerState(layoutManagerState)
    }
}
