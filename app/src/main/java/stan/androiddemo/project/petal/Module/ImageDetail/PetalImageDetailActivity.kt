package stan.androiddemo.project.petal.Module.ImageDetail

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_petal_image_detail.*
import org.greenrobot.eventbus.EventBus
import stan.androiddemo.R
import stan.androiddemo.project.petal.Base.BasePetalActivity
import stan.androiddemo.project.petal.Model.PinsMainInfo

class PetalImageDetailActivity : BasePetalActivity() {
    private val KEYPARCELABLE = "Parcelable"
    private var mActionFrom: Int = 0
    val ACTION_KEY = "key"//key值
    val ACTION_DEFAULT = -1//默认值
    val ACTION_THIS = 0//来自自己的跳转
    val ACTION_MAIN = 1//来自主界面的跳转
    val ACTION_MODULE = 2//来自模块界面的跳转
    val ACTION_BOARD = 3//来自画板界面的跳转
    val ACTION_ATTENTION = 4//来自我的关注界面的跳转
    val ACTION_SEARCH = 5//来自搜索界面的跳转

    lateinit var mPinsBean:PinsMainInfo

    lateinit var mImageUrl: String//图片地址
    lateinit var mImageType: String//图片类型
    lateinit var mPinsId: String

    private var isLike = false//该图片是否被喜欢操作 默认false 没有被操作过
    private var isGathered = false//该图片是否被采集过


    override fun getTag(): String {return this.toString() }

    companion object {
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
        toolbar.setNavigationOnClickListener { onBackPressed() }
        EventBus.getDefault().register(this)
        mActionFrom = intent.getIntExtra("action",ACTION_DEFAULT)
        recoverData(savedInstanceState)

        mImageUrl = mPinsBean.file!!.key!!
        mImageType = mPinsBean.file!!.type!!
        mPinsId = mPinsBean.pin_id.toString()

        isLike = mPinsBean.liked

        img_image_detail_bg.aspectRatio = mPinsBean.imgRatio


    }

    fun recoverData(savedInstanceState:Bundle?){
        if (savedInstanceState != null){
            if (savedInstanceState!!.getParcelable<PinsMainInfo>(KEYPARCELABLE)!=null){
                mPinsBean = savedInstanceState!!.getParcelable<PinsMainInfo>(KEYPARCELABLE)
            }
        }
    }

}
