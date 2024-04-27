package com.example.submission5_androidexpert.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.submission5_androidexpert.ConstantVariable.Companion.DAILY_REMINDER_ON
import com.example.submission5_androidexpert.ConstantVariable.Companion.RELEASE_REMINDER_ON
import com.example.submission5_androidexpert.R
import com.example.submission5_androidexpert.databinding.ActivityMainBinding
import com.example.submission5_androidexpert.databinding.ActivityReminderSettingBinding
import com.example.submission5_androidexpert.reminder.Reminder
import com.example.submission5_androidexpert.reminder.ReminderReceiver.Companion.ID_DAILY_ALARM
import com.example.submission5_androidexpert.reminder.ReminderReceiver.Companion.ID_RELEASE_ALARM
import com.example.submission5_androidexpert.reminder.ReminderReceiver.Companion.TYPE_DAILY_REMINDER
import com.example.submission5_androidexpert.reminder.ReminderReceiver.Companion.TYPE_RELEASE_REMINDER

class ReminderSettingActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityReminderSettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReminderSettingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.title = this.resources.getString(R.string.reminder_setting)

        val releaseReminderOn = applicationContext
            .getSharedPreferences(RELEASE_REMINDER_ON, Context.MODE_PRIVATE)
            .getBoolean(RELEASE_REMINDER_ON, true)
        if (!releaseReminderOn) binding.switchReleaseReminder.isChecked = false

        val dailyReminderOn = applicationContext
            .getSharedPreferences(DAILY_REMINDER_ON, Context.MODE_PRIVATE)
            .getBoolean(DAILY_REMINDER_ON, true)
        if (!dailyReminderOn) binding.switchDailyReminder.isChecked = false

        // Avoid setOnCheckedChangedListener
        binding.switchReleaseReminder.setOnClickListener(this)
        binding.switchDailyReminder.setOnClickListener(this)
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
                if (binding.switchReleaseReminder.isChecked) {
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
                if (binding.switchDailyReminder.isChecked) {
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
