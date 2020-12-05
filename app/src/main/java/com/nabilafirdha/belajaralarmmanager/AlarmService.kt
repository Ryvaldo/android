package com.nabilafirdha.belajaralarmmanager

import  android.app.AlarmManager
import  android.app.PendingIntent
import  android.content.Context
import  android.content.Intent
import  android.os.Build

class AlarmService(private  val context: Context){
    private val alarmManager: AlarmManager? = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?

    fun setAlarmSekarang(timeInMillis: Long){
        setAlarm(
            timeInMillis,
            getPendingIntent(
                getIntent().apply{
                    action = Constants.ACTION_SET_EXACT
                    putExtra(Constants.EXTRA_EXACT_ALARM_TIME, timeInMillis)
                }
            )
        )
    }

    //Seminggu
    fun setAlarmBerulang(timeInMillis: Long){
        setAlarm(
            timeInMillis,
            getPendingIntent(
                getIntent().apply{
                    action = Constants.ACTION_SET_REPETITIVE_EXACT
                    putExtra(Constants.EXTRA_EXACT_ALARM_TIME, timeInMillis)
                }
            )
        )
    }

    private fun getPendingIntent(intent: Intent) =
        PendingIntent.getBroadcast(
            context, getRandomRequestCode(), intent,PendingIntent.FLAG_UPDATE_CURRENT
        )

    private fun setAlarm(timeInMillis: Long, pendingIntent: PendingIntent){
        alarmManager?.let{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP, timeInMillis,pendingIntent
                )
            }else{
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis,pendingIntent)
                }
            }
        }
    }
    private fun  getIntent() = Intent(context, AlarmReceiver::class.java)
    private fun  getRandomRequestCode() = RandomIntUtil.getRandomInt()
}