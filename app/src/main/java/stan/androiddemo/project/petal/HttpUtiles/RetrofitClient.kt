package stan.androiddemo.project.petal.HttpUtiles

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import stan.androiddemo.project.petal.HttpUtiles.Converter.AvatarConverter

/**
 * Created by stanhu on 11/8/2017.
 */
class RetrofitClient{
    companion object {
        val mBaseUrl = "https://api.huaban.com/"

        var gson = Gson()

        val httpClient = OkHttpClient.Builder()

        val builder = Retrofit.Builder().baseUrl(mBaseUrl).addCallAdapterFactory(RxJavaCallAdapterFactory.create())

        fun <S> createService(serviceClass: Class<S>): S {
            val retrofit = builder
                    .client(OkHttpHelper.addLogClient(httpClient).build())
                    .addConverterFactory(AvatarConverter.create(gson))
                    .build()
            return retrofit.create(serviceClass)
        }

        fun <S> createService(serviceClass: Class<S>, listener: OnProgressResponseListener): S {
            val retrofit = builder
                    .client(OkHttpHelper.addProgressClient(httpClient, listener).build())
                    .build()
            return retrofit.create(serviceClass)
        }

    }
}