package stan.androiddemo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import stan.androiddemo.tool.HttpTool

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, arrayListOf("项目Demo"))
        list_view_menu.adapter = adapter



        list_view_menu.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val title = (view as TextView).text
            when(title){
                "项目Demo"->
                {
                    val intent = Intent(this, ProjectActivity::class.java)
                    startActivity(intent)
                }

            }
        }
    }
}
