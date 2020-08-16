package com.example.submission5_androidexpert.widget

import android.content.Intent
import android.widget.RemoteViewsService

class FavMovieWidgetService: RemoteViewsService() {
    override fun onGetViewFactory(p0: Intent): RemoteViewsFactory{
        return FavMovieWidgetViewsFactory(this.applicationContext)
    }
}