package com.example.submission5_androidexpert.navigation.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.example.submission5_androidexpert.ConstantVariable.Companion.FRAGMENT_ARGUMENT
import com.example.submission5_androidexpert.ConstantVariable.Companion.TVSHOW
import com.example.submission5_androidexpert.R
import com.example.submission5_androidexpert.adapter.FavoritePagerAdapter
import com.example.submission5_androidexpert.databinding.ContainerFavoriteBinding
import com.google.android.material.tabs.TabLayout

class FavoriteContainer : Fragment() {
    private var _binding: ContainerFavoriteBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ContainerFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            binding.viewPager.adapter = FavoritePagerAdapter(it.applicationContext, childFragmentManager)
            binding.tabLayout.setupWithViewPager(binding.viewPager)
            binding.tabLayout.setSelectedTabIndicatorColor(
                ContextCompat.getColor(it.applicationContext,
                R.color.colorAccent
            ))
        }
        // Check if intent come from Favorite Movie Widget or Favorite TV Show Widget
        if (arguments?.getString(FRAGMENT_ARGUMENT) == TVSHOW){
            binding.tabLayout.getTabAt(1)?.select()
        }
        // scroll recycle view to top when user reselected tab
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
                /*if (tab?.position == 0)
                    binding.viewPager[0].recycle_view.scrollToPosition(0)
                else
                    binding.viewPager[1].recycle_view.scrollToPosition(0)*/
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
            override fun onTabSelected(tab: TabLayout.Tab?) {
            }
        })
        activity?.invalidateOptionsMenu()
    }
}
