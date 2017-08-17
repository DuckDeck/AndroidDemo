package stan.androiddemo.project.petal.API

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Observable
import stan.androiddemo.project.petal.Config.Config
import stan.androiddemo.project.petal.Module.BoardDetail.BoardDetailBean

/**
 * Created by stanhu on 17/8/2017.
 */
interface BoardDetailAPI{
    @GET("boards/{boardId}")
     fun httpsBoardDetailRx(@Header(Config.Authorization) authorization: String,
                            @Path("boardId") boardId: String): Observable<BoardDetailBean>

    //https//api.huaban.com/boards/19196160/pins?limit=40
    //获取画板的图片集合
    @GET("boards/{boardId}/pins")
     fun httpsBoardPinsRx(@Header(Config.Authorization) authorization: String,
                          @Path("boardId") boardId: String, @Query("limit") limit: Int): Observable<ListPinsBean>

    //https//api.huaban.com/boards/19196160/pins?limit=40&max=508414882
    //获取画板的图片集合 根据上一个图片的id继续加载
    @GET("boards/{boardId}/pins")
     fun httpsBoardPinsMaxRx(@Header(Config.Authorization) authorization: String,
                             @Path("boardId") boardId: String, @Query("max") max: Int, @Query("limit") limit: Int): Observable<ListPinsBean>

}