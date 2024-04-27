package com.example.submission5_androidexpert.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.submission5_androidexpert.ConstantVariable.Companion.EXTRA_KIND_OF_CONTENT
import com.example.submission5_androidexpert.ConstantVariable.Companion.EXTRA_MOVIE_ID
import com.example.submission5_androidexpert.ConstantVariable.Companion.EXTRA_TVSHOW_ID
import com.example.submission5_androidexpert.ConstantVariable.Companion.FRAGMENT_ARGUMENT
import com.example.submission5_androidexpert.ConstantVariable.Companion.FROM_MOVIE_WIDGET
import com.example.submission5_androidexpert.ConstantVariable.Companion.FROM_MOVIE_WIDGET_WITH_ID
import com.example.submission5_androidexpert.ConstantVariable.Companion.FROM_TVSHOW_WIDGET
import com.example.submission5_androidexpert.ConstantVariable.Companion.FROM_TVSHOW_WIDGET_WITH_ID
import com.example.submission5_androidexpert.ConstantVariable.Companion.MOVIE
import com.example.submission5_androidexpert.ConstantVariable.Companion.SEARCH_QUERY
import com.example.submission5_androidexpert.ConstantVariable.Companion.TVSHOW
import com.example.submission5_androidexpert.R
import com.example.submission5_androidexpert.databinding.ActivityMainBinding
import com.example.submission5_androidexpert.viewmodel.FavoriteViewModel
import com.example.submission5_androidexpert.viewmodel.MainViewModel
import com.example.submission5_androidexpert.viewmodel.SearchViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    private lateinit var navController: NavController
    private lateinit var navView: BottomNavigationView
    private lateinit var searchMenu: SearchView
    private lateinit var viewModel: MainViewModel
    private lateinit var searchViewModel: SearchViewModel
    private var currentDestination: Int? = null

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title = null
        ViewModelProvider(this).get(FavoriteViewModel::class.java)
        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        navView = findViewById(R.id.nav_view)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.nav_movie,
            R.id.nav_tv,
            R.id.nav_fav,
            R.id.nav_release
        ).build()
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener(this)
        currentDestination = navController.currentDestination?.id

        // Recycler view will scroll to top when user click on binding.toolbar or title (except for Fav Fragment)
        binding.toolbar.setOnClickListener{
            /*val recyclerView = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment)?.recycle_view
            if (currentDestination != R.id.nav_fav)
                recyclerView?.scrollToPosition(0)*/
        }
        // Set action on Toolbar's button UP when SearchResult Fragment show up
        binding.toolbar.setNavigationOnClickListener {
            searchViewModel.resetLiveData()
            onBackPressed()
        }
        // Handle user's click from release notification or widgets
        handleIntent()
    }

    private fun startDetailActivity(kindOfContent: String, idTag: String, id: Int) {
        val intentToDetailActivity = Intent(this, DetailActivity::class.java)
        intentToDetailActivity.putExtra(EXTRA_KIND_OF_CONTENT, kindOfContent)
        intentToDetailActivity.putExtra(idTag, id)
        startActivity(intentToDetailActivity)
    }

    private fun handleIntent() {
        if (intent != null) {
            when (intent.action){
                FROM_MOVIE_WIDGET -> navController.navigate(R.id.nav_fav)
                FROM_TVSHOW_WIDGET -> {
                    val argument = Bundle()
                    argument.putString(FRAGMENT_ARGUMENT, TVSHOW)
                    navController.navigate(R.id.nav_fav, argument)
                }
                FROM_MOVIE_WIDGET_WITH_ID -> {
                    navController.navigate(R.id.nav_fav)
                    val id = intent.getIntExtra(EXTRA_MOVIE_ID, 0)
                    startDetailActivity(MOVIE, EXTRA_MOVIE_ID, id)
                }
                FROM_TVSHOW_WIDGET_WITH_ID -> {
                    val argument = Bundle()
                    argument.putString(FRAGMENT_ARGUMENT, TVSHOW)
                    navController.navigate(R.id.nav_fav, argument)
                    val id = intent.getIntExtra(EXTRA_TVSHOW_ID, 0)
                    startDetailActivity(TVSHOW, EXTRA_TVSHOW_ID, id)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchMenu = menu.findItem(R.id.search).actionView as SearchView
        searchMenu.apply {
            maxWidth = binding.toolbar.width
            isIconified = true
            setOnSearchClickListener {
                when (currentDestination) {
                    R.id.nav_movie ->
                        searchMenu.queryHint = this.resources.getString(R.string.search_movie_hint)
                    R.id.nav_tv ->
                        searchMenu.queryHint = this.resources.getString(R.string.search_tv_hint)
                    R.id.nav_search -> {
                        searchViewModel.resetLiveData()
                        onBackPressed()
                    }
                }
            }
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleSearchAction(intent)
    }

    private fun handleSearchAction(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val searchQuery = intent.getStringExtra(SearchManager.QUERY)
            when (currentDestination) {
                R.id.nav_movie -> searchQuery?.let { navigateToSearchFragment(it, MOVIE) }
                R.id.nav_tv -> searchQuery?.let { navigateToSearchFragment(it, TVSHOW) }
            }
        }
    }

    private fun navigateToSearchFragment(searchQuery: String, kindOfContent: String) {
        searchMenu.onActionViewCollapsed()
        val args = Bundle()
        args.putString(EXTRA_KIND_OF_CONTENT, kindOfContent)
        args.putString(SEARCH_QUERY, searchQuery)
        navController.navigate(R.id.nav_search, args)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        // Hide search on Favorite or Release fragment and show it on another fragment
        // later invoked from Favorite or Release fragment
        menu?.getItem(0)?.isVisible = !(currentDestination == R.id.nav_fav || currentDestination == R.id.nav_release)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        currentDestination = destination.id
        when (currentDestination) {
            R.id.nav_movie -> binding.toolbar.title = this.resources.getString(R.string.main_title_movie)
            R.id.nav_tv -> binding.toolbar.title = this.resources.getString(R.string.main_title_tv)
            R.id.nav_fav -> binding.toolbar.title = this.resources.getString(R.string.title_favorite)
            R.id.nav_release -> binding.toolbar?.title = this.resources.getString(R.string.main_title_release)
        }
        // Show search menu on Movie or TV Show fragment
        if (binding.toolbar.menu.size() > 0)
            binding.toolbar.menu[0].isVisible = !(currentDestination == R.id.nav_fav || currentDestination == R.id.nav_release)
        // Hide navView on Search Fragemnt
        navView.isVisible = (currentDestination != R.id.nav_search)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.lang_setting -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
                true
            }
            R.id.reminder_setting -> {
                val mIntent = Intent(this, ReminderSettingActivity::class.java)
                startActivity(mIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun setTitle(query: String) {
        binding.toolbar.title = query
    }
}
