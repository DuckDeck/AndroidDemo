package stan.androiddemo.project.petal.Base

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import stan.androiddemo.tool.NetUtils

/**
 * Created by stanhu on 10/8/2017.
 */
public abstract class BaseActivity:AppCompatActivity(){

    protected val TAG = getTag()

    abstract fun getTag():String

    abstract fun  getLayoutId():Int

    var isLogin = false

    var mAthorization:String = ""

    lateinit var mContext:Context

    override fun toString(): String {
        return javaClass.simpleName + " @" + Integer.toHexString(hashCode());
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT){
            if (isTranslucentStatusBar()){
                val wd = window
                wd.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }
        }

        setContentView(getLayoutId())
        mContext = this
        initResAndListener()
    }


    fun isTranslucentStatusBar():Boolean{   return true }

    open fun initResAndListener(){    }

    fun checkException(e:Throwable,mRootView:View){
        NetUtils.checkHttpException(mContext,e,mRootView)
    }
}