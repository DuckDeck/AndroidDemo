package stan.androiddemo.project.petal.Base


import android.content.Context
import android.support.v7.app.AppCompatDialogFragment
import rx.Subscription
import rx.subscriptions.CompositeSubscription


abstract class BaseDialogFragment : AppCompatDialogFragment() {
    protected val TAG = getTAGInfo()

    abstract  fun getTAGInfo():String

    var mAuthorization:String = ""


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

    fun throwRuntimeException(context: Context) {
        throw RuntimeException(context.toString() + " must implement OnDialogInteractionListener")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this.mCompositeSubscription != null){
            this.mCompositeSubscription = null
        }
    }
}
