package com.example.submission5_androidexpert.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.os.Binder
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.submission5_androidexpert.ConstantVariable.Companion.EXTRA_TVSHOW_ID
import com.example.submission5_androidexpert.R
import com.example.submission5_androidexpert.db.DatabaseContract.FavoriteTvShowColumns.Companion.TVSHOW_DATE
import com.example.submission5_androidexpert.db.DatabaseContract.FavoriteTvShowColumns.Companion.TVSHOW_ID
import com.example.submission5_androidexpert.db.DatabaseContract.FavoriteTvShowColumns.Companion.TVSHOW_NAME
import com.example.submission5_androidexpert.db.DatabaseContract.FavoriteTvShowColumns.Companion.TVSHOW_OVERVIEW
import com.example.submission5_androidexpert.db.DatabaseContract.FavoriteTvShowColumns.Companion.TVSHOW_POSTER
import com.example.submission5_androidexpert.db.DatabaseContract.FavoriteTvShowColumns.Companion.TVSHOW_VOTE
import com.example.submission5_androidexpert.db.DatabaseContract.FavoriteTvShowColumns.Companion.URI_FAVORITE_TVSHOW
import com.example.submission5_androidexpert.helper.MappingHelper
import com.example.submission5_androidexpert.retrofit.posterURL
import com.example.submission5_androidexpert.room.FavoriteTvShow

internal class FavTvShowWidgetViewsFactory(private val mContext: Context): RemoteViewsService.RemoteViewsFactory {

    private var favTvShowList = ArrayList<FavoriteTvShow>()

    override fun onCreate() {
    }

    @SuppressLint("Recycle")
    override fun onDataSetChanged() {
        val identityToken = Binder.clearCallingIdentity()

        favTvShowList.clear()
        val cursor = mContext.applicationContext.contentResolver.query(
            URI_FAVORITE_TVSHOW,
            arrayOf(
                TVSHOW_ID,
                TVSHOW_NAME,
                TVSHOW_OVERVIEW,
                TVSHOW_VOTE,
                TVSHOW_POSTER,
                TVSHOW_DATE
            ),
            null,
            null,
            null
        ) as Cursor
        favTvShowList.addAll(MappingHelper.mapFavTvShowsCursorToArrayList(cursor))
        Binder.restoreCallingIdentity(identityToken)
    }

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.item_in_widget)

        if (!favTvShowList.isNullOrEmpty()){
            rv.setViewVisibility(R.id.btn_delete, View.GONE)
            favTvShowList[position].let {
                rv.setTextViewText(
                    R.id.txt_title, String.format(
                        mContext.resources.getString(R.string.list_contents),
                        it.name
                    )
                )
                if (!it.firstAirDate.isNullOrBlank()) {
                    rv.setTextViewText(
                        R.id.txt_year, String.format(
                            mContext.resources.getString(R.string.list_year),
                            it.firstAirDate.substring(0, 4)
                        )
                    )
                }
                if (!it.overview.isNullOrBlank()) {
                    rv.setTextViewText(
                        R.id.txt_overview, String.format(
                            mContext.resources.getString(R.string.list_contents),
                            it.overview
                        )
                    )
                }
                if (it.voteAverage != 0f) {
                    rv.setTextViewText(
                        R.id.txt_score, String.format(
                            mContext.resources.getString(R.string.detail_vote),
                            it.voteAverage.toString()
                        )
                    )
                }
                if (!it.posterPath.isNullOrBlank()) {
                    rv.setViewVisibility(R.id.lbl_img_not_available, View.GONE)
                    Glide.with(mContext.applicationContext)
                        .asBitmap()
                        .load(posterURL + it.posterPath)
                        .listener(object : RequestListener<Bitmap> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Bitmap>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                return false
                            }

                            override fun onResourceReady(
                                bitmap: Bitmap?,
                                model: Any?,
                                target: Target<Bitmap>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                rv.setImageViewBitmap(R.id.img_poster, bitmap)
                                return true
                            }
                        })
                        .submit()
                        .get()
                }
            }
            val intent = Intent()
            intent.putExtra(EXTRA_TVSHOW_ID, favTvShowList[position]._ID)
            rv.setOnClickFillInIntent(R.id.item_main_view, intent)
        }
        return rv
    }

    override fun getCount(): Int = favTvShowList.size

    override fun getLoadingView(): RemoteViews? = null

    override fun getItemId(p0: Int): Long = 0

    override fun hasStableIds(): Boolean = false

    override fun getViewTypeCount(): Int = 1

    override fun onDestroy() {
    }
}