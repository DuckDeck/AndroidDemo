package stan.androiddemo.project.petal.API

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST
import rx.Observable
import stan.androiddemo.project.petal.Config.Config
import stan.androiddemo.project.petal.Module.Login.TokenBean

/**
 * Created by stanhu on 11/8/2017.
 */
interface TokenAPI{
    //https 获取token接口 OAuth 2.0密码模式
    //Authorization 报头一个固定的值 内容 grant_type=password&password=密码&username=账号
    //传入用户名和密码
    @FormUrlEncoded
    @POST("https://huaban.com/oauth/access_token/")
     fun httpsGetTokenRx(@Header(Config.Authorization) authorization: String, @Field("grant_type") grant: String,
                                 @Field("username") username: String, @Field("password") password: String): Observable<TokenBean>

    //刷新token接口
    @FormUrlEncoded
    @POST("https://huaban.com/oauth/access_token/")
     fun httpsRefreshTokenRx(@Header(Config.Authorization) authorization: String, @Field("grant_type") grant: String,
                                     @Field("refresh_token") username: String): Observable<TokenBean>
}