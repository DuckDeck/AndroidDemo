package stan.androiddemo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_communication.*
import stan.androiddemo.communication.IPC.IPCActivity
import stan.androiddemo.communication.Notification.NotificationActivity

class CommunicationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_communication)

        val adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, arrayListOf("IPC","Notification"))
        list_view_communication.adapter = adapter

        list_view_communication.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val title = (view as TextView).text
            when(title){
                "IPC"->
                {
                    val intent = Intent(this, IPCActivity::class.java)
                    startActivity(intent)
                }
                "Notification"->
                {
                    val intent = Intent(this, NotificationActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}
