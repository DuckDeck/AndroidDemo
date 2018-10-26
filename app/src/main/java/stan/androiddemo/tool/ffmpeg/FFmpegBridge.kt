package stan.androiddemo.tool.ffmpeg
import java.lang.System

class FFmpegBridge{
    init {
        System.loadLibrary("avutil");
        System.loadLibrary("fdk-aac");
        System.loadLibrary("avcodec");
        System.loadLibrary("avformat");
        System.loadLibrary("swscale");
        System.loadLibrary("swresample");
        System.loadLibrary("avfilter");
        System.loadLibrary("jx_ffmpeg_jni");
    }

    /**
     *
     * @return 返回ffmpeg的编译信息
     */
    external fun getFFmpegConfig(): String
    /**
     *  命令形式运行ffmpeg
     * @param cmd
     * @return 返回0表示成功
     */
    private external fun jxCMDRun(cmd: Array<String>): Int
    /**
     * 编码一帧视频，暂时只能编码yv12视频
     * @param data
     * @return
     */
    external fun encodeFrame2H264(data: ByteArray): Int
    /**
     * 编码一帧音频,暂时只能编码pcm音频
     * @param data
     * @return
     */
    external fun encodeFrame2AAC(data: ByteArray): Int

    /**
     * 录制结束
     * @return
     */
    external fun recordEnd(): Int

    /**
     * 初始化
     * @param debug
     * @param logUrl
     */
    external fun initJXFFmpeg(debug: Boolean, logUrl: String)

    /**
     *
     * @param mediaBasePath 视频存放目录
     * @param mediaName 视频名称
     * @param filter 旋转镜像剪切处理
     * @param in_width 输入视频宽度
     * @param in_height 输入视频高度
     * @param out_height 输出视频高度
     * @param out_width 输出视频宽度
     * @param frameRate 视频帧率
     * @param bit_rate 视频比特率
     * @return
     */

    external fun prepareJXFFmpegEncoder(mediaBasePath: String, mediaName: String, filter: Int, in_width: Int, in_height: Int, out_width: Int, out_height: Int, frameRate: Int, bit_rate: Long): Int

    /**
     * 命令形式执行
     * @param cmd
     */
    fun jxFFmpegCMDRun(cmd: String): Int {
        val regulation = "[ \\t]+"
        val split = cmd.split(regulation.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        return jxCMDRun(split)
    }

    /**
     * 底层回调
     * @param state
     * @param what
     */
//    @Synchronized
//    fun notifyState(state: Int, what: Float) {
//        for (listener in listeners) {
//            if (listener != null) {
//                if (state == ALL_RECORD_END) {
//                    listener!!.allRecordEnd()
//                }
//            }
//        }
//    }

    /**
     * 注册录制回调
     * @param listener
     */
    /*
    fun registFFmpegStateListener(listener: FFmpegStateListener) {

        if (!listeners.contains(listener)) {
            listeners.add(listener)
        }
    }

    fun unRegistFFmpegStateListener(listener: FFmpegStateListener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener)
        }
    }

    interface FFmpegStateListener {
        fun allRecordEnd()
    }
    */

}