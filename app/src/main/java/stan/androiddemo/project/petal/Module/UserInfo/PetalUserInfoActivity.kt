package stan.androiddemo.project.petal.Module.UserInfo

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewPager
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.facebook.common.references.CloseableReference
import com.facebook.datasource.DataSource
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber
import com.facebook.imagepipeline.image.CloseableImage
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.activity_petal_user_info.*
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import stan.androiddemo.PageAdapter
import stan.androiddemo.R
import stan.androiddemo.UI.BasePetalRecyclerFragment
import stan.androiddemo.project.petal.API.OperateAPI
import stan.androiddemo.project.petal.API.UserAPI
import stan.androiddemo.project.petal.Base.BasePetalActivity
import stan.androiddemo.project.petal.Config.Config
import stan.androiddemo.project.petal.Event.OnBoardFragmentInteractionListener
import stan.androiddemo.project.petal.Event.OnDialogInteractionListener
import stan.androiddemo.project.petal.Event.OnPinsFragmentInteractionListener
import stan.androiddemo.project.petal.HttpUtiles.RetrofitClient
import stan.androiddemo.project.petal.Model.BoardListInfo
import stan.androiddemo.project.petal.Model.PinsMainInfo
import stan.androiddemo.project.petal.Module.BoardDetail.BoardDetailActivity
import stan.androiddemo.project.petal.Module.ImageDetail.PetalImageDetailActivity
import stan.androiddemo.project.petal.Module.Login.UserMeAndOtherBean
import stan.androiddemo.tool.*
import stan.androiddemo.tool.ImageLoad.ImageLoadBuilder
import java.util.concurrent.TimeUnit

class PetalUserInfoActivity : BasePetalActivity(),OnDialogInteractionListener,OnPinsFragmentInteractionListener,OnBoardFragmentInteractionListener<UserBoardItemBean> {
    lateinit var mHttpRoot: String
    lateinit var mFormatUrlSmall:String
    lateinit var mFansFollowingFormat:String
    lateinit var mKey: String
    lateinit var mTitle: String
    var isMe: Boolean = false
    var isFollow: Boolean = false
    lateinit var mAdapter: PageAdapter
    var currentSelectFragmentIndex = 0
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

    override fun onResume() {
        super.onResume()
        getHttpsUserInfo()
    }

