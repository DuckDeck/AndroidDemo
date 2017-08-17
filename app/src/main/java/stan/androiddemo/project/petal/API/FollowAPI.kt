package stan.androiddemo.project.petal.API

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import rx.Observable
import stan.androiddemo.project.petal.Config.Config
import stan.androiddemo.project.petal.Module.Follow.FollowBoardListBean
import stan.androiddemo.project.petal.Module.Follow.FollowPinsBean

/**
 * Created by stanhu on 17/8/2017.
 */
interface FollowAPI{
    //https://api.huaban.com/following?limit=40
    //我的关注图片  需要 报头 bearer getAccess_token
    @GET("following")
    fun httpsMyFollowingPinsRx(@Header(Config.Authorization) authorization: String,
                               @Query("limit") limit: Int): Observable<FollowPinsBean>

    //https://api.huaban.com/following?limit=40&max=670619456
    //我的关注图片的 后续滑动联网
    @GET("following")
    fun httpsMyFollowingPinsMaxRx(@Header(Config.Authorization) authorization: String,
                                  @Query("max") max: Int, @Query("limit") limit: Int): Observable<FollowPinsBean>

    //https://api.huaban.com/boards/following?page=1&per_page=20
    //我的关注画板
    @GET("boards/following")
    fun httpsMyFollowingBoardRx(@Header(Config.Authorization) authorization: String,
                                @Query("page") page: Int, @Query("per_page") per_page: Int): Observable<FollowBoardListBean>

}
