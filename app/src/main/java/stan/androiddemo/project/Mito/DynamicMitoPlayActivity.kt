package stan.androiddemo.project.Mito

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_dynamic_mito_play.*
import stan.androiddemo.Model.ResultInfo
import stan.androiddemo.R
import stan.androiddemo.project.Mito.Model.ImageSetInfo
import java.io.File

class DynamicMitoPlayActivity : AppCompatActivity() {

    lateinit var imageSet:ImageSetInfo
    var videoLink = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dynamic_mito_play)

        imageSet = intent.getParcelableExtra("video")
        title = imageSet.title
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        video_view.setOnPreparedListener {
            progress_bar.clearAnimation()
            progress_bar.visibility = View.GONE
        }

        txt_download.setOnClickListener {

            val request = DownloadManager.Request(Uri.parse(videoLink))
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
            request.setTitle("下载")
            request.setDescription("正在下载动态壁纸$title")
            request.setAllowedOverRoaming(true)


            val pictureFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absoluteFile
            val fileDir = File(pictureFolder,"Mito")
            if (!fileDir.exists()){
                fileDir.mkdir()
            }
            request.setDestinationInExternalFilesDir(this,fileDir.absolutePath,"$title.mp4")
            val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val id = downloadManager.enqueue(request)

        }


        getVideoLink()
    }

    private fun getVideoLink(){
        ImageSetInfo.getVideoLink(imageSet.url){res:ResultInfo ->
           runOnUiThread {

               if (res.code != 0) {
                   Toast.makeText(this@DynamicMitoPlayActivity,res.message, Toast.LENGTH_LONG).show()

                   return@runOnUiThread
               }
               val url = res.data!! as String
                videoLink = url
               video_view.setVideoPath(url)

               val mediaController = MediaController(this)
               video_view.setMediaController(mediaController)
               video_view.requestFocus()
               video_view.start()
           }
        }
    }
}
