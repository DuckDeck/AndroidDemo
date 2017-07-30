package download.demo

import android.os.AsyncTask
import android.os.Environment
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.InputStream
import java.io.RandomAccessFile
import kotlin.Exception

/**
 * Created by hugfo on 2017/7/9.
 */
class DownloadTask: AsyncTask<String, Int, Int> {
    val TYPE_SUCCESS = 0
    val TYPE_FAILED = 1
    val TYPE_PAUSED = 2
    val TYPE_CANCLED = 3
    var listener: DownloadListener
    var isCanceled = false
    var isPaused = false
    var lastProgress = 0

    // when use this as default constructor, the class name () must remove
    constructor(listener:DownloadListener){
        this.listener = listener
    }


    override fun doInBackground(vararg p0: String?): Int {
        var inputStream:InputStream? = null
        var savedFile:RandomAccessFile? = null
        var file:File? = null
        try {
            var downloadLength:Long = 0
            val downloadUrl = p0[0]
            val fileName = downloadUrl!!.substring(downloadUrl.lastIndexOf("/"))
            val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path
            file = File(directory + fileName)
            if(file.exists()){
                downloadLength = file.length()
            }
            val contentLength = getContentLength(downloadUrl)
            if (contentLength == 0.0.toLong()){
                return TYPE_FAILED
            }
            else if(contentLength == downloadLength){
                return  TYPE_SUCCESS
            }
            val client = OkHttpClient()
            val request = Request.Builder().addHeader("RANGE", "bytes=$downloadLength-").url(downloadUrl).build()
            val response = client.newCall(request).execute()
            if(response != null){
                inputStream = response.body()!!.byteStream()
                savedFile = RandomAccessFile(file,"rw")
                savedFile.seek(downloadLength)
                val b = ByteArray(1024)
                var total = 0
                var length = inputStream.read(b)

                while (length != -1){
                    if (isCanceled){
                        return TYPE_CANCLED
                    }
                    else if (isPaused){
                        return TYPE_PAUSED
                    }
                    else{
                        total += length
                        savedFile.write(b,0,length)
                        var progress =( (total + downloadLength)* 100 / contentLength).toInt()
                        //这个就去会让系统调用onProgressUpdate方法
                        publishProgress(progress)
                    }
                    length = inputStream.read(b)
                }
                response!!.body()!!.close()
                return TYPE_SUCCESS
            }
        }
        catch (e:Exception){
            e.printStackTrace()
        }
        finally {
            try {
                if (inputStream != null){
                    inputStream.close()
                }
                if (savedFile != null){
                    savedFile .close()
                }
                if(isCanceled && file!= null){
                    file.delete()
                }
            }
            catch (e:Exception){
                e.fillInStackTrace()
            }

        }
        return  TYPE_FAILED
        //所有的return 方法会调用onPostExecute方法
    }


    override fun onProgressUpdate(vararg values: Int?) {
        val progress = values[0]
        if (progress != null) {
            if (progress > lastProgress){
                listener.onProgress(progress)
                lastProgress = progress
            }
        }
    }

    override fun onPostExecute(result: Int?) {
        when(result){
           TYPE_SUCCESS->listener.onSuccess()
            TYPE_FAILED->listener.onFailed()
            TYPE_PAUSED->listener.onPaused()
            TYPE_CANCLED->listener.onCanceled()
        }
    }

    fun pauseDownload(){
        isPaused = true
    }

    fun  cancelDownload(){
        isCanceled = true
    }

    //获取下载内容长度
    fun getContentLength(downloadUrl:String):Long{
        val client = OkHttpClient()
        val request = Request.Builder().url(downloadUrl).build()
        val response = client.newCall(request).execute()
        if (response != null && response.isSuccessful){
            val contentLength = response.body()!!.contentLength()
            response.close()
            return contentLength
        }
        return  0
    }
}