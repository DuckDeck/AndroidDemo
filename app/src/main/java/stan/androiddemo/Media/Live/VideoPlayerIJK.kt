package stan.androiddemo.Media.Live


import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.FrameLayout
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import java.io.IOException


class VideoPlayerIJK : FrameLayout{

    private var mediaPlayer:IMediaPlayer? = null

    var path = ""

    private var listener:VideoPlayerListener? = null
    private var surfaceView:SurfaceView? = null

    lateinit var mContext:Context

    constructor(context: Context):super(context){
        initVideoView(context)
    }

    constructor(context: Context,attrs:AttributeSet):super(context,attrs){
        initVideoView(context)
    }

    constructor(context: Context,attrs: AttributeSet,defStyleAttr:Int):super(context,attrs,defStyleAttr){
        initVideoView(context)
    }

    fun initVideoView(context: Context){
        mContext = context

        //focusable = true
    }

    fun setVideoPath(path:String){
        if (path.isNullOrEmpty()){
            this.path = path
            createSurfaceView()
        }
        else{
            if(surfaceView == null){
                createSurfaceView()
            }
            this.path = path
            load()
        }

    }

    fun createSurfaceView(){
        surfaceView = SurfaceView(mContext)
        surfaceView!!.holder.addCallback(LmnSurfaceCallback())
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,Gravity.CENTER)
        surfaceView!!.layoutParams = layoutParams
        addView(surfaceView)
    }

    //只有声明inner才是内部灶，才能调用外部类的方法
    inner class LmnSurfaceCallback:SurfaceHolder.Callback{
        override fun surfaceCreated(p0: SurfaceHolder?) {}
        override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
            load()
        }

        override fun surfaceDestroyed(p0: SurfaceHolder?) {}
    }

    fun load(){
        createPlayer()
        try {
            mediaPlayer?.dataSource = path

        }
        catch (e:IOException){
            e.printStackTrace()
        }
        mediaPlayer?.setDisplay(surfaceView!!.holder)
        mediaPlayer?.prepareAsync()
    }

    fun createPlayer(){
        if(mediaPlayer != null){
            mediaPlayer!!.stop()
            mediaPlayer!!.setDisplay(null)
            mediaPlayer!!.release()
        }
        val ijkMediaPlayer = IjkMediaPlayer()
        //ijkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER,"mediacodec",1)
        mediaPlayer = ijkMediaPlayer
        if(listener != null){
            mediaPlayer!!.setOnPreparedListener(listener!!)
            mediaPlayer!!.setOnInfoListener(listener!!)
            mediaPlayer!!.setOnSeekCompleteListener(listener!!)
            mediaPlayer!!.setOnBufferingUpdateListener(listener!!)
            mediaPlayer!!.setOnErrorListener(listener!!)
        }
    }

    fun setListener(listener:VideoPlayerListener){
        this.listener = listener
        mediaPlayer?.setOnPreparedListener(listener)
    }

    fun start(){
        mediaPlayer?.start()
    }

    fun release(){
        mediaPlayer?.reset()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun pause(){
        mediaPlayer?.pause()
    }

    fun reset(){
        mediaPlayer?.reset()
    }

    fun getDuration():Int{
        if(mediaPlayer != null){
            return mediaPlayer!!.duration.toInt()
        }
        return 0
    }

    fun getCurrentPosition():Long{
        if(mediaPlayer != null){
            return mediaPlayer!!.currentPosition
        }
        return 0
    }

    fun seekTo(l:Long){
        mediaPlayer?.seekTo(l)
    }


}
abstract  class VideoPlayerListener : IMediaPlayer.OnBufferingUpdateListener, IMediaPlayer.OnCompletionListener, IMediaPlayer.OnPreparedListener, IMediaPlayer.OnInfoListener, IMediaPlayer.OnVideoSizeChangedListener, IMediaPlayer.OnErrorListener, IMediaPlayer.OnSeekCompleteListener


