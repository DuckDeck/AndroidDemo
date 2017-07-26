package stan.androiddemo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_draw_view.*
import stan.androiddemo.drawview.drawtext.DrawTextActivity


class DrawViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw_view)
        title = ""
        setSupportActionBar(toolbar_main_draw)

        toolbar_main_draw.setNavigationOnClickListener {
            onBackPressed()
        }
        val adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,
                arrayListOf("DrawText"))
        list_view_draw.adapter = adapter

        list_view_draw.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val title = (view as TextView).text
            when(title){
                "DrawText"->
                {
                    val intent = Intent(this, DrawTextActivity::class.java)
                    startActivity(intent)
                }

            }
        }
    }
}
