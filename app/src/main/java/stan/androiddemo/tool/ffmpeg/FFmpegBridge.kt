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

    }

    external fun ffmpegInfo(): String

}