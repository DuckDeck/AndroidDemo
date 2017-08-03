package stan.androiddemo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_project.*
import stan.androiddemo.project.belle.BelleActivity
import stan.androiddemo.project.novel.NovelSearchActivity
import stan.androiddemo.project.weather.WeatherActivity


class ProjectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project)
        title = ""
        setSupportActionBar(toolbar_main_project)

        toolbar_main_project.setNavigationOnClickListener {
            onBackPressed()
        }
        val adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,
                arrayListOf("Weather","Novel","belle"))
        list_view_project.adapter = adapter

        list_view_project.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val title = (view as TextView).text
            when(title){
                "Weather"->
                {
                    val intent = Intent(this, WeatherActivity::class.java)
                    startActivity(intent)
                }
                "Novel"->
                {
                    val intent = Intent(this, NovelSearchActivity::class.java)
                    startActivity(intent)
                }
                "belle"->
                {
                    val intent = Intent(this, BelleActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}
