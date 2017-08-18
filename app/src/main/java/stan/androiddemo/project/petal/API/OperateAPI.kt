package stan.androiddemo.project.petal.API

import retrofit2.http.*
import rx.Observable
import stan.androiddemo.project.petal.Config.Config
import stan.androiddemo.project.petal.Module.ImageDetail.GatherInfoBean
import stan.androiddemo.project.petal.Module.ImageDetail.GatherResultBean
import stan.androiddemo.project.petal.Module.ImageDetail.LikePinsOperateBean
import stan.androiddemo.project.petal.Module.UserInfo.FollowUserOperateBean
import stan.androiddemo.project.petal.Module.UserInfo.UserBoardSingleBean

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

    @FormUrlEncoded
    @POST("pins/")
     fun httpsGatherPins(@Header(Config.Authorization) authorization: String,
                                 @Field("board_id") boardId: String,
                                 @Field("text") describe: String,
                                 @Field("via") PinsIda: String): Observable<GatherResultBean>

    //关注某个用户
    //https://api.huaban.com/users/17037199/follow  或者unfollow POST方法 统一成一个接口
    @POST("users/{userId}/{operate}")
    abstract fun httpsFollowUserOperate(@Header(Config.Authorization) authorization: String,
                                        @Path("userId") userId: String, @Path("operate") operate: String): Observable<FollowUserOperateBean>

    //修改某个画板的信息
    //https://api.huaban.com/boards/29646779 category=photography&description=%E6%B7%BB%E5%8A%A0%E6%8F%8F%E8%BF%B0&title=%E6%B7%BB%E5%8A%A0
    @FormUrlEncoded
    @POST("boards/{boardId}")
    fun httpsEditBoard(@Header(Config.Authorization) authorization: String,@Path("boardId") boardId: String,
                       @Field("title") title: String, @Field("description") description: String,
                       @Field("category") category: String): Observable<UserBoardSingleBean>


    @FormUrlEncoded
    @POST("boards/{boardId}")
    abstract fun httpsDeleteBoard(@Header(Config.Authorization) authorization: String, @Path("boardId") boardId: String,
                                  @Field("_method") operate: String): Observable<UserBoardSingleBean>

}