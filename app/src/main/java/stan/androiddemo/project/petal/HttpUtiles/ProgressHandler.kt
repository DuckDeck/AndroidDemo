package stan.androiddemo.project.petal.HttpUtiles

import android.os.Handler
import android.os.Looper
import android.os.Message

/**
 * Created by stanhu on 11/8/2017.
 */
abstract class ProgressHandler{
    protected abstract fun sendMessage(progressBean: ProgressBean)

    protected abstract fun handleMessage(message: Message)

    protected abstract fun onProgress(progress: Long, total: Long, done: Boolean)

    protected class ResponseHandler(private val mProgressHandler: ProgressHandler, looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            mProgressHandler.handleMessage(msg)
        }
    }
}