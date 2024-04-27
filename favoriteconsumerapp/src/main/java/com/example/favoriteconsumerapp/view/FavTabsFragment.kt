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
import com.example.favoriteconsumerapp.databinding.FragmentTabsBinding
import com.example.favoriteconsumerapp.viewmodel.Viewmodel

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

    private var _binding: FragmentTabsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentTabsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(Viewmodel::class.java)
        if (arguments != null) {
            val index = arguments?.getInt(FAV_ARG_SECTION_NUMBER, 0) as Int
            if (index == 0) {
                favMovieAdapter = RecycleViewAdapter(index)
                viewModel.getAllFavMovies()?.observe(viewLifecycleOwner, Observer { favMovies ->
                    if (!favMovies.isNullOrEmpty()){
                        favMovieAdapter.setFavMoviesData(favMovies)
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.txtErrorMsg.visibility = View.INVISIBLE
                    } else {
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.txtErrorMsg.visibility = View.VISIBLE
                        binding.txtErrorMsg.text = activity?.resources?.getString(R.string.fav_movie_empty)
                    }
                })
                binding.recycleView.adapter = favMovieAdapter
                binding.recycleView.layoutManager = LinearLayoutManager(context)
            } else {
                favTvShowAdapter = RecycleViewAdapter(index)
                viewModel.getAllFavTvShows()?.observe(viewLifecycleOwner, Observer { favTvShows ->
                    if (!favTvShows.isNullOrEmpty()){
                        favTvShowAdapter.setFavTvShowsData(favTvShows)
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.txtErrorMsg.visibility = View.INVISIBLE
                    } else {
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.txtErrorMsg.visibility = View.VISIBLE
                        binding.txtErrorMsg.text = activity?.resources?.getString(R.string.fav_tvshow_empty)
                    }
                })
                binding.recycleView.adapter = favTvShowAdapter
                binding.recycleView.layoutManager = LinearLayoutManager(context)
            }
        }
        return binding.root
    }
}