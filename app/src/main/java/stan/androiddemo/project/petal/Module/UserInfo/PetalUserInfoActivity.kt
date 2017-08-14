package stan.androiddemo.project.petal.Module.UserInfo

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_petal_user_info.*
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import stan.androiddemo.R
import stan.androiddemo.project.petal.API.UserAPI
import stan.androiddemo.project.petal.Base.BasePetalActivity
import stan.androiddemo.project.petal.Config.Config
import stan.androiddemo.project.petal.HttpUtiles.RetrofitClient
import stan.androiddemo.project.petal.Model.BoardListInfo
import stan.androiddemo.tool.CompatUtils
import stan.androiddemo.tool.Logger
import stan.androiddemo.tool.SPUtils

class PetalUserInfoActivity : BasePetalActivity() {

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


}
