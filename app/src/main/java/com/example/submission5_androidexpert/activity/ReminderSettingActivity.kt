package com.example.submission5_androidexpert.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.submission5_androidexpert.ConstantVariable.Companion.DAILY_REMINDER_ON
import com.example.submission5_androidexpert.ConstantVariable.Companion.RELEASE_REMINDER_ON
import com.example.submission5_androidexpert.R
import com.example.submission5_androidexpert.reminder.Reminder
import com.example.submission5_androidexpert.reminder.ReminderReceiver.Companion.ID_DAILY_ALARM
import com.example.submission5_androidexpert.reminder.ReminderReceiver.Companion.ID_RELEASE_ALARM
import com.example.submission5_androidexpert.reminder.ReminderReceiver.Companion.TYPE_DAILY_REMINDER
import com.example.submission5_androidexpert.reminder.ReminderReceiver.Companion.TYPE_RELEASE_REMINDER
import kotlinx.android.synthetic.main.activity_reminder_setting.*

class ReminderSettingActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder_setting)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.title = this.resources.getString(R.string.reminder_setting)

        val releaseReminderOn = applicationContext
            .getSharedPreferences(RELEASE_REMINDER_ON, Context.MODE_PRIVATE)
            .getBoolean(RELEASE_REMINDER_ON, true)
        if (!releaseReminderOn) switch_release_reminder.isChecked = false

        val dailyReminderOn = applicationContext
            .getSharedPreferences(DAILY_REMINDER_ON, Context.MODE_PRIVATE)
            .getBoolean(DAILY_REMINDER_ON, true)
        if (!dailyReminderOn) switch_daily_reminder.isChecked = false

        // Avoid setOnCheckedChangedListener
        switch_release_reminder.setOnClickListener(this)
        switch_daily_reminder.setOnClickListener(this)
    }

    private fun setReleaseReminderPref(param: Boolean) {
        applicationContext
            .getSharedPreferences(RELEASE_REMINDER_ON, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(RELEASE_REMINDER_ON, param)
            .apply()
    }

    private fun setDailyReminderPref(param: Boolean) {
        applicationContext
            .getSharedPreferences(DAILY_REMINDER_ON, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(DAILY_REMINDER_ON, param)
            .apply()
    }

    override fun onClick(switchId: View) {
        when (switchId.id) {
            R.id.switch_release_reminder -> {
                val releaseReminder = Reminder(applicationContext)
                if (switch_release_reminder.isChecked) {
                    releaseReminder.turnOnReleaseReminder()
                    setReleaseReminderPref(true)
                    Toast.makeText(
                        this,
                        applicationContext.resources.getString(R.string.release_reminder_on_msg),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    releaseReminder.turnOffReminder(ID_RELEASE_ALARM, TYPE_RELEASE_REMINDER)
                    setReleaseReminderPref(false)
                    Toast.makeText(
                        this,
                        String.format(
                            applicationContext.resources.getString(R.string.reminder_off_msg),
                            applicationContext.resources.getString(R.string.release_reminder_title)
                        ),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            R.id.switch_daily_reminder -> {
                val dailyReminder = Reminder(applicationContext)
                if (switch_daily_reminder.isChecked) {
                    dailyReminder.turnOnDailyReminder()
                    setDailyReminderPref(true)
                    Toast.makeText(
                        this,
                        applicationContext.resources.getString(R.string.daily_reminder_on_msg),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    dailyReminder.turnOffReminder(ID_DAILY_ALARM, TYPE_DAILY_REMINDER)
                    setDailyReminderPref(false)
                    Toast.makeText(
                        this,
                        String.format(
                            applicationContext.resources.getString(R.string.reminder_off_msg),
                            applicationContext.resources.getString(R.string.daily_reminder_title)
                        ),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
