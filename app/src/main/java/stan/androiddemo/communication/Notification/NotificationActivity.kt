package stan.androiddemo.communication.Notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.NotificationCompat
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_notifcation.*
import stan.androiddemo.R

class NotificationActivity : AppCompatActivity() {

    lateinit var intentFilter:IntentFilter
    lateinit var networkChangeReceiver:NetworkChangeReceiver


    lateinit var intentFilterLocal:IntentFilter


    lateinit var localReceive:LocalReceive

    lateinit var localBroadcastManager:LocalBroadcastManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifcation)

        intentFilter = IntentFilter()
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        networkChangeReceiver = NetworkChangeReceiver()
        registerReceiver(networkChangeReceiver,intentFilter)




        localBroadcastManager = LocalBroadcastManager.getInstance(this)


        txt_send_notif.setOnClickListener {
            val intent = Intent("com.androiddemo.broadcast.CUSTOMBROADCAST")
            //sendBroadcast(intent) 这是标准广播
            sendOrderedBroadcast(intent,null) //这是有序广播
            //这种广播其他APP也可以收到，这样肯定不行
            //可以使用本地广播



        }


        intentFilterLocal = IntentFilter()
        intentFilterLocal.addAction("com.androiddemo.broadcast.LOCALBROADCAST")
        localReceive = LocalReceive()
        localBroadcastManager.registerReceiver(localReceive,intentFilterLocal)


        txt_send_notif_local.setOnClickListener {

            val intentLocal = Intent("com.androiddemo.broadcast.LOCALBROADCAST")
            localBroadcastManager.sendBroadcast(intentLocal)
        }







        btn_send_notif.setOnClickListener {
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val intent = Intent(this,PendingIntentActivity::class.java)
            val pi = PendingIntent.getActivity(this,0,intent,0)
            val notification = NotificationCompat.Builder(this).setContentTitle("This is the content title")
                    .setContentText("This is the content text")
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(resources,R.mipmap.ic_launcher))
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .build()
            manager.notify(1,notification)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkChangeReceiver)
        localBroadcastManager.unregisterReceiver(localReceive)
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

    inner class LocalReceive:BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            Toast.makeText(p0,"receive local broadcast",Toast.LENGTH_LONG).show()
        }

    }
}
