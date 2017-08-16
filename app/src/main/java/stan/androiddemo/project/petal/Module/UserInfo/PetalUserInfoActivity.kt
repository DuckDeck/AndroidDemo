package stan.androiddemo.project.petal.Module.UserInfo

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.view.Menu
import android.view.MenuItem
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.activity_petal_user_info.*
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers
import stan.androiddemo.R
import stan.androiddemo.project.petal.API.OperateAPI
import stan.androiddemo.project.petal.API.UserAPI
import stan.androiddemo.project.petal.Base.BasePetalActivity
import stan.androiddemo.project.petal.Config.Config
import stan.androiddemo.project.petal.Event.OnDialogInteractionListener
import stan.androiddemo.project.petal.HttpUtiles.RetrofitClient
import stan.androiddemo.project.petal.Model.BoardListInfo
import stan.androiddemo.tool.CompatUtils
import stan.androiddemo.tool.Logger
import stan.androiddemo.tool.SPUtils
import java.util.concurrent.TimeUnit

class PetalUserInfoActivity : BasePetalActivity(),OnDialogInteractionListener {


    lateinit var mKey: String
    lateinit var mTitle: String
    var isMe: Boolean = false
    var isFollow: Boolean = false

    companion object {
        fun launch(activity: Activity, key: String, title: String) {
            val intent = Intent(activity, PetalUserInfoActivity::class.java)
            intent.putExtra("title", title)
            intent.putExtra("key", key)
            activity.startActivity(intent)
        }
    }


    override fun getTag(): String {
       return this.toString()
    }

    override fun getLayoutId(): Int {
       return R.layout.activity_petal_user_info
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)

        collapsingtoolbar_user.setExpandedTitleColor(Color.TRANSPARENT)
        mTitle = intent.getStringExtra("title")
        mKey = intent.getStringExtra("key")

        val userId = SPUtils.get(mContext, Config.USERID, "") as String

        isMe = mKey == userId

        if (isMe){
            addSubscription(getMyBoardListInfo())
        }

        tv_user_friend.setCompoundDrawablesWithIntrinsicBounds(null,
                CompatUtils.getTintDrawable(mContext,R.drawable.ic_chevron_right_white_24dp, Color.WHITE)
        ,null,null)
    }

    override fun initResAndListener() {
        super.initResAndListener()
        RxView.clicks(float_button_opera).throttleFirst(300,TimeUnit.MILLISECONDS)
                .subscribe(Action1 {
                    startOperate()
                })
        appBar_layout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val size = -verticalOffset
            val total = appBarLayout.totalScrollRange
            val per = 1F - (size / total)
            linearlayout_user_info.alpha = per
        }
    }

    fun startOperate(){
        if (isMe){
            showAddBoardDialog()
        }
         else{
            httpFollowUser()
        }
     }

    fun httpFollowUser(){
        val operation = if (isFollow) Config.OPERATEUNFOLLOW else Config.OPERATEFOLLOW
        RetrofitClient.createService(OperateAPI::class.java).httpsFollowUserOperate(mAuthorization,mKey,operation)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object:Subscriber<FollowUserOperateBean>(){
                    override fun onCompleted() {
                       val res = if (isFollow) R.drawable.ic_done_white_24dp else R.drawable.ic_loyalty_black_24dp
                        float_button_opera.hide(object:FloatingActionButton.OnVisibilityChangedListener(){
                            override fun onHidden(fab: FloatingActionButton?) {
                                super.onHidden(fab)
                                fab?.setImageResource(res)
                                fab?.show()
                                TODO("refresh the fragment")
                            }
                        })
                    }

                    override fun onError(e: Throwable?) {
                        checkException(e!!,appBar_layout)
                    }

                    override fun onNext(t: FollowUserOperateBean?) {
                        isFollow = !isFollow
                    }

                })
    }

    fun showAddBoardDialog(){
        val fragment = BoardAddDialogFragment.create()
        fragment.mLintener = this
        fragment.show(supportFragmentManager,null)
    }


    private fun getMyBoardListInfo(): Subscription {
        return RetrofitClient.createService(UserAPI::class.java)
                .httpsBoardListInfo(mAuthorization, Config.OPERATEBOARDEXTRA)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<BoardListInfo>() {
                    override fun onCompleted() {
                        Logger.d()
                    }

                    override fun onError(e: Throwable) {
                        Logger.d(e.toString())
                    }

                    override fun onNext(boardListInfoBean: BoardListInfo) {
                        Logger.d(boardListInfoBean.boards!!.size.toString() )
                    }
                })
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(if (isMe) R.menu.petal_menu_user_me else R.menu.petal_menu_user,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.action_follow->{
                httpFollowUser()
            }
            R.id.action_user_setting->{
                TODO("user setting")
            }
        }
        return true
    }

    override fun onDialogClick(option: Boolean, info: HashMap<String, Any>) {

    }

}
