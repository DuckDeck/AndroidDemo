package stan.androiddemo.Media.Live

import android.content.Intent
import kotlinx.android.synthetic.main.activity_live.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import stan.androiddemo.Media.TakePhoto.TakePhotoActivity
import stan.androiddemo.R

class LiveActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live)

        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = ""

        val adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,
                arrayListOf("推流"))
        list_view_layout.adapter = adapter

        list_view_layout.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val title = (view as TextView).text
            when(title){

                "推流"->
                {
                    val intent = Intent(this, PushLiveActivity::class.java)
                    startActivity(intent)
                }


            }
        }
    }
}