    override fun getLayoutId(): Int {
       return R.layout.activity_petal_user_info
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        mFormatUrlSmall = resources.getString(R.string.url_image_small)
        mHttpRoot = resources.getString(R.string.httpRoot)
        mFansFollowingFormat = resources.getString(R.string.text_fans_attention)
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

        val tt = resources.getStringArray(R.array.title_user_info)
        val titles = ArrayList<String>()
        titles.addAll(tt)
        var fragments = arrayListOf(PetalUserBoardFragment.newInstance(mKey),PetalUserPinsFragment.newInstance(mKey),PetalUserLikeFragment.newInstance(mKey))
        mAdapter = PageAdapter(supportFragmentManager,fragments,titles)
        viewPager.adapter = mAdapter
        tablayout_user.setupWithViewPager(viewPager)
        tablayout_user.setSelectedTabIndicatorColor(Color.WHITE)
        viewPager.setOnPageChangeListener(object:ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                currentSelectFragmentIndex = position
                if (position != 0){
                    float_button_opera.translationY = 0F
                    if (float_button_opera.visibility != View.GONE){
                        float_button_opera.hide()
                    }
                }
                else{
                    if (float_button_opera.visibility != View.VISIBLE){
                        float_button_opera.show()
                    }
                }
            }

        })
    }

    override fun initResAndListener() {
        super.initResAndListener()
        RxView.clicks(float_button_opera).throttleFirst(300,TimeUnit.MILLISECONDS)
                .subscribe({
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
                        setFabDrawableAnimator(res,float_button_opera)
                    }

                    override fun onError(e: Throwable?) {
                        checkException(e!!,appBar_layout)
                        setFabDrawableAnimator(R.drawable.ic_report_white_24dp,float_button_opera)
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

    fun setFabDrawableAnimator(resId:Int,mFabActionBtn:FloatingActionButton){
        mFabActionBtn.hide(object:FloatingActionButton.OnVisibilityChangedListener(){
            override fun onHidden(fab: FloatingActionButton?) {
                super.onHidden(fab)
                fab?.setImageResource(resId)
                fab?.show()
                (mAdapter.mFragments[currentSelectFragmentIndex] as BasePetalRecyclerFragment<*>).setRefresh()
            }
        })
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

    private fun getHttpsUserInfo():Subscription{
        return RetrofitClient.createService(UserAPI::class.java).httpsUserInfoRx(mAuthorization,mKey).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object:Subscriber<UserMeAndOtherBean>(){
                    override fun onNext(t: UserMeAndOtherBean?) {
                        setUserHeadAndBackGround(t!!)
                        setUserTextInfo(t!!)
                        setUserFollow(t!!)
                    }

                    override fun onCompleted() {   }

                    override fun onError(e: Throwable?) {}

                })
    }

    fun setUserHeadAndBackGround(bean:UserMeAndOtherBean){
        var url = bean.avatar
        if (!url.isNullOrEmpty()){
            if (url!!.contains(mHttpRoot)){
                url = String.format(mFormatUrlSmall,url!!)
            }
            ImageLoadBuilder.Start(applicationContext,img_image_user,url!!).setPlaceHolderImage(
                    CompatUtils.getTintDrawable(mContext,R.drawable.ic_account_circle_white_48dp,Color.WHITE)

            ).setIsCircle(true,true)
                    .setBitmapDataSubscriber(object:BaseBitmapDataSubscriber(){
                        override fun onFailureImpl(dataSource: DataSource<CloseableReference<CloseableImage>>?) {}

                        override fun onNewResultImpl(bitmap: Bitmap?) {
                            if (bitmap == null){

                            }
                            else{
                                val backDrawable = BitmapDrawable(resources,FastBlurUtil.doBlur(bitmap!!,25,false))
                                if (Utils.checkUiThreadBoolean()){
                                    appBar_layout.background = backDrawable
                                }
                                else{
                                    appBar_layout.post {
                                        appBar_layout.background = backDrawable
                                    }
                                }
                            }
                        }

                    }).build()
        }
    }
    fun setUserTextInfo(bean:UserMeAndOtherBean){
        if (!bean.username.isNullOrEmpty()){
            tv_user_name.text = bean.username
        }
        else{
            tv_user_name.text = "用户名为空"
        }

        val location = bean.profile?.location
        val job = bean.profile?.job
        val buffer = StringBuffer()
        if (!TextUtils.isEmpty(location)) {
            buffer.append(location)
            buffer.append(" ")
        }
        if (!TextUtils.isEmpty(job)) {
            buffer.append(job)
        }
        if (!TextUtils.isEmpty(buffer)) {
            tv_user_location_job.text = buffer
        }

        tv_user_friend.text = String.format(mFansFollowingFormat, bean.follower_count, bean.following_count)
    }

    fun setUserFollow(bean:UserMeAndOtherBean){
        isFollow = bean.following_count === 1
        //网络返回成功后 如果是我显示添加 否则根据关注状态 显示
        float_button_opera.setImageResource(
                if (isMe)
                    R.drawable.ic_add_black_24dp
                else
                    if (isFollow) R.drawable.ic_done_black_24dp else R.drawable.ic_loyalty_black_24dp)
        float_button_opera.show()
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
        if (option){
            Logger.d(info.toString())
        }
    }
    override fun onClickPinsItemImage(bean: PinsMainInfo, view: View) {
        PetalImageDetailActivity.launch(this@PetalUserInfoActivity)
    }

    override fun onClickPinsItemText(bean: PinsMainInfo, view: View) {
        PetalImageDetailActivity.launch(this@PetalUserInfoActivity)
    }

    override fun onClickBoardItemImage(bean: UserBoardItemBean, view: View) {
        BoardDetailActivity.launch(this@PetalUserInfoActivity,bean.board_id.toString(),bean.title!!)
    }
}
