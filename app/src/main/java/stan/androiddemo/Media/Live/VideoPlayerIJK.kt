package stan.androiddemo.Media.Live


import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.FrameLayout
import tv.danmaku.ijk.media.player.IMediaPlayer


class VideoPlayerIJK : FrameLayout{

    private var mediaPlayer:IMediaPlayer? = null

    var path = ""

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
    }

    fun createSurfaceView(){
        surfaceView = SurfaceView(mContext)
        surfaceView!!.holder.addCallback(LmnSurfaceCallback())
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

    }
}

