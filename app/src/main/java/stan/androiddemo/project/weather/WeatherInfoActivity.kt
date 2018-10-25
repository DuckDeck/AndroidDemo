package stan.androiddemo.project.weather

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_weather_info.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import stan.androiddemo.R
import stan.androiddemo.project.weather.gson.Weather
import stan.androiddemo.project.weather.service.AutoUpdateService
import stan.androiddemo.project.weather.utility.Utility
import stan.androiddemo.tool.HttpTool
import java.io.IOException
import java.util.regex.Pattern

class WeatherInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            val decorView = window.decorView
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) //this looks work for me
            // but the status bar info lost
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.statusBarColor = Color.RED
            //not work

        }
        setContentView(R.layout.activity_weather_info)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val bingPic = prefs.getString("bing_pic", null)


        // 邮箱验证规则
        val regEx = "/^https?://(([a-zA-Z0-9_-])+(\\.)?)*(:\\d+)?(/((\\.)?(\\?)?=?&?[a-zA-Z0-9_-](\\?)?)*)*$/i"
        // 编译正则表达式
        val pattern = Pattern.compile(regEx)
        // 忽略大小写的写法


        if (bingPic != null && pattern.matcher(bingPic).matches()) {
            Glide.with(this).load(bingPic).into(img_bg_bing)
        } else {
            loadBingPic()
        }
        val weatherString = prefs.getString("weather", null)
        val weatherId: String
        if (weatherString != null) {
            val weather = Utility.handleWeatherResponse(weatherString)
            weatherId = weather!!.basic!!.weatherId!!
            showWeatherInfo(weather)
        } else {
            weatherId = intent.getStringExtra("weather_id")
            drawer_layout_fixed.visibility = View.INVISIBLE
            requestWeather(weatherId)
        }

        swipe_refresh_weather.setOnRefreshListener({ requestWeather(weatherId) })

        back_button.setOnClickListener {
            drawer_layout_fixed.openDrawer(GravityCompat.START)
            //Iusse2 when click back to show the city choose raise a error with this   java.lang.IllegalArgumentException: No drawer view found with gravity LEFT
            //you need add the city choose fragment below the drawlayout
        }
    }



    fun requestWeather( weatherId:String) {
        val weatherUrl = "http://guolin.tech/api/weather?cityid=$weatherId&key=bc0418b57b2d4918819d3974ac1285d9"
        HttpTool.get(weatherUrl, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@WeatherInfoActivity, "获取天气信息失败", Toast.LENGTH_SHORT).show()
                    swipe_refresh_weather.isRefreshing = false
                }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val responseText = response.body()!!.string()
                val weather = Utility.handleWeatherResponse(responseText)
                runOnUiThread {
                    if (weather != null && "ok" == weather.status) {
                        val editor = PreferenceManager.getDefaultSharedPreferences(this@WeatherInfoActivity).edit()
                        editor.putString("weather", responseText)
                        editor.apply()
                        showWeatherInfo(weather)
                    } else {
                        Toast.makeText(this@WeatherInfoActivity, "获取天气信息失败", Toast.LENGTH_SHORT).show()
                    }
                    swipe_refresh_weather.setRefreshing(false)
                }
            }
        })
        loadBingPic()
    }

    @SuppressLint("SetTextI18n")
    fun showWeatherInfo(weather: Weather?) = if (weather != null && "ok" == weather.status) {
        val intent = Intent(this, AutoUpdateService::class.java)
        startService(intent)
        val cityName = weather.basic!!.cityName
        val updateTime = weather.basic!!.update!!.updateTime.split(" ")[1]
        val degree = weather.now!!.temperature + "C"
        val weatherInfo = weather.now!!.more!!.info
        title_city.text = cityName
        title_update_time.text = updateTime
        degree_text.text = degree
        weather_info_text.text = weatherInfo
        forecast_layout.removeAllViews()
        for (forecast in weather.forecastList!!) {
            val view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecast_layout, false)
            (view.findViewById<TextView>(R.id.date_text) as TextView).text = forecast.date
            (view.findViewById<TextView>(R.id.info_text) as TextView).text = forecast.more!!.info
            (view.findViewById<TextView>(R.id.max_text) as TextView).text = forecast.temperature!!.max
            (view.findViewById<TextView>(R.id.min_text) as TextView).text = forecast.temperature!!.min
            forecast_layout.addView(view)
        }
        if (weather.aqi != null) {
            api_text.text = weather.aqi!!.city!!.aqi
            pm25_text.text = weather.aqi!!.city!!.pm25
        }
        comfort_text.text = "舒适度:" + weather.suggestion!!.comfort!!.info
        car_wash_text.text = "洗车指数:" + weather.suggestion!!.carWash!!.info
        sport_text.text = "运动建议:" + weather.suggestion!!.sport!!.info
        drawer_layout_fixed.setVisibility(View.VISIBLE)
    } else {
        Toast.makeText(this, "获取天气自信失败！", Toast.LENGTH_SHORT).show()
    }

    fun loadBingPic() {
        val requestBingPic = "http://guolin.tech/api/bing_pic"
        HttpTool.get(requestBingPic, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val bingPic = response.body()!!.string()
                //this is a bug can not convert the body to teh url
                //not a bug ,usr the string() can convert the httpbody to url correctly
                val editor = PreferenceManager.getDefaultSharedPreferences(this@WeatherInfoActivity).edit()
                editor.putString("bing_pic", bingPic)
                editor.apply()
                runOnUiThread { Glide.with(this@WeatherInfoActivity).load(bingPic).into(img_bg_bing) }
            }
        })
    }

    fun closeDrawer(){
        drawer_layout_fixed.closeDrawers()
    }

    fun refresh(){
        swipe_refresh_weather.isRefreshing = true
    }
}
