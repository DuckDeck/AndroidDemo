package download.demo

/**
 * Created by hugfo on 2017/7/9.
 */
public interface DownloadListener {
    fun onProgress(progress:Int)
    fun onSuccess()
    fun onFailed()
    fun onPaused()
    fun onCanceled()
}