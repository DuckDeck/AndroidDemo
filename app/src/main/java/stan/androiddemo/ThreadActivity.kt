package stan.androiddemo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_thread.*
import stan.androiddemo.Thread.Service.ServiceActivity

class ThreadActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thread)
        title = ""
        setSupportActionBar(toolbar_main_thread)

        toolbar_main_thread.setNavigationOnClickListener {
            onBackPressed()
        }
        val adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,
                arrayListOf("Service"))
        list_view_thread.adapter = adapter

        list_view_thread.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val title = (view as TextView).text
            when(title){
                "Service"->{
                    val intent = Intent(this,ServiceActivity::class.java)
                    startActivity(intent)
                }

            }
        }
    }
}
