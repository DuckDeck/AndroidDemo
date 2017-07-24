package stan.androiddemo.tool

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Created by hugfo on 2017/7/18.
 */
class HttpTool{
   companion object {
       fun sendOKHttpRequest(address: String, callback: okhttp3.Callback) {
           Log.e("sendOKHttpRequest, 请求地址",address)
           val client = OkHttpClient()
           val request = Request.Builder().header("Connection","close").url(address).build()

           client.newCall(request).enqueue(callback)
       }
   }
}