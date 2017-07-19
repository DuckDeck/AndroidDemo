package stan.androiddemo.project.weather

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager

import stan.androiddemo.R


class WeatherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)



        val prefs =  PreferenceManager.getDefaultSharedPreferences(this)
        if (prefs.getString("weather",null) != null){
            val intent = Intent(this,WeatherInfoActivity::class.java)
            startActivity(intent)
        }
    }

}
