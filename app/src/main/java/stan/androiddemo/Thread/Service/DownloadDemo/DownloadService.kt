package download.demo

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.Environment
import android.os.IBinder
import android.widget.Toast
import stan.androiddemo.Thread.Service.ServiceActivity
import stan.androiddemo.R
import java.io.File


class DownloadService : Service(), DownloadListener {

    var downloadTask: DownloadTask? = null
    lateinit var downloadUrl:String


    override fun onProgress(progress: Int) {
        getNotificationManager().notify(1,getNotification("Downloading....",progress))
    }

    override fun onSuccess() {
        downloadTask = null
        stopForeground(true)
        getNotificationManager().notify(1,getNotification("Download Success",-1))
        Toast.makeText(this,"Download Success",Toast.LENGTH_LONG).show()
    }

    override fun onFailed() {
        downloadTask = null
        stopForeground(true)
        getNotificationManager().notify(1,getNotification("Download Failed",-1))
        Toast.makeText(this,"Download Failed",Toast.LENGTH_LONG).show()
    }

    override fun onPaused() {
       downloadTask = null
       Toast.makeText(this,"Download Paused",Toast.LENGTH_LONG).show()

    }

    override fun onCanceled() {
        downloadTask = null
        stopForeground(true)
        Toast.makeText(this,"Download Canceled",Toast.LENGTH_LONG).show()
    }

    val mBinder = DownloadBinder()

    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }

    // mark ad inner class can usr outer class property
    inner class DownloadBinder:Binder(){
        fun startDownload(url:String){
            if (downloadTask == null){
                downloadTask = DownloadTask(this@DownloadService)
            }
            downloadUrl = url
            downloadTask!!.execute(downloadUrl)
            startForeground(1,getNotification("Downloading...",0))
            Toast.makeText(this@DownloadService,"Downloading...",Toast.LENGTH_LONG).show()
        }

        public fun pauseDownload(){
            if (downloadTask != null){
                downloadTask!!.pauseDownload()
            }
        }

        public fun  cancelDownload(){
            if (downloadTask != null){
                downloadTask!!.cancelDownload()
            }
            else{
                if (downloadUrl != null){
                    val fileName = downloadUrl.substring(downloadUrl!!.lastIndexOf("/"))
                    val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path
                    val file = File(directory + fileName)
                    if (file.exists()){
                        file.delete()
                    }
                    getNotificationManager().cancel(1)
                    stopForeground(true)
                    Toast.makeText(this@DownloadService,"Download Canceled",Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun getNotificationManager():NotificationManager{
        return getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    fun getNotification(title:String,progress:Int): Notification {
        val intent = Intent(this,ServiceActivity::class.java)
        val pi = PendingIntent.getActivity(this,0,intent,0)
        val builder = android.support.v7.app.NotificationCompat.Builder(this)
        builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.setLargeIcon(BitmapFactory.decodeResource(resources,R.mipmap.ic_launcher))
        builder.setContentIntent(pi)
        builder.setContentTitle(title)
        if (progress>0){
            builder.setContentText(progress.toString()+"%")
            builder.setProgress(100,progress,false)
        }
        return builder.build()
    }

}
