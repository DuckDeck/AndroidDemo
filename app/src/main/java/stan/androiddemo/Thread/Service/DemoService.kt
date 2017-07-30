package stan.androiddemo.Thread.Service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.IBinder
import android.support.v7.app.NotificationCompat
import android.util.Log
import stan.androiddemo.R

class DemoService : Service() {

    val LOG = DemoService::class.java.canonicalName

    var mBinder = DownloadBinder()

    override fun onBind(intent: Intent): IBinder? {
        // TODO: Return the communication channel to the service.
        return mBinder
    }

    override fun onCreate() {
        super.onCreate()
        Log.e(LOG,"onCreate executed")
        //create a front service
        val intent = Intent(this,ServiceActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this,0,intent,0)
        //可以让这个pendingIntent来启动activity
        val notification = NotificationCompat.Builder(this).setContentTitle("This is the content title")
                .setContentText("This is the content text").setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(resources,R.mipmap.ic_launcher))
                .setContentIntent(pendingIntent)
                .build()
        startForeground(1,notification)

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e(LOG,"onStartCommand executed")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Log.e(LOG,"onDestroy executed")
        super.onDestroy()
    }

    class DownloadBinder : Binder() {
        val LOG = DownloadBinder::class.java.canonicalName
        fun startDownload(){
            Log.e(LOG,"startDownload startDownload")
        }
        fun getProgress():Int{
            Log.e(LOG,"getProgress startDownload")
            return 0
        }
    }
}
