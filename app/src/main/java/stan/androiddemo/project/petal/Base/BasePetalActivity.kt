package stan.androiddemo.project.petal.Base

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import rx.Subscription
import rx.subscriptions.CompositeSubscription
import stan.androiddemo.R
import stan.androiddemo.project.petal.Config.Config
import stan.androiddemo.project.petal.Config.UserSingleton
import stan.androiddemo.tool.Base64Utils
import stan.androiddemo.tool.NetUtils
import stan.androiddemo.tool.SPUtils

/**
 * Created by stanhu on 10/8/2017.
 */
public abstract class BasePetalActivity :AppCompatActivity(){

    protected val TAG = getTag()

    abstract fun getTag():String

    abstract fun  getLayoutId():Int

    var isLogin = false

    var mAuthorization:String = ""


    lateinit var mFormatImageUrlBig: String
    //大图的后缀

    lateinit var mFormatImageGeneral: String

    lateinit var mContext:Context

    override fun toString(): String {
        return javaClass.simpleName + " @" + Integer.toHexString(hashCode());
    }

    private var mCompositeSubscription: CompositeSubscription? = null
    fun getCompositeSubscription(): CompositeSubscription {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = CompositeSubscription()
        }

        return this.mCompositeSubscription!!
    }

    fun addSubscription(s: Subscription?) {
        if (s == null) {
            return
        }

        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = CompositeSubscription()
        }

        this.mCompositeSubscription!!.add(s)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT){
            if (isTranslucentStatusBar()){
                val wd = window
                wd.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }
        }

        mFormatImageGeneral = resources.getString(R.string.url_image_general)
        mFormatImageUrlBig = resources.getString(R.string.url_image_big)
        setContentView(getLayoutId())
        mContext = this
        getLoginData()
        initResAndListener()
    }


   open fun isTranslucentStatusBar():Boolean{   return true }

    fun getLoginData(){
        UserSingleton.instance.isLogin(application)
        isLogin = SPUtils.get(mContext,Config.ISLOGIN,false) as Boolean
        mAuthorization = getAuthorizations(isLogin)
    }

    open fun initResAndListener(){    }


    protected fun getAuthorizations(isLogin: Boolean): String {
        val temp = " "
        if (isLogin) {
            return (SPUtils.get(mContext, Config.TOKENTYPE, temp) as String) + temp + (SPUtils.get(mContext, Config.TOKENACCESS, temp) as String)
        }
        return Base64Utils.mClientInto
    }


    override fun onDestroy() {
        super.onDestroy()
        if (this.mCompositeSubscription != null){
            this.mCompositeSubscription = null
        }
    }

    fun checkException(e:Throwable,mRootView:View){
        NetUtils.checkHttpException(mContext,e,mRootView)
    }

    fun toast(msg:String){
        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show()
    }
}