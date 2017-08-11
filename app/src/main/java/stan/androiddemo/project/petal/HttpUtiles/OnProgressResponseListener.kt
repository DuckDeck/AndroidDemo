package stan.androiddemo.project.petal.HttpUtiles

/**
 * Created by stanhu on 11/8/2017.
 */
interface OnProgressResponseListener {
    fun onResponseProgress(bytesRead: Long, contentLength: Long, done: Boolean)
}