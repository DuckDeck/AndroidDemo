package stan.androiddemo.project.Mito

import android.net.Uri
import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_dynamic_mito_play.*
import stan.androiddemo.Model.ResultInfo
import stan.androiddemo.R
import stan.androiddemo.project.Mito.Model.ImageSetInfo

class DynamicMitoPlayActivity : AppCompatActivity() {

    lateinit var imageSet:ImageSetInfo

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

               video_view.setVideoPath(url)

               val mediaController = MediaController(this)
               video_view.setMediaController(mediaController)
               video_view.requestFocus()
               video_view.start()
           }
        }
    }
}
