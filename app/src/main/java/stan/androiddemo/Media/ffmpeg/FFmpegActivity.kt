package stan.androiddemo.Media.ffmpeg

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_ffmpeg.*
import stan.androiddemo.R
import stan.androiddemo.tool.ffmpeg.FFmpegBridge

class FFmpegActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ffmpeg)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        txtfinfo.text = FFmpegBridge.ffmpegInfo()
    }
}
