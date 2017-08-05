package stan.androiddemo.tool

import android.util.Log
import okhttp3.*

/**
 * Created by hugfo on 2017/7/18.
 */
class HttpTool{
   companion object {
       fun get(address: String, callback: okhttp3.Callback) {
           Log.e("Get, 请求地址",address)
           val client = OkHttpClient()
           val request = Request.Builder().header("Connection","close").url(address).build()
           client.newCall(request).enqueue(callback)
       }

       fun post(address: String,map: HashMap<String,String>,callback: Callback){
           Log.e("Post, 请求地址",address)
           val client = OkHttpClient()
           val b = FormBody.Builder()
           for ((key, value) in map){
               b.add(key, value)
           }
           val body =b.build()
           val request = Request.Builder().header("Connection","close").post(body).url(address).build()
           client.newCall(request).enqueue(callback)
       }

       fun post(address: String,head:HashMap<String,String>,map: HashMap<String,String>,callback: Callback){
           Log.e("Post, 请求地址",address)
           val client = OkHttpClient()
           val sb = StringBuilder()
           for ((key, value) in map){
               sb.append(key)
               sb.append("=")
               sb.append(value)
               sb.append("&")
           }
           sb.removeSuffix("&")
           val b =  RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),sb.toString())
           val req = Request.Builder()
           for ((key, value) in head){
               req.addHeader(key, value)
           }
           val request = req.post(b).url(address).build()
           client.newCall(request).enqueue(callback)
       }
   }
}