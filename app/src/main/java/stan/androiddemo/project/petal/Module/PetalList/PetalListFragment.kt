package stan.androiddemo.project.petal.Module.PetalList

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.graphics.drawable.VectorDrawableCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import com.chad.library.adapter.base.BaseViewHolder
import com.facebook.drawee.view.SimpleDraweeView
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import stan.androiddemo.R
import stan.androiddemo.UI.BasePetalRecyclerFragment
import stan.androiddemo.project.petal.API.PetalAPI
import stan.androiddemo.project.petal.Config.Config
import stan.androiddemo.project.petal.HttpUtiles.RetrofitClient
import stan.androiddemo.project.petal.Model.PinsMainInfo
import stan.androiddemo.project.petal.Module.Main.PetalActivity
import stan.androiddemo.tool.ImageLoad.ImageLoadBuilder
import stan.androiddemo.tool.Logger


class PetalListFragment : BasePetalRecyclerFragment<PinsMainInfo>() {

    lateinit var mKey: String//用于联网查询的关键字
    var mLimit = Config.LIMIT
//    val mUrlSmallFormat = context.resources.getString(R.string.url_image_small)
    lateinit var  mUrlGeneralFormat :String
//    val mUrlBigFormat = context.resources.getString(R.string.url_image_big)
    lateinit var progressLoading: Drawable
    override fun getTheTAG(): String {
        return this.toString()
    }

    companion object {
        fun createListFragment(type:String,title:String):PetalListFragment{
            val fragment = PetalListFragment()
            val bundle = Bundle()
            bundle.putString("key",type)
            bundle.putString("title",title)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val d0 = VectorDrawableCompat.create(resources,R.drawable.ic_toys_black_24dp,null)
        progressLoading = DrawableCompat.wrap(d0!!.mutate())
        DrawableCompat.setTint(progressLoading,resources.getColor(R.color.tint_list_pink))
        mKey = arguments.getString("key")
        mUrlGeneralFormat = context.resources.getString(R.string.url_image_general)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Logger.d(context.toString())
        if (context is PetalActivity){
            mAuthorization = context.mAuthorization
        }

    }

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return  StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
    }

    override fun getItemLayoutId(): Int {
        return R.layout.petal_cardview_image_item
    }

    override fun itemLayoutConvert(helper: BaseViewHolder, t: PinsMainInfo) {
        val imgUrl = String.format(mUrlGeneralFormat,t.file!!.key)
        val ratio = t.file!!.width.toFloat() / t.file!!.height.toFloat()
        val img = helper.getView<SimpleDraweeView>(R.id.img_card_main)
        img.aspectRatio = ratio
        ImageLoadBuilder.Start(context,img,imgUrl).setProgressBarImage(progressLoading).build()
    }

    override fun requestListData(page: Int): Subscription {
        return RetrofitClient.createService(PetalAPI::class.java).httpsTypeLimitRx(mAuthorization!!,mKey,mLimit)
                .map { it.pins }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object:Subscriber<List<PinsMainInfo>>(){
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable?) {
                       loadError()
                        checkException(e)
                    }

                    override fun onNext(t: List<PinsMainInfo>?) {
                        if (t == null){
                            loadError()
                            return
                        }
                        loadSuccess(t!!)
                    }

                })
    }




}
