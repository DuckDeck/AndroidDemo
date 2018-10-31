package stan.androiddemo.tool.ffmpeg
import java.lang.System

class FFmpegBridge{


     companion object {
         init {
             System.loadLibrary("avutil");
             System.loadLibrary("fdk-aac");
             System.loadLibrary("avcodec");
             System.loadLibrary("avformat");
             System.loadLibrary("swscale");
             System.loadLibrary("swresample");
             System.loadLibrary("avfilter");
             System.loadLibrary("native-lib")

         }
         external fun ffmpegInfo(): String
     }

}