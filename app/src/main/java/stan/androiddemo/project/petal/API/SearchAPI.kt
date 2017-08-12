package stan.androiddemo.project.petal.API

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import rx.Observable
import rx.Observer
import stan.androiddemo.project.petal.Config.Config
import stan.androiddemo.project.petal.Module.Search.SearchBoardBean
import stan.androiddemo.project.petal.Module.Search.SearchHintBean
import stan.androiddemo.project.petal.Module.Search.SearchImageBean
import stan.androiddemo.project.petal.Module.Search.SearchPeopleBean

/**
 * Created by hugfo on 2017/8/12.
 */
interface SearchAPI{
    //https//api.huaban.com/search/hint?q=%E4%BA%BA
    //搜索关键字 提示
    @GET("search/hint")
    fun httpsSearHintBean(@Header(Config.Authorization) authorization: String,
                          @Query("q") key: String): Observable<SearchHintBean>

    @GET("search/")
    fun httpsImageSearchRx(@Header(Config.Authorization) authorization: String,
                                    @Query("q") key: String, @Query("page") page: Int,
                                    @Query("per_page") per_page: Int): Observable<SearchImageBean>

    //画板搜索
    @GET("search/boards/")
    fun httpsBoardSearchRx(@Header(Config.Authorization) authorization: String,
                                    @Query("q") key: String, @Query("page") page: Int,
                                    @Query("per_page") per_page: Int): Observable<SearchBoardBean>

    //用户搜索
    @GET("search/people/")
    fun httpsPeopleSearchRx(@Header(Config.Authorization) authorization: String,
                            @Query("q") key:String,
                            @Query("page") page:Int,
                            @Query("per_page") per_page:Int):Observable<SearchPeopleBean>

}