package com.example.submission5_androidexpert.widget

import android.content.Intent
import android.widget.RemoteViewsService

class FavTvShowWidgetService: RemoteViewsService() {
    override fun onGetViewFactory(p0: Intent): RemoteViewsFactory {
        return FavTvShowWidgetViewsFactory(this.applicationContext)
    }
}