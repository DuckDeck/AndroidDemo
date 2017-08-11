package stan.androiddemo.project.petal.Config

import android.app.Application
import stan.androiddemo.tool.SPUtils

/**
 * Created by stanhu on 11/8/2017.
 */
class UserSingleton{
    private var mAuthorization: String? = null

    private var isLogin: Boolean? = null

   companion object {
       @Volatile  var instance = UserSingleton()
   }



    fun getInstance(): UserSingleton {
        return instance
    }

    fun getAuthorization(): String? {
        if (isLogin!!) {
            if (mAuthorization == null) {

            }
            return mAuthorization
        }

        return mAuthorization
    }

    fun setAuthorization(mAuthorization: String) {

        this.mAuthorization = mAuthorization
    }

    fun isLogin(mContext: Application): Boolean {
        if (isLogin == null) {
            isLogin = SPUtils.get(mContext, Config.ISLOGIN, false) as Boolean?
        }
        return isLogin!!
    }

    fun setLogin(login: Boolean) {
        isLogin = login
    }
}