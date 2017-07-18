package stan.androiddemo.tool

import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Created by hugfo on 2017/7/18.
 */
class HttpTool{
   companion object {
       fun sendOKHttpRequest(address: String, callback: okhttp3.Callback) {
           val client = OkHttpClient()
           val request = Request.Builder().url(address).build()
           client.newCall(request).enqueue(callback)
       }
   }
}