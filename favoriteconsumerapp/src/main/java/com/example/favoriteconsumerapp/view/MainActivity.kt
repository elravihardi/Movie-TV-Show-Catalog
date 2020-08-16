package com.example.favoriteconsumerapp.view

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.example.favoriteconsumerapp.R
import com.example.favoriteconsumerapp.adapter.FavoritePagerAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_tabs.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title = this.resources.getString(R.string.title_favorite)

        view_pager.adapter = FavoritePagerAdapter(this.applicationContext, supportFragmentManager)
        tab_layout.setupWithViewPager(view_pager)
        tab_layout.setSelectedTabIndicatorColor(ContextCompat.getColor(applicationContext, R.color.colorAccent))
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
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.fav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.lang_setting -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
