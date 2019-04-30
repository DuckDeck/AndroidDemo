package stan.androiddemo.Media.Live

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
                p0?.seekTo(0)
                p0?.start()
            }

            override fun onError(p0: IMediaPlayer?, p1: Int, p2: Int): Boolean {
               return false
            }

            override fun onInfo(p0: IMediaPlayer?, p1: Int, p2: Int): Boolean {
                if(p1 == IMediaPlayer.MEDIA_INFO_BUFFERING_START){
                    txt_video_info.visibility = View.VISIBLE
                    video_view_ijk.visibility = View.GONE
                    Toast.makeText(this@PullLiveActivity,"直播结束",Toast.LENGTH_SHORT).show()
                }
                else if(p1 == IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START || p1 == IMediaPlayer.MEDIA_INFO_BUFFERING_END){
                    txt_video_info.visibility = View.GONE
                    video_view_ijk.visibility = View.VISIBLE
                    Toast.makeText(this@PullLiveActivity,"直播开始",Toast.LENGTH_SHORT).show()
                }
                return false
            }

            override fun onPrepared(p0: IMediaPlayer?) {
                p0?.start()
            }

            override fun onSeekComplete(p0: IMediaPlayer?) {
            }

            override fun onVideoSizeChanged(p0: IMediaPlayer?, p1: Int, p2: Int, p3: Int, p4: Int) {
            }
        })

    }

    override fun onStop() {
        super.onStop()
        IjkMediaPlayer.native_profileEnd()
    }

}
