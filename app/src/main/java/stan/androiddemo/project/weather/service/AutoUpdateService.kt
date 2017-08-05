package stan.androiddemo.project.weather.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.SystemClock
import android.preference.PreferenceManager
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import stan.androiddemo.project.weather.utility.Utility
import stan.androiddemo.tool.HttpTool
import java.io.IOException

/**
 * Created by hugfo on 2017/7/18.
 */
class AutoUpdateService: Service() {
    override fun onBind(p0: Intent?): IBinder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        updateWeather()
        updateBingPic()
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val anHour = 8 * 60 * 60 * 1000
        val triggerAytTime = SystemClock.elapsedRealtime() + anHour
        val i = Intent(this, AutoUpdateService::class.java)
        val pi = PendingIntent.getService(this, 0, i, 0)
        alarmManager.cancel(pi)
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAytTime, pi)
        return super.onStartCommand(intent, flags, startId)
    }

    private fun updateWeather() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val weatherString = prefs.getString("weather", null)
        if (weatherString != null) {
            val weather = Utility.handleWeatherResponse(weatherString)
            val weatherId = weather!!.basic!!.weatherId
            val weatherUrl = "http://guolin.tech/api/weather?cityid=$weatherId&key=bc0418b57b2d4918819d3974ac1285d9"
            HttpTool.get(weatherUrl, object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    val responseText = response.body().string()
                    val weather1 = Utility.handleWeatherResponse(responseText)
                    if (weather1 != null && "OK" == weather1!!.status) {
                        val editor = PreferenceManager.getDefaultSharedPreferences(this@AutoUpdateService).edit()
                        editor.putString("weather", responseText)
                        editor.apply()

                    }
                }
            })
        }
    }

    fun updateBingPic() {
        val requestBingPic = "http://guolin.tech/api/bing_pic"
        HttpTool.get(requestBingPic, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val bingPic = response.body().string()
                //this is a bug can not convert the body to teh url
                //not a bug ,usr the string() can convert the httpbody to url correctly
                val editor = PreferenceManager.getDefaultSharedPreferences(this@AutoUpdateService).edit()
                editor.putString("bing_pic", bingPic)
                editor.apply()
            }
        })
    }
}