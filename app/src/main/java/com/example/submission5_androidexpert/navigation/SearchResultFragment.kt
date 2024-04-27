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
import com.example.submission5_androidexpert.activity.MainActivity
import com.example.submission5_androidexpert.adapter.MainRecycleViewAdapter
import com.example.submission5_androidexpert.adapter.OnMovieItemClickCallback
import com.example.submission5_androidexpert.adapter.OnTvShowItemClickCallback
import com.example.submission5_androidexpert.databinding.FragmentMainBinding
import com.example.submission5_androidexpert.model.Movie
import com.example.submission5_androidexpert.model.TvShow
import com.example.submission5_androidexpert.viewmodel.SearchViewModel

class SearchResultFragment : Fragment() {
    private lateinit var recycleViewAdapter: MainRecycleViewAdapter
    private val viewModel: SearchViewModel by activityViewModels()
    private var kindOfContent: String? = null
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        kindOfContent  = arguments?.getString(EXTRA_KIND_OF_CONTENT)
        val searchQuery = arguments?.getString(SEARCH_QUERY)
        (requireActivity() as? MainActivity)?.setTitle(searchQuery)
        when (kindOfContent) {
            MOVIE -> {
                recycleViewAdapter = MainRecycleViewAdapter(0)
                binding.recycleView.adapter = recycleViewAdapter
                searchQuery?.let {
                    viewModel.searchMovie(it).observe(viewLifecycleOwner, moviesObserver)
                    viewModel.searchError?.observe(viewLifecycleOwner, errorMessageObserver)
                }
                binding.swipeContainer.setOnRefreshListener {
                    viewModel.resetLiveData()
                    searchQuery?.let { refreshMovieData(it) }
                }
            }
            TVSHOW -> {
                recycleViewAdapter = MainRecycleViewAdapter(1)
                binding.recycleView.adapter = recycleViewAdapter
                searchQuery?.let {
                    viewModel.searchTvShow(it).observe(viewLifecycleOwner, tvshowObserver)
                    viewModel.searchError?.observe(viewLifecycleOwner, errorMessageObserver)
                }
                binding.swipeContainer.setOnRefreshListener {
                    viewModel.resetLiveData()
                    searchQuery?.let { refreshTvShowData(it) }
                }
            }
        }
        binding.swipeContainer.setColorSchemeResources(R.color.colorAccent, R.color.colorGray)
        binding.recycleView.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    private fun refreshMovieData(query: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.txtErrorMessage.visibility = View.INVISIBLE
        recycleViewAdapter.clearMoviesData()
        binding.recycleView.Recycler().clear()
        binding.recycleView.recycledViewPool.clear()
        binding.recycleView.layoutManager?.removeAllViews()
        viewModel.searchMovie(query).observe(viewLifecycleOwner, moviesObserver)
        viewModel.searchError?.observe(viewLifecycleOwner, errorMessageObserver)
        binding.swipeContainer.isRefreshing = false
    }

    private fun refreshTvShowData(query: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.txtErrorMessage.visibility = View.INVISIBLE
        recycleViewAdapter.clearTvShowsData()
        binding.recycleView.Recycler().clear()
        binding.recycleView.recycledViewPool.clear()
        binding.recycleView.layoutManager?.removeAllViews()
        viewModel.searchTvShow(query).observe(viewLifecycleOwner, tvshowObserver)
        viewModel.searchError?.observe(viewLifecycleOwner, errorMessageObserver)
        binding.swipeContainer.isRefreshing = false
    }

    private val moviesObserver = Observer<ArrayList<Movie>> { movieList ->
        if (!movieList.isNullOrEmpty()) {
            recycleViewAdapter.setMoviesData(movieList)
            binding.progressBar.visibility = View.INVISIBLE
            binding.txtErrorMessage.visibility = View.INVISIBLE
        }
    }

    private val tvshowObserver = Observer<ArrayList<TvShow>> { tvShowList ->
        if (!tvShowList.isNullOrEmpty()) {
            recycleViewAdapter.setTvShowsData(tvShowList)
            binding.progressBar.visibility = View.INVISIBLE
            binding.txtErrorMessage.visibility = View.INVISIBLE
        }
    }

    private val errorMessageObserver = Observer<Int> { errorCode ->
        if (errorCode != null) {
            binding.progressBar.visibility = View.INVISIBLE
            when (errorCode){
                1 -> binding.txtErrorMessage.text = activity?.resources?.getString(R.string.error_conn_message)
                200 -> binding.txtErrorMessage.text = activity?.resources?.getString(R.string.search_not_found)
                401 -> binding.txtErrorMessage.text = activity?.resources?.getString(R.string.invalid_api_key)
                else -> binding.txtErrorMessage.text = activity?.resources?.getString(R.string.server_error)
            }
            binding.txtErrorMessage.visibility = View.VISIBLE
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
