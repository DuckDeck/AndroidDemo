package stan.androiddemo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_project.*
import stan.androiddemo.project.FiveStroke.FiveStrokeActivity
import stan.androiddemo.project.Game2048.GameActivity
import stan.androiddemo.project.Mito.MitoActivity
import stan.androiddemo.project.novel.NovelSearchActivity
import stan.androiddemo.project.petal.Module.Guide.GuideActivity
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
                arrayListOf("Weather","Novel","美图","五笔查询","花瓣","Game2048"))
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
                "美图"->
                {
                    val intent = Intent(this, MitoActivity::class.java)
                    startActivity(intent)
                }
                "五笔查询"->
                {
                    val intent = Intent(this, FiveStrokeActivity::class.java)
                    startActivity(intent)
                }
                "花瓣"->
                {
                    val intent = Intent(this, GuideActivity::class.java)
                    startActivity(intent)
                }
                "Game2048"->
                {
                    val intent = Intent(this, GameActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}
