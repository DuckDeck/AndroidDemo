package stan.androiddemo.project.petal.API


import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import rx.Observable
import stan.androiddemo.project.petal.Config.Config
import stan.androiddemo.project.petal.Model.BoardListInfo
import stan.androiddemo.project.petal.Module.Login.UserMeAndOtherBean

/**
 * Created by stanhu on 14/8/2017.
 */
interface UserAPI{
    @GET("users/me")
     fun httpsUserRx(@Header(Config.Authorization) authorization: String): Observable<UserMeAndOtherBean>

    //获取我的画板集合信息 不需要显示需要保存
    //https://api.huaban.com/last_boards/?extra=recommend_tags
    @GET("last_boards/")
    abstract fun httpsBoardListInfo(@Header(Config.Authorization) authorization: String,
                                    @Query("extra") extra: String): Observable<BoardListInfo>

}