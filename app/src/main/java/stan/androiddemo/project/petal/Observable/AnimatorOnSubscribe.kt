package stan.androiddemo.project.petal.Observable

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import com.jakewharton.rxbinding.internal.Preconditions
import rx.Observable
import rx.Subscriber
import stan.androiddemo.tool.Logger

/**
 * Created by stanhu on 11/8/2017.
 */
class AnimatorOnSubscribe :Observable.OnSubscribe<Unit>{
    lateinit var  animator: Animator

    constructor(animator:Animator){
        this.animator = animator
    }

    override fun call(t: Subscriber<in Unit>?) {
        Preconditions.checkUiThread()
        val adapter = object:AnimatorListenerAdapter(){
            override fun onAnimationStart(animation: Animator?) {
                super.onAnimationStart(animation)
                t?.onNext(null)
                Logger.d("onAnimationStart")
            }

            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                t?.onCompleted()
                Logger.d("onAnimationEnd")
            }
        }
        animator.addListener(adapter)
        animator.start()
    }

}