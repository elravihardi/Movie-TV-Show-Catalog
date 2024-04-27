package com.example.submission5_androidexpert.reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.submission5_androidexpert.reminder.ReminderReceiver.Companion.ID_DAILY_ALARM
import com.example.submission5_androidexpert.reminder.ReminderReceiver.Companion.ID_RELEASE_ALARM
import com.example.submission5_androidexpert.reminder.ReminderReceiver.Companion.TYPE_DAILY_REMINDER
import com.example.submission5_androidexpert.reminder.ReminderReceiver.Companion.TYPE_RELEASE_REMINDER
import java.util.*

class Reminder(private val context: Context) {

    fun turnOnDailyReminder() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)
        intent.addCategory(TYPE_DAILY_REMINDER)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ID_DAILY_ALARM,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 7)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val timeInMillis = calendar.timeInMillis + 86400000L

        // as of API 19, all repeating alarms are inexact. So I use one-time alarm,
        // and then scheduling the next one when handling each alarm delivery
        alarmManager.setExact(
            AlarmManager.RTC,
            timeInMillis,
            pendingIntent
        )
    }

    fun turnOnReleaseReminder() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)
        intent.addCategory(TYPE_RELEASE_REMINDER)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ID_RELEASE_ALARM,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 8)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val timeInMillis = calendar.timeInMillis + 86400000L

        // as of API 19, all repeating alarms are inexact. So i use one-time alarm,
        // and then scheduling the next one when handling each alarm delivery
        alarmManager.setExact(
            AlarmManager.RTC,
            timeInMillis,
            pendingIntent
        )
    }

    fun turnOffReminder(requestCode: Int, category: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)
        intent.addCategory(category)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
    }
}
