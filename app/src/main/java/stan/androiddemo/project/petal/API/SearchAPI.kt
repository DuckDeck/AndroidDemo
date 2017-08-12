package stan.androiddemo.project.petal.API

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import rx.Observable
import stan.androiddemo.project.petal.Config.Config
import stan.androiddemo.project.petal.Module.Search.SearchHintBean

/**
 * Created by hugfo on 2017/8/12.
 */
interface SearchAPI{
    //https//api.huaban.com/search/hint?q=%E4%BA%BA
    //搜索关键字 提示
    @GET("search/hint")
    abstract fun httpsSearHintBean(@Header(Config.Authorization) authorization: String, @Query("q") key: String): Observable<SearchHintBean>

}