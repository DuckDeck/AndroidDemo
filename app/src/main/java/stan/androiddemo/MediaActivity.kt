package stan.androiddemo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_media.*
import stan.androiddemo.layout.keyboard.KeyboardActivity

class MediaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = ""
        setSupportActionBar(toolbar)
        val adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,
                arrayListOf("拍照"))
        list_view_layout.adapter = adapter

        list_view_layout.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val title = (view as TextView).text
            when(title){

                "拍照"->
                {
                    val intent = Intent(this, KeyboardActivity::class.java)
                    startActivity(intent)
                }

            }
        }
    }

}
