package stan.androiddemo.project.petal.Module.ImageDetail

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.graphics.drawable.AnimatedVectorDrawableCompat
import android.view.Menu
import android.view.MenuItem
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.imagepipeline.image.ImageInfo
import kotlinx.android.synthetic.main.activity_petal_image_detail.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import stan.androiddemo.R
import stan.androiddemo.project.petal.Base.BasePetalActivity
import stan.androiddemo.project.petal.Model.PinsMainInfo
import stan.androiddemo.tool.AnimatorUtils
import stan.androiddemo.tool.CompatUtils
import stan.androiddemo.tool.ImageLoad.ImageLoadBuilder
import stan.androiddemo.tool.Logger

class PetalImageDetailActivity : BasePetalActivity() {
    private val KEYPARCELABLE = "Parcelable"
    private var mActionFrom: Int = 0


    lateinit var mPinsBean:PinsMainInfo

    lateinit var mImageUrl: String//图片地址
    lateinit var mImageType: String//图片类型
    lateinit var mPinsId: String

    private var isLike = false//该图片是否被喜欢操作 默认false 没有被操作过
    private var isGathered = false//该图片是否被采集过

    lateinit var mDrawableCancel: Drawable
    lateinit var mDrawableRefresh: Drawable


    override fun getTag(): String {return this.toString() }

    companion object {

        val ACTION_KEY = "key"//key值
        val ACTION_DEFAULT = -1//默认值
        val ACTION_THIS = 0//来自自己的跳转
        val ACTION_MAIN = 1//来自主界面的跳转
        val ACTION_MODULE = 2//来自模块界面的跳转
        val ACTION_BOARD = 3//来自画板界面的跳转
        val ACTION_ATTENTION = 4//来自我的关注界面的跳转
        val ACTION_SEARCH = 5//来自搜索界面的跳转

        fun launch(activity:Activity){
            val intent = Intent(activity,PetalImageDetailActivity::class.java)
            activity.startActivity(intent)
        }
        fun launch(activity: Activity,action:Int){
            val intent = Intent(activity,PetalImageDetailActivity::class.java)
            intent.putExtra("action",action)
            activity.startActivity(intent)
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_petal_image_detail
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //注册eventbus
        setSupportActionBar(toolbar)
        title = ""
        toolbar.setNavigationOnClickListener { onBackPressed() }
        EventBus.getDefault().register(this)
        mActionFrom = intent.getIntExtra("action",ACTION_DEFAULT)
        recoverData(savedInstanceState)

        mDrawableCancel = CompatUtils.getTintDrawable(mContext,R.drawable.ic_cancel_black_24dp,Color.GRAY)
        mDrawableRefresh = CompatUtils.getTintDrawable(mContext,R.drawable.ic_refresh_black_24dp,Color.GRAY)
        mImageUrl = mPinsBean.file!!.key!!
        mImageType = mPinsBean.file!!.type!!
        mPinsId = mPinsBean.pin_id.toString()

        isLike = mPinsBean.liked

        img_image_detail_bg.aspectRatio = mPinsBean.imgRatio

        supportFragmentManager.beginTransaction().replace(R.id.frame_layout_petal_with_refresh,
                PetalImageDetailFragment.newInstance(mPinsId)).commit()

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelable(KEYPARCELABLE,mPinsBean)
    }

    fun recoverData(savedInstanceState:Bundle?){
        if (savedInstanceState != null){
            if (savedInstanceState!!.getParcelable<PinsMainInfo>(KEYPARCELABLE)!=null){
                mPinsBean = savedInstanceState!!.getParcelable<PinsMainInfo>(KEYPARCELABLE)
            }
        }
    }

    override fun initResAndListener() {
        super.initResAndListener()
        fab_image_detail.setImageResource(R.drawable.ic_camera_white_24dp)
        fab_image_detail.setOnClickListener {

        }
    }

    @Subscribe(sticky = true)
    fun onEventReceiveBean(bean: PinsMainInfo) {
        //接受EvenBus传过来的数据
        Logger.d(TAG + " receive bean")
        this.mPinsBean = bean
    }

    override fun onResume() {
        super.onResume()
        showImage()
    }

    fun showImage(){
        var objectAnimator: ObjectAnimator? = null
        if (mImageType.toLowerCase().contains("fig")){
            objectAnimator = AnimatorUtils.getRotationFS(fab_image_detail)
            objectAnimator?.start()
        }
        val url = String.format(mFormatImageUrlBig,mImageUrl)
        val url_low = String.format(mFormatImageGeneral,mImageUrl)
        ImageLoadBuilder.Start(mContext,img_image_detail_bg,url).setUrlLow(url_low)
                .setRetryImage(mDrawableRefresh)
                .setFailureImage(mDrawableCancel)
                .setControllerListener(object:BaseControllerListener<ImageInfo>(){
                    override fun onFinalImageSet(id: String?, imageInfo: ImageInfo?, animatable: Animatable?) {
                        super.onFinalImageSet(id, imageInfo, animatable)
                        Logger.d("onFinalImageSet"+Thread.currentThread().toString())
                        if (animatable != null){
                            animatable.start()
                        }
                        if (objectAnimator!= null && objectAnimator!!.isRunning){
                            img_image_detail_bg.postDelayed(Runnable {
                                objectAnimator?.cancel()
                            },600)
                        }
                    }

                    override fun onSubmit(id: String?, callerContext: Any?) {
                        super.onSubmit(id, callerContext)
                        Logger.d("onSubmit"+Thread.currentThread().toString())
                    }

                    override fun onFailure(id: String?, throwable: Throwable?) {
                        super.onFailure(id, throwable)
                        Logger.d(throwable.toString())
                    }
                }).build()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
         super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.petal_image_detail_menu,menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val drawableCompat:AnimatedVectorDrawableCompat?
        if (isLike){
            drawableCompat = AnimatedVectorDrawableCompat.create(mContext
            ,R.drawable.drawable_animation_petal_favorite_undo)
        }
        else{
            drawableCompat = AnimatedVectorDrawableCompat.create(mContext
                    ,R.drawable.drawable_animation_petal_favorite_do)
        }
        menu?.findItem(R.id.action_like)?.icon = drawableCompat
        return super.onPrepareOptionsMenu(menu)
    }



    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            android.R.id.home->{
                //这里原代码有一个逻辑要处理
            }
            R.id.action_like->{

            }
            R.id.action_download->{

            }
            R.id.action_gather->{

            }

        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}
