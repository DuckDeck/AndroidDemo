package stan.androiddemo.project.petal.Observable

import rx.Observable
import stan.androiddemo.project.petal.Model.ErrorBaseInfo
import stan.androiddemo.tool.Logger

/**
 * Created by stanhu on 12/8/2017.
 */
object ErrorHelper {

    fun <T : ErrorBaseInfo> getCheckNetError(bean: T?): Observable<T> {
        return Observable.create { subscriber ->
            if (bean != null) {
                val msg = bean!!.msg
                if (msg != null) {
                    Logger.d("onError=" + msg!!)

                    subscriber.onError(RuntimeException(bean.msg))
                } else {
                    Logger.d("onNext")
                    subscriber.onNext(bean)
                }
            } else {
                subscriber.onError(RuntimeException())
            }
        }
    }

}