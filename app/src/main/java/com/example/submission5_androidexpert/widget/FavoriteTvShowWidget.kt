package com.example.submission5_androidexpert.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.widget.RemoteViews
import androidx.core.net.toUri
import com.example.submission5_androidexpert.ConstantVariable.Companion.EXTRA_TVSHOW_ID
import com.example.submission5_androidexpert.ConstantVariable.Companion.FROM_TVSHOW_WIDGET
import com.example.submission5_androidexpert.ConstantVariable.Companion.FROM_TVSHOW_WIDGET_WITH_ID
import com.example.submission5_androidexpert.R
import com.example.submission5_androidexpert.activity.MainActivity

class FavoriteTvShowWidget : AppWidgetProvider() {
    companion object {
        private const val TO_DETAIL_ACTION = "com.example.submission5_androidexpert.TO_DETAIL"
        private const val TO_MAIN_ACTION = "com.example.submission5_androidexpert.TO_MAIN"

        private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val intent = Intent(context, FavTvShowWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

            val views = RemoteViews(context.packageName, R.layout.favorite_tv_show_widget)
            views.setRemoteAdapter(R.id.stack_view, intent)
            views.setEmptyView(R.id.stack_view, R.id.empty_view)

            // Send Broadcast if User click the Widget Banner
            val intentToMain = Intent(context, FavoriteTvShowWidget::class.java)
            intentToMain.action = TO_MAIN_ACTION
            intentToMain.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()
            val pendingIntent1 = PendingIntent.getBroadcast(
                context,
                1,
                intentToMain,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            views.setOnClickPendingIntent(R.id.banner_view, pendingIntent1)

            // Send Broadcast if User click one of item in Widget
            val intentToDetail = Intent(context, FavoriteTvShowWidget::class.java)
            intentToDetail.action = TO_DETAIL_ACTION
            intentToDetail.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()
            val pendingIntent2 = PendingIntent.getBroadcast(
                context,
                2,
                intentToDetail,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            views.setPendingIntentTemplate(R.id.stack_view, pendingIntent2)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action != null) {
            // Set Intent to Main Activity if User click the Widget Banner or one of item in Widget
            val intentToMainActivity = Intent(context, MainActivity::class.java)
            intentToMainActivity.addFlags(FLAG_ACTIVITY_NEW_TASK)
            intentToMainActivity.addFlags(FLAG_ACTIVITY_CLEAR_TASK)
            intentToMainActivity.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            if (intent.action == TO_MAIN_ACTION) {
                // Main Activity will navogate to Favorite Fragment
                intentToMainActivity.action = FROM_TVSHOW_WIDGET
                context.startActivity(intentToMainActivity)
            } else if (intent.action == TO_DETAIL_ACTION){
                // Main Activity will start Detail Activity to load Movie detail
                val id = intent.getIntExtra(EXTRA_TVSHOW_ID, 0)
                intentToMainActivity.action = FROM_TVSHOW_WIDGET_WITH_ID
                intentToMainActivity.putExtra(EXTRA_TVSHOW_ID, id)
                context.startActivity(intentToMainActivity)
            }
        }
    }
}