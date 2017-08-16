package stan.androiddemo.project.petal.API


import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Observable
import stan.androiddemo.project.petal.Config.Config
import stan.androiddemo.project.petal.Model.BoardListInfo
import stan.androiddemo.project.petal.Module.Login.UserMeAndOtherBean
import stan.androiddemo.project.petal.Module.UserInfo.UserBoardListBean

/**
 * Created by stanhu on 14/8/2017.
 */
interface UserAPI{
    @GET("users/me")
     fun httpsUserRx(@Header(Config.Authorization) authorization: String): Observable<UserMeAndOtherBean>

    //获取我的画板集合信息 不需要显示需要保存
    //https://api.huaban.com/last_boards/?extra=recommend_tags
    @GET("last_boards/")
    fun httpsBoardListInfo(@Header(Config.Authorization) authorization: String,
                                    @Query("extra") extra: String): Observable<BoardListInfo>

    @GET("users/{userId}/boards")
    fun httpsUserBoardRx(@Header(Config.Authorization) authorization: String,
                         @Path("userId") pinsId: String, @Query("limit") limit: Int): Observable<UserBoardListBean>

    @GET("users/{userId}/boards")
    fun httpsUserBoardMaxRx(@Header(Config.Authorization) authorization: String,
                            @Path("userId") pinsId: String, @Query("max") max: Int, @Query("limit") limit: Int): Observable<UserBoardListBean>

}