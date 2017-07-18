package stan.androiddemo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_project.*
import stan.androiddemo.project.weather.WeatherActivity


class ProjectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project)


        val adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, arrayListOf("weather"))
        list_view_project.adapter = adapter


        list_view_project.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val title = (view as TextView).text
            when(title){
                "weather"->
                {
                    val intent = Intent(this, WeatherActivity::class.java)
                    startActivity(intent)
                }

            }
        }
    }
}
