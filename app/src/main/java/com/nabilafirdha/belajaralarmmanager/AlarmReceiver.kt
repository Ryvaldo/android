package com.nabilafirdha.belajaralarmmanager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import io.karn.notify.Notify
import android.text.format.DateFormat
import android.webkit.ConsoleMessage
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val timeInMillis = intent.getLongExtra(Constants.EXTRA_EXACT_ALARM_TIME, 0L)
        when (intent.action){
            Constants.ACTION_SET_EXACT ->{
                buatNotifikasi(context, "Ini Alarm saat ini", konversiTanggal(timeInMillis))
            }

            Constants.ACTION_SET_REPETITIVE_EXACT ->{
                setAlarmBerulang(AlarmService(context))
                buatNotifikasi(context, "Ini Alarm saat ini", konversiTanggal(timeInMillis))
            }
        }
    }

    private fun buatNotifikasi(context: Context, title: String, message: String){
        Notify
            .with(context)
            .content {
                this.title = title
                text = "Alarm nyala pada pukul: - $message"
            }
            .show()
    }

    private fun setAlarmBerulang(alarmService: AlarmService) {
        val cal = Calendar.getInstance().apply {
            this.timeInMillis = timeInMillis + TimeUnit.DAYS.toMillis(7)
            Timber.d("Pasang Alarm untuk minggu pada waktu yang sama  --"
                    + "${konversiTanggal(this.timeInMillis)}")
        }
        alarmService.setAlarmBerulang(cal.timeInMillis)
    }

    private fun konversiTanggal(timeInMills:Long):String =
        DateFormat.format("dd/MM/yyyy hh:mm:ss", timeInMills).toString()
}