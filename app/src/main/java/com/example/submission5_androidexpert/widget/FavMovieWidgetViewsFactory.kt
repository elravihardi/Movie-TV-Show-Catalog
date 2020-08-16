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
import com.example.submission5_androidexpert.ConstantVariable.Companion.EXTRA_MOVIE_ID
import com.example.submission5_androidexpert.R
import com.example.submission5_androidexpert.db.DatabaseContract.FavoriteMovieColumns.Companion.MOVIE_DATE
import com.example.submission5_androidexpert.db.DatabaseContract.FavoriteMovieColumns.Companion.MOVIE_ID
import com.example.submission5_androidexpert.db.DatabaseContract.FavoriteMovieColumns.Companion.MOVIE_OVERVIEW
import com.example.submission5_androidexpert.db.DatabaseContract.FavoriteMovieColumns.Companion.MOVIE_POSTER
import com.example.submission5_androidexpert.db.DatabaseContract.FavoriteMovieColumns.Companion.MOVIE_TITLE
import com.example.submission5_androidexpert.db.DatabaseContract.FavoriteMovieColumns.Companion.MOVIE_VOTE
import com.example.submission5_androidexpert.db.DatabaseContract.FavoriteMovieColumns.Companion.URI_FAVORITE_MOVIE
import com.example.submission5_androidexpert.helper.MappingHelper
import com.example.submission5_androidexpert.retrofit.posterURL
import com.example.submission5_androidexpert.room.FavoriteMovie


internal class FavMovieWidgetViewsFactory(private val mContext: Context): RemoteViewsService.RemoteViewsFactory {

    private var favMovieList = ArrayList<FavoriteMovie>()

    override fun onCreate() {
    }

    @SuppressLint("Recycle")
    override fun onDataSetChanged() {
        val identityToken = Binder.clearCallingIdentity()
        favMovieList.clear()
        val cursor = mContext.applicationContext.contentResolver.query(
            URI_FAVORITE_MOVIE,
            arrayOf(MOVIE_ID, MOVIE_TITLE, MOVIE_OVERVIEW, MOVIE_VOTE, MOVIE_POSTER, MOVIE_DATE),
            null,
            null,
            null
        ) as Cursor
        favMovieList.addAll(MappingHelper.mapFavMoviesCursorToArrayList(cursor))
        Binder.restoreCallingIdentity(identityToken)
    }

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.item_in_widget)
        if (!favMovieList.isNullOrEmpty()){
            rv.setViewVisibility(R.id.btn_delete, View.GONE)
            favMovieList[position].let {
                rv.setTextViewText(R.id.txt_title, String.format(
                    mContext.resources.getString(R.string.list_contents),
                    it.title)
                )
                if (!it.releaseDate.isNullOrBlank()) {
                    rv.setTextViewText(R.id.txt_year, String.format(
                        mContext.resources.getString(R.string.list_year),
                        it.releaseDate.substring(0, 4))
                    )
                }
                if (!it.overview.isNullOrBlank()) {
                    rv.setTextViewText(R.id.txt_overview, String.format(
                        mContext.resources.getString(R.string.list_contents),
                        it.overview)
                    )
                }
                if (it.voteAverage != 0f) {
                    rv.setTextViewText(R.id.txt_score, String.format(
                        mContext.resources.getString(R.string.detail_vote),
                        it.voteAverage.toString())
                    )
                }
                if (!it.posterPath.isNullOrBlank()) {
                    rv.setViewVisibility(R.id.lbl_img_not_available, View.GONE)
                    Glide.with(mContext.applicationContext)
                        .asBitmap()
                        .load(posterURL + it.posterPath)
                        .listener(object: RequestListener<Bitmap> {
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
            intent.putExtra(EXTRA_MOVIE_ID, favMovieList[position]._ID)
            rv.setOnClickFillInIntent(R.id.item_main_view, intent)
        }
        return rv
    }

    override fun getCount(): Int = favMovieList.size

    override fun getLoadingView(): RemoteViews? = null

    override fun getItemId(p0: Int): Long = 0

    override fun hasStableIds(): Boolean = false

    override fun getViewTypeCount(): Int = 1

    override fun onDestroy() {
    }
}