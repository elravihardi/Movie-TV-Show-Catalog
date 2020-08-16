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
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.container_favorite.*
import kotlinx.android.synthetic.main.fragment_main.view.*

class FavoriteContainer : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.container_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            view_pager.adapter = FavoritePagerAdapter(it.applicationContext, childFragmentManager)
            tab_layout.setupWithViewPager(view_pager)
            tab_layout.setSelectedTabIndicatorColor(
                ContextCompat.getColor(it.applicationContext,
                R.color.colorAccent
            ))
        }
        // Check if intent come from Favorite Movie Widget or Favorite TV Show Widget
        if (arguments?.getString(FRAGMENT_ARGUMENT) == TVSHOW){
            tab_layout.getTabAt(1)?.select()
        }
        // scroll recycle view to top when user reselected tab
        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
                if (tab?.position == 0)
                    view_pager[0].recycle_view.scrollToPosition(0)
                else
                    view_pager[1].recycle_view.scrollToPosition(0)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
            override fun onTabSelected(tab: TabLayout.Tab?) {
            }
        })
        activity?.invalidateOptionsMenu()
    }
}
