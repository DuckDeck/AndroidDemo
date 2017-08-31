package stan.androiddemo.project.petal.Module.Guide

import android.animation.AnimatorInflater
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_guide.*
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import stan.androiddemo.R
import stan.androiddemo.project.petal.API.TokenAPI
import stan.androiddemo.project.petal.Base.BasePetalActivity
import stan.androiddemo.project.petal.Config.Config
import stan.androiddemo.project.petal.HttpUtiles.RetrofitClient
import stan.androiddemo.project.petal.Module.Login.TokenBean
import stan.androiddemo.project.petal.Module.Main.PetalActivity
import stan.androiddemo.project.petal.Observable.AnimatorOnSubscribe
import stan.androiddemo.tool.*

class GuideActivity : BasePetalActivity() {
    private val PASSWORD = "password"
    private val mTimeDifference = TimeUtils.HOUR

    override fun getTag(): String {
        return this.toString()
    }



    override fun getLayoutId(): Int {
        return R.layout.activity_guide
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //img_app_guide.setImageBitmap(BitmapFactory.decodeResource(resources,R.drawable.guide))
    }

    override fun onResume() {
        super.onResume()
        val animation = AnimatorInflater.loadAnimator(mContext,R.animator.guide_animator)
        animation.setTarget(img_app_guide)
        Observable.create(AnimatorOnSubscribe(animation)).observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                .filter {
                    isLogin
                }
                .filter {
                    val lastTime = SPUtils.get(applicationContext, Config.LOGINTIME, 0L) as Long
                    val dTime = System.currentTimeMillis() - lastTime
                    Logger.d("dTime=$dTime default$mTimeDifference")
                     dTime > mTimeDifference
                }.flatMap {
                    Logger.d("flatMap")
                    val userAccount = SPUtils.get(applicationContext, Config.USERACCOUNT, "") as String
                    val userPassword = SPUtils.get(applicationContext, Config.USERPASSWORD, "") as String
                     getUserToken(userAccount, userPassword)
                 }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object:Subscriber<TokenBean>(){
                    override fun onError(e: Throwable?) {
                        //TODO login
                    }

                    override fun onNext(t: TokenBean?) {
                        Logger.d("https success")
                        saveToken(t!!)
                    }

                    override fun onCompleted() {
                        PetalActivity.launch(this@GuideActivity)
                        finish()
                    }

                })

    }

    private fun saveToken(tokenBean: TokenBean) {
        SPBuild(applicationContext)
                .addData(Config.LOGINTIME, System.currentTimeMillis())
                .addData(Config.TOKENACCESS, tokenBean.access_token!!)
                .addData(Config.TOKENTYPE, tokenBean.token_type!!)
                .build()
    }

    private fun getUserToken(username: String, password: String): Observable<TokenBean> {
        return RetrofitClient.createService(TokenAPI::class.java)
                .httpsGetTokenRx(Base64Utils.mClientInto, PASSWORD, username, password)
    }
}
