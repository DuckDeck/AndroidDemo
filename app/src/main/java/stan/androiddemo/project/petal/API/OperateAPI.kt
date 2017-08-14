package stan.androiddemo.project.petal.API

import retrofit2.http.*
import rx.Observable
import stan.androiddemo.project.petal.Config.Config
import stan.androiddemo.project.petal.Module.ImageDetail.GatherInfoBean
import stan.androiddemo.project.petal.Module.ImageDetail.LikePinsOperateBean

/**
 * Created by stanhu on 14/8/2017.
 */
interface OperateAPI{
    @POST("pins/{pinId}/{operate}")
     fun httpsLikeOperate(@Header(Config.Authorization)
                                  authorization: String, @Path("pinId") pinsId: String,
                                  @Path("operate") operate: String): Observable<LikePinsOperateBean>

    //对某个图片进行采集前网络访问 判断是否被采集过
    //https://api.huaban.com/pins/707907583/repin/?check=true
    @GET("pins/{viaId}/repin/")
    abstract fun httpsGatherInfo(@Header(Config.Authorization) authorization: String,
                                 @Path("viaId") viaId: String, @Query("check") check: Boolean): Observable<GatherInfoBean>


}