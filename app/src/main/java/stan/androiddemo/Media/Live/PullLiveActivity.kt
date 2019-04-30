package stan.androiddemo.Media.Live

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_pull_live.*
import stan.androiddemo.R
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import java.lang.Exception

class PullLiveActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pull_live)

        try {
            IjkMediaPlayer.loadLibrariesOnce(null)
            IjkMediaPlayer.native_profileBegin("libijkplayer.so")
        }
        catch (e:Exception){
            finish()
        }

        video_view_ijk.setVideoPath("http://144.34.157.61/mylive_hls/44.m3u8")
        video_view_ijk.setListener( object:VideoPlayerListener(){
            override fun onBufferingUpdate(p0: IMediaPlayer?, p1: Int) {

            }

            override fun onCompletion(p0: IMediaPlayer?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onError(p0: IMediaPlayer?, p1: Int, p2: Int): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onInfo(p0: IMediaPlayer?, p1: Int, p2: Int): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onPrepared(p0: IMediaPlayer?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onSeekComplete(p0: IMediaPlayer?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onVideoSizeChanged(p0: IMediaPlayer?, p1: Int, p2: Int, p3: Int, p4: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })



    }

}
