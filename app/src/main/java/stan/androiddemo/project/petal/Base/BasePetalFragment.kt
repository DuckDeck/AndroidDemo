package stan.androiddemo.UI

import android.support.v4.app.Fragment
import rx.Subscription
import rx.subscriptions.CompositeSubscription

/**
 * Created by stanhu on 11/8/2017.
 */
abstract class BasePetalFragment : Fragment() {
    protected var TAG = getTheTAG()

    protected abstract fun getTheTAG(): String

    //联网的授权字段 几乎所有的Fragment子类都有联网功能 故父类提供变量
    protected var mAuthorization: String? = null

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

    override fun toString(): String {
        return javaClass.simpleName + " @" + Integer.toHexString(hashCode())
    }
}