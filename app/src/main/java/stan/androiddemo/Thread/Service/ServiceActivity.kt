package stan.androiddemo.Thread.Service

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import download.demo.DownloadService
import kotlinx.android.synthetic.main.activity_service.*
import stan.androiddemo.R

class ServiceActivity : AppCompatActivity() {
    val LOG = ServiceActivity::class.java.canonicalName

    lateinit var downlodBinderTest:DemoService.DownloadBinder

    val connectionTest = object:ServiceConnection{
        override fun onServiceDisconnected(p0: ComponentName) {

        }

        override fun onServiceConnected(p0: ComponentName, p1: IBinder) {
            downlodBinderTest = p1 as DemoService.DownloadBinder
            p1.startDownload()
            p1.getProgress()
        }

    }


    lateinit var downloadBinder:DownloadService.DownloadBinder

    val connection = object :ServiceConnection{
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            downloadBinder = p1 as DownloadService.DownloadBinder
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service)

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),1)
        }

        //start and bind service
        //一定要记得注册啊，不注册onServiceConnected这个方法不会跑的
        val intent = Intent(this,DownloadService::class.java)
        startService(intent)
        bindService(intent,connection,Context.BIND_AUTO_CREATE)


        btn_start_service.setOnClickListener {
            val intent = Intent(this,DemoService::class.java)
            startService(intent)
        }

        btn_stop_service.setOnClickListener {
            val intent = Intent(this,DemoService::class.java)
            stopService(intent)
        }

        btn_bind_service.setOnClickListener {
            val intent = Intent(this,DemoService::class.java)
            bindService(intent,connectionTest, Context.BIND_AUTO_CREATE)
        }

        btn_un_bind_service.setOnClickListener {
            unbindService(connectionTest)
        }

        btn_start_intent_service.setOnClickListener {
            Log.e(LOG,"Thread id is "+Thread.currentThread().id)
            val serviceIntent = Intent(this,DemoIntentService::class.java)
            startService(serviceIntent)
        }

        btn_start_download.setOnClickListener {
             val url = "https://d.ruanmei.com/qiyu/qiyusetup_2.11_x64.exe"
            downloadBinder.startDownload(url)
        }

        btn_pause_download.setOnClickListener {
            downloadBinder.pauseDownload()
        }

        btn_cancel_download.setOnClickListener {
            downloadBinder.cancelDownload()
        }



    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
    }
}
