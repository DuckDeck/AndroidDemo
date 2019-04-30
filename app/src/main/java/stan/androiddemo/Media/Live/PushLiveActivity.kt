package stan.androiddemo.Media.Live

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Camera
import android.hardware.camera2.CameraAccessException
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.Toast
import com.github.faucamp.simplertmp.RtmpHandler
import kotlinx.android.synthetic.main.activity_push_live.*
import net.ossrs.yasea.SrsEncodeHandler
import net.ossrs.yasea.SrsPublisher
import net.ossrs.yasea.SrsRecordHandler
import stan.androiddemo.R
import java.io.IOException
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.net.SocketException

class PushLiveActivity : AppCompatActivity(),SrsEncodeHandler.SrsEncodeListener,RtmpHandler.RtmpListener,SrsRecordHandler.SrsRecordListener {



    lateinit var  publisher:SrsPublisher



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_push_live)





        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf("android.permission.CAMERA"),0x0001)
            }
            else{
               startPullLive()
            }
        }
        catch (e: CameraAccessException){
            e.printStackTrace()
        }


        btn_back.setOnClickListener {
            onBackPressed()
        }

        btn_switch_camera.setOnClickListener {
            publisher.switchCameraFace((publisher.cameraId + 1) % Camera.getNumberOfCameras())
        }







    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            0x0001->{
                val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (cameraAccepted){
                   startPullLive()
                }
                else{
                    Toast.makeText(this,"请开启应用拍照权限",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun startPullLive(){
        publisher = SrsPublisher(push_view)
        publisher.setEncodeHandler(SrsEncodeHandler(this))
        publisher.setRtmpHandler(RtmpHandler((this)))
        publisher.setRecordHandler(SrsRecordHandler(this))

        publisher.setPreviewResolution(1920,1200)
        publisher.setOutputResolution(1200,1920)
        publisher.setVideoHDMode()
        publisher.startPublish("rtmp://144.34.157.61:1935/mylive/44")
        publisher.startCamera()
    }


    override fun onBackPressed() {
        super.onBackPressed()
        publisher.stopCamera()
    }

    override fun onNetworkWeak() {}

    override fun onNetworkResume() {}

    override fun onEncodeIllegalArgumentException(e: IllegalArgumentException?) {}


    override fun onRtmpConnecting(msg: String?) {
        Toast.makeText(applicationContext,"连接中...",Toast.LENGTH_SHORT).show()
    }

    override fun onRtmpConnected(msg: String?) {
        Toast.makeText(applicationContext,"连接中成功",Toast.LENGTH_SHORT).show()
    }

    override fun onRtmpVideoStreaming() {}

    override fun onRtmpAudioStreaming() {}

    override fun onRtmpStopped() {}

    override fun onRtmpDisconnected() {
        Toast.makeText(applicationContext,"直播断开连接",Toast.LENGTH_SHORT).show()    }

    override fun onRtmpVideoFpsChanged(fps: Double) {
        Log.i("Output Fps:%f",fps.toString())
    }

    override fun onRtmpVideoBitrateChanged(bitrate: Double) {
        if(bitrate / 1000 > 0){
            Log.i("Video bitrate :%f kbps",(bitrate/1000).toString())
        }
        else{
            Log.i("Video bitrate :%d kbps",bitrate.toString())
        }
    }

    override fun onRtmpAudioBitrateChanged(bitrate: Double) {
        if(bitrate / 1000 > 0){
            Log.i("Video bitrate :%f kbps",(bitrate/1000).toString())
        }
        else{
            Log.i("Video bitrate :%d kbps",bitrate.toString())
        }
    }

    override fun onRtmpSocketException(e: SocketException?) {
        Log.e("onRtmpSocketException",e?.localizedMessage)
    }

    override fun onRtmpIOException(e: IOException?) {}

    override fun onRtmpIllegalArgumentException(e: IllegalArgumentException?) {}

    override fun onRtmpIllegalStateException(e: IllegalStateException?) {}

    override fun onRecordPause() {}

    override fun onRecordResume() {}

    override fun onRecordStarted(msg: String?) {
        Toast.makeText(applicationContext,msg,Toast.LENGTH_SHORT).show()
    }

    override fun onRecordFinished(msg: String?) {
        Toast.makeText(applicationContext,msg,Toast.LENGTH_SHORT).show()
    }

    override fun onRecordIllegalArgumentException(e: IllegalArgumentException?) {}

    override fun onRecordIOException(e: IOException?) {}

}
