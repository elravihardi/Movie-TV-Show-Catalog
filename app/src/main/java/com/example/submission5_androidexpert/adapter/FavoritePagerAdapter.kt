package com.example.submission5_androidexpert.adapter

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.submission5_androidexpert.R
import com.example.submission5_androidexpert.navigation.favorite.FavTabsFragment

class FavoritePagerAdapter(private val mContext: Context, fm: FragmentManager):
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    @StringRes
    private val tabsTitle = intArrayOf (
        R.string.title_movie,
        R.string.title_tv
    )

    override fun getItem(position: Int): Fragment {
        return FavTabsFragment.newFavInstance(position)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(tabsTitle[position])
    }

    override fun getCount() = tabsTitle.size
}