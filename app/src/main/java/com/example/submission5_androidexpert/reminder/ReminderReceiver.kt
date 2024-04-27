package com.example.submission5_androidexpert.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import com.example.submission5_androidexpert.ConstantVariable.Companion.EXTRA_KIND_OF_CONTENT
import com.example.submission5_androidexpert.ConstantVariable.Companion.EXTRA_MOVIE_ID
import com.example.submission5_androidexpert.ConstantVariable.Companion.MOVIE
import com.example.submission5_androidexpert.R
import com.example.submission5_androidexpert.activity.DetailActivity
import com.example.submission5_androidexpert.activity.MainActivity
import com.example.submission5_androidexpert.model.GetMovieListResponse
import com.example.submission5_androidexpert.model.Movie
import com.example.submission5_androidexpert.retrofit.TmdbApiClient
import com.example.submission5_androidexpert.retrofit.TmdbApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ReminderReceiver : BroadcastReceiver() {

    companion object {
        const val ID_DAILY_ALARM = 100
        const val ID_RELEASE_ALARM = 101
        const val TYPE_DAILY_REMINDER = "dailyReminderAlarm"
        const val TYPE_RELEASE_REMINDER = "releaseReminderAlarm"
        const val CHANNEL_DAILY_NAME = "Daily reminder"
        const val CHANNEL_RELEASE_NAME = "Release reminder"
        const val CHANNEL_DAILY_ID = "Channel_0"
        const val CHANNEL_RELEASE_ID = "Channel_1"
    }

    private val tmdbApi = TmdbApiClient.getClient().create(TmdbApiInterface::class.java)

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.hasCategory(TYPE_RELEASE_REMINDER)) {
            getMovieReleaseList(context)
        } else if (intent.hasCategory(TYPE_DAILY_REMINDER)){
            showDailyReminderNotification(context)
        }
    }

    private fun getMovieReleaseList(context: Context) {
        val todayDate = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val movieListCall: Call<GetMovieListResponse> = tmdbApi.getMovieReleaseToday(
            TmdbApiClient.apiKey,
            dateFormat.format(todayDate.time),
            dateFormat.format(todayDate.time)
        )

        val movieList: ArrayList<Movie> = ArrayList()
        movieListCall.enqueue(
            object: Callback<GetMovieListResponse> {
                override fun onFailure(call: Call<GetMovieListResponse>?, t: Throwable?) {
                }
                override fun onResponse(call: Call<GetMovieListResponse>?, response: Response<GetMovieListResponse>?) {
                    val body = response?.body()
                    if (body != null && body.results.isNotEmpty()) {
                        for (i in body.results.iterator()) {
                            movieList.add(
                                Movie(i.id, i.title, i.overview, i.posterPath, i.voteAverage, i.releaseDate)
                            )
                        }
                        movieList.sortByDescending { it.title }
                        showReleaseReminderNotification(context, movieList)
                    }
                }
            }
        )
    }

    private fun showReleaseReminderNotification(context: Context, movieList: ArrayList<Movie>) {
        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_RELEASE_ID,
                CHANNEL_RELEASE_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManagerCompat.createNotificationChannel(channel)
        }
        for ((i, movie) in movieList.withIndex()) {
            var resultIntent: Intent?
            resultIntent = Intent(context, DetailActivity::class.java)
            resultIntent.putExtra(EXTRA_MOVIE_ID, movie.id)
            resultIntent.putExtra(EXTRA_KIND_OF_CONTENT, MOVIE)
            val resultPendingIntent = TaskStackBuilder.create(context).run {
                addNextIntentWithParentStack(resultIntent)
                getPendingIntent(i, PendingIntent.FLAG_UPDATE_CURRENT)
            }
            val notification = NotificationCompat.Builder(context, CHANNEL_DAILY_ID)
                .setContentIntent(resultPendingIntent)
                .setSmallIcon(R.drawable.ic_movie_24dp)
                .setContentTitle(context.resources.getString(R.string.main_title_release))
                .setContentText(String.format(
                    context.resources.getString(R.string.movie_release_notif_content),
                    movie.title))
                .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setChannelId(CHANNEL_RELEASE_ID)
                .setAutoCancel(true)
                .build()
            notificationManagerCompat.notify(i, notification)
        }
        // after the receiver receives the first alarm broadcast,
        // then the next alarm will be set via this code (If there are no changes to the reminder settings)
        val alarmReminder = Reminder(context)
        alarmReminder.turnOnReleaseReminder()
    }

    private fun showDailyReminderNotification(context: Context) {
        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_DAILY_ID,
                CHANNEL_DAILY_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManagerCompat.createNotificationChannel(channel)
        }
        val resultIntent = Intent(context, MainActivity::class.java)
        resultIntent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        val resultPendingIntent = PendingIntent.getActivity(
            context,
            ID_DAILY_ALARM,
            resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val builder = NotificationCompat.Builder(context, CHANNEL_DAILY_ID)
            .setContentIntent(resultPendingIntent)
            .setSmallIcon(R.drawable.ic_movie_24dp)
            .setContentTitle(context.resources.getString(R.string.app_name))
            .setContentText(context.resources.getString(R.string.daily_reminder_notif_msg))
            .setColor(ContextCompat.getColor(context, R.color.colorAccent))
            .setAutoCancel(true)
            .setChannelId(CHANNEL_DAILY_ID)
            .build()
        notificationManagerCompat.notify(ID_DAILY_ALARM, builder)
        // after the receiver received the first alarm broadcast,
        // then the next alarm will be set via this code (If there are no changes to the reminder settings)
        val alarmReminder = Reminder(context)
        alarmReminder.turnOnDailyReminder()
    }
}