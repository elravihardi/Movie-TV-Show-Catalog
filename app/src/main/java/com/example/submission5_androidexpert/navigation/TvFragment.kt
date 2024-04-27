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
import com.example.submission5_androidexpert.ConstantVariable.Companion.EXTRA_TVSHOW_ID
import com.example.submission5_androidexpert.ConstantVariable.Companion.TVSHOW
import com.example.submission5_androidexpert.R
import com.example.submission5_androidexpert.activity.DetailActivity
import com.example.submission5_androidexpert.adapter.MainRecycleViewAdapter
import com.example.submission5_androidexpert.adapter.OnTvShowItemClickCallback
import com.example.submission5_androidexpert.databinding.FragmentMainBinding
import com.example.submission5_androidexpert.model.TvShow
import com.example.submission5_androidexpert.viewmodel.MainViewModel

class TvFragment : Fragment(){
    private val recycleViewAdapter: MainRecycleViewAdapter = MainRecycleViewAdapter(1)
    private val viewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        viewModel.getTvShowList().observe(viewLifecycleOwner, tvshowObserver)
        viewModel.tvshowRequestError?.observe(viewLifecycleOwner, errorMessageObserver)
        binding.swipeContainer.setOnRefreshListener {
            viewModel.resetTvShowLiveData()
            refreshTvShowData()
        }
        binding.swipeContainer.setColorSchemeResources(R.color.colorAccent, R.color.colorGray)
        binding.recycleView.layoutManager = LinearLayoutManager(context)
        binding.recycleView.adapter = recycleViewAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.tvshowLayoutManagerState?.observe(viewLifecycleOwner, Observer { state ->
            if (state != null)
                binding.recycleView.layoutManager?.onRestoreInstanceState(state)
        })
        recycleViewAdapter.setOnTvShowItemClickCallback(object: OnTvShowItemClickCallback{
            override fun onItemClicked(data: TvShow) {
                val intentToDetailActivity = Intent(view.context, DetailActivity::class.java)
                intentToDetailActivity.putExtra(EXTRA_TVSHOW_ID, data.id)
                intentToDetailActivity.putExtra(EXTRA_KIND_OF_CONTENT, TVSHOW)
                startActivity(intentToDetailActivity)
            }
        })
    }

    private fun refreshTvShowData() {
        binding.progressBar.visibility = View.VISIBLE
        binding.txtErrorMessage.visibility = View.INVISIBLE
        recycleViewAdapter.clearTvShowsData()
        binding.recycleView.Recycler().clear()
        binding.recycleView.recycledViewPool.clear()
        binding.recycleView.layoutManager?.removeAllViews()
        viewModel.getTvShowList().observe(viewLifecycleOwner, tvshowObserver)
        viewModel.tvshowRequestError?.observe(viewLifecycleOwner, errorMessageObserver)
        binding.swipeContainer.isRefreshing = false
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
            binding.txtErrorMessage.visibility = View.VISIBLE
            when (errorCode){
                1 -> binding.txtErrorMessage.text = activity?.resources?.getString(R.string.error_conn_message)
                401 -> binding.txtErrorMessage.text = activity?.resources?.getString(R.string.invalid_api_key)
                404 -> binding.txtErrorMessage.text = activity?.resources?.getString(R.string.tvshow_error404_message)
                else -> binding.txtErrorMessage.text = activity?.resources?.getString(R.string.server_error)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Save scroll position on Recycler View
        val layoutManagerState = this.binding.recycleView.layoutManager?.onSaveInstanceState()
        viewModel.setTvShowLayoutManagerState(layoutManagerState)
    }
}
