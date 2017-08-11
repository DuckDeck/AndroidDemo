package stan.androiddemo.project.petal.API

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Observable
import stan.androiddemo.project.petal.Config.Config
import stan.androiddemo.project.petal.Model.PinsMainInfo

/**
 * Created by stanhu on 11/8/2017.
 */
interface PetalAPI{
    @GET("favorite/{type}")
    fun httpsTypeLimitRx(@Header(Config.Authorization) authorization: String,
                         @Path("type") type: String, @Query("limit") limit: Int): Observable<ListPinsBean>
    @GET("favorite/{type}")
    fun httpsTypeMaxLimitRx(@Header(Config.Authorization) authorization: String,
                            @Path("type") type: String, @Query("max") max:Int,@Query("limit") limit: Int):Observable<ListPinsBean>
}

class ListPinsBean(){
    var pins:ArrayList<PinsMainInfo>? = null
}