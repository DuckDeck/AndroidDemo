package stan.androiddemo.Media.Live

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_pull_live.*
import master.flame.danmaku.controller.DrawHandler
import master.flame.danmaku.danmaku.model.BaseDanmaku
import master.flame.danmaku.danmaku.model.DanmakuTimer
import master.flame.danmaku.danmaku.model.IDanmakus
import master.flame.danmaku.danmaku.model.IDisplayer
import master.flame.danmaku.danmaku.model.android.DanmakuContext
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser
import stan.androiddemo.R
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import java.lang.Exception
import java.util.*

class PullLiveActivity : AppCompatActivity() {



    lateinit var  danmakuContext:DanmakuContext

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

        video_view_ijk.setVideoPath("rtmp://bqbbq.com/mylive/44")
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





        danmaku_view.setCallback(object:DrawHandler.Callback{
            override fun prepared() {
                danmaku_view.start()
            }

            override fun updateTimer(timer: DanmakuTimer?) {

            }

            override fun danmakuShown(danmaku: BaseDanmaku?) {

            }

            override fun drawingFinished() {
            }
        })




        danmaku_view.enableDanmakuDrawingCache(true)

        danmakuContext = DanmakuContext.create()
        danmakuContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN,3.0f)
        danmakuContext.isDuplicateMergingEnabled = false
        danmakuContext.setScrollSpeedFactor(1.2f)
        danmaku_view.prepare(getDeaultDanmakuParser(),danmakuContext)
        danmaku_view.showFPS(true)


    }

    fun getDeaultDanmakuParser():BaseDanmakuParser{
        return object:BaseDanmakuParser(){
            override fun parse(): IDanmakus {
                return danmakus
            }
        }
    }


    fun generateSomeDanmaku(){
        for (i in 0 until 100){
            val time = Random().nextInt(300)
            val content = "" + time + time
            addDanmaku(content,false)
        }
    }

    fun addDanmaku(content:String,withBorder:Boolean){
        val danmaku = danmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL)
        danmaku.text = content
        danmaku.padding = 5
        danmaku.textSize = 20.0f
        danmaku.textColor = Color.WHITE
        val time = Random().nextInt(900)
        danmaku.time = danmaku_view.currentTime + time * 10
        if (withBorder){
            danmaku.borderColor = Color.GREEN
        }
        danmaku_view.addDanmaku(danmaku)
    }

    override fun onPause() {
        super.onPause()
        if (danmaku_view != null && danmaku_view.isPrepared){
            danmaku_view.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        if (danmaku_view != null && danmaku_view.isPrepared && danmaku_view.isPaused){
           danmaku_view.resume()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (danmaku_view != null){
            danmaku_view.release()

        }
    }

    override fun onStop() {
        super.onStop()
        IjkMediaPlayer.native_profileEnd()
    }

}
