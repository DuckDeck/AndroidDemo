package stan.androiddemo.communication.Notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import stan.androiddemo.R

class NotificationActivity : AppCompatActivity() {

    lateinit var intentFilter:IntentFilter
    lateinit var networkChangeReceiver:NetworkChangeReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifcation)

        intentFilter = IntentFilter()
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        networkChangeReceiver = NetworkChangeReceiver()
        registerReceiver(networkChangeReceiver,intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkChangeReceiver)
    }

    inner class NetworkChangeReceiver: BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
         //  Toast.makeText(p0,"network changes",Toast.LENGTH_LONG).show()
            val connectionManage = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectionManage.activeNetworkInfo //need add access network status
            if (networkInfo != null && networkInfo.isAvailable){
                Toast.makeText(p0,"network is available",Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(p0,"network is unavailable",Toast.LENGTH_LONG).show()
            }

        }
        //测试确实很OK
    }
}
