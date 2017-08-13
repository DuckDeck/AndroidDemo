package stan.androiddemo.project.petal.API

import licola.demo.com.huabandemo.Module.ImageDetail.PinsDetailBean
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Observable
import stan.androiddemo.project.petal.Config.Config
import stan.androiddemo.project.petal.Model.PinsMainInfo

/**
 * Created by hugfo on 2017/8/13.
 */
interface ImageDetailAPI {
    @GET("pins/{pinsId}")
    abstract fun httpsPinsDetailRx(@Header(Config.Authorization) authorization: String,
                                   @Path("pinsId") pinsId: String): Observable<PinsDetailBean>

    @GET("pins/{pinsId}/recommend/")
    abstract fun httpPinsRecommendRx(@Header(Config.Authorization) authorization: String,
                                     @Path("pinsId") pinsId: String,
                                     @Query("page") page: Int,
                                     @Query("limit") limit: Int): Observable<List<PinsMainInfo>>

}