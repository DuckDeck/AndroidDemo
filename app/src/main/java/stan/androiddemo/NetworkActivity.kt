package stan.androiddemo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_network.*
import stan.androiddemo.network.networktest.NetworkTestActivity

class NetworkActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network)
        title = ""
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        val adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,
                arrayListOf("网络测试"))
        list_view.adapter = adapter

        list_view.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val title = (view as TextView).text
            when(title){
                "网络测试"->
                {
                    val intent = Intent(this, NetworkTestActivity::class.java)
                    startActivity(intent)
                }

            }
        }
    }
}
