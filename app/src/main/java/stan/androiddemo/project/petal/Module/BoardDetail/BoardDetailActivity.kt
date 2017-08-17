package stan.androiddemo.project.petal.Module.BoardDetail

import android.animation.AnimatorInflater
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.view.View
import kotlinx.android.synthetic.main.activity_board_detail.*
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import stan.androiddemo.R
import stan.androiddemo.project.petal.API.OperateAPI
import stan.androiddemo.project.petal.Base.BasePetalActivity
import stan.androiddemo.project.petal.Config.Config
import stan.androiddemo.project.petal.Event.OnBoardDetailFragmentInteractionListener
import stan.androiddemo.project.petal.HttpUtiles.RetrofitClient
import stan.androiddemo.project.petal.Model.PinsMainInfo
import stan.androiddemo.project.petal.Module.ImageDetail.PetalImageDetailActivity
import stan.androiddemo.project.petal.Module.UserInfo.FollowUserOperateBean
import stan.androiddemo.project.petal.Module.UserInfo.PetalUserInfoActivity
import stan.androiddemo.project.petal.Observable.AnimatorOnSubscribe
import stan.androiddemo.tool.NetUtils
import stan.androiddemo.tool.SPUtils

class BoardDetailActivity : BasePetalActivity(),OnBoardDetailFragmentInteractionListener {
    lateinit var mKey: String
    lateinit var mTitle: String
    //是否我的画板 音响FloatingActionButton 的显示
    private var isMe = false
    //该画板是否被关注的标志位 默认false
    var isFollow = false

    override fun getTag(): String {
       return this.toString()
    }

    companion object {
        fun launch(activity:Activity,key:String,title:String){
            val intent = Intent(activity,BoardDetailActivity::class.java)
            intent.putExtra("title",title)
            intent.putExtra("key",key)
            activity.startActivity(intent)
        }
        fun launch(activity:Activity,flag:Int){
            val intent = Intent(activity,BoardDetailActivity::class.java)
            intent.flags = flag
            activity.startActivity(intent)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        mKey = intent.getStringExtra("key")
        mTitle = intent.getStringExtra("title")
        title = mTitle

        fab_board_detail.isEnabled = false
        fab_board_detail.setOnClickListener {
            actionAttention()
        }
        supportFragmentManager.beginTransaction().replace(R.id.frame_layout_petal_with_refresh,BoardDetailFragment.newInstance(mKey)).commit()
    }

    override fun getLayoutId(): Int {
       return R.layout.activity_board_detail
    }

    fun actionAttention(){
        val operate = if (isFollow) Config.OPERATEUNFOLLOW else Config.OPERATEFOLLOW
        val animation = AnimatorInflater.loadAnimator(mContext,R.animator.scale_percent_small_animation)
        animation.setTarget(fab_board_detail)

        Observable.create(AnimatorOnSubscribe(animation)).observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .flatMap { RetrofitClient.createService(OperateAPI::class.java).httpsFollowUserOperate(mAuthorization,mKey,operate)
                        }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object:Subscriber<FollowUserOperateBean>(){
                    override fun onCompleted() {
                       setFabDrawableAndStart(if (isFollow) R.drawable.ic_done_white_24dp else R.drawable.ic_loyalty_black_24dp,mContext,fab_board_detail)
                    }

                    override fun onError(e: Throwable?) {
                        setFabDrawableAndStart(R.drawable.ic_report_white_24dp,mContext,fab_board_detail)
                        NetUtils.checkHttpException(mContext,e,toolbar)
                    }

                    override fun onNext(t: FollowUserOperateBean?) {
                        isFollow = !isFollow
                    }

                })
    }

    private fun setFabDrawableAndStart(resId: Int, mContext: Context, mFabActionBtn: FloatingActionButton) {
        mFabActionBtn.setImageResource(resId)
        val animation = AnimatorInflater.loadAnimator(mContext, R.animator.scale_percent_magnify_animation)
        animation.setTarget(mFabActionBtn)
        animation.start()
    }

    override fun onClickUserField(key: String, title: String) {
        PetalUserInfoActivity.launch(this@BoardDetailActivity,key,title)
    }

    override fun onClickPinsItemImage(bean: PinsMainInfo, view: View) {
        PetalImageDetailActivity.launch(this@BoardDetailActivity)
    }

    override fun onHttpBoardAttentionState(userId: String, isAttention: Boolean) {
        if (isLogin){
            val localUserId = SPUtils.get(mContext,Config.USERID,"") as String
            isMe = localUserId == userId
        }
        if (!isMe){
            isFollow = isAttention
            fab_board_detail.visibility = View.VISIBLE
            fab_board_detail.setImageResource(if (isAttention) R.drawable.ic_done_white_24dp else R.drawable.ic_loyalty_white_24dp)
        }
    }

    override fun onClickPinsItemText(bean: PinsMainInfo, view: View) {
        PetalImageDetailActivity.launch(this@BoardDetailActivity)
    }

}
