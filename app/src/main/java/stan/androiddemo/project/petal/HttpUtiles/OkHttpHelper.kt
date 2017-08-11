package stan.androiddemo.project.petal.HttpUtiles

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import stan.androiddemo.tool.Logger

/**
 * Created by stanhu on 11/8/2017.
 */
class OkHttpHelper{
    companion object {
        val progressBean = ProgressBean()
        val  mProgressHandler:ProgressHandler? = null
        fun addLogClient(builder:OkHttpClient.Builder):OkHttpClient.Builder{
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BASIC
            builder.addInterceptor(logging)
            return builder
        }
        fun addProgressClient(builder:OkHttpClient.Builder,listener:OnProgressResponseListener):OkHttpClient.Builder{
            builder.addNetworkInterceptor {
                Logger.d("start intercept")
                val originalResponse = it.proceed(it.request())
                return@addNetworkInterceptor originalResponse.newBuilder().body(
                    ProgressResponseBody(originalResponse.body(),listener)
                ).build()
            }
            return builder
        }
    }
}

