package stan.androiddemo.project.petal.Module.Guide

import android.animation.Animator
import android.animation.AnimatorInflater
import kotlinx.android.synthetic.main.activity_guide.*
import stan.androiddemo.R
import stan.androiddemo.project.petal.Base.BaseActivity
import stan.androiddemo.project.petal.Module.Main.PetalActivity

class GuideActivity : BaseActivity() {

    override fun getTag(): String {
        return this.toString()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_guide
    }

    override fun onResume() {
        super.onResume()
        val animation = AnimatorInflater.loadAnimator(mContext,R.animator.guide_animator)
        animation.setTarget(img_app_guide)
        animation.start()
        animation.addListener(object:Animator.AnimatorListener{
            override fun onAnimationRepeat(p0: Animator?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onAnimationCancel(p0: Animator?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onAnimationStart(p0: Animator?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onAnimationEnd(p0: Animator?) {
                PetalActivity.launch(this@GuideActivity)
            }

        })
    }
}
