package stan.androiddemo.project.petal.Module.PetalList

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseViewHolder
import com.facebook.drawee.view.SimpleDraweeView
import org.greenrobot.eventbus.EventBus
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import stan.androiddemo.R
import stan.androiddemo.UI.BasePetalRecyclerFragment
import stan.androiddemo.project.petal.API.ListPinsBean
import stan.androiddemo.project.petal.API.PetalAPI
import stan.androiddemo.project.petal.Config.Config
import stan.androiddemo.project.petal.Event.OnPinsFragmentInteractionListener
import stan.androiddemo.project.petal.HttpUtiles.RetrofitClient
import stan.androiddemo.project.petal.Model.PinsMainInfo
import stan.androiddemo.tool.CompatUtils
import stan.androiddemo.tool.ImageLoad.ImageLoadBuilder
import stan.androiddemo.tool.Logger


class PetalListFragment : BasePetalRecyclerFragment<PinsMainInfo>() {

    var mLimit = Config.LIMIT
    var maxId = 0
    private var mListener: OnPinsFragmentInteractionListener? = null
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

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Logger.d(context.toString())
        if (context is OnPinsFragmentInteractionListener){
            mListener = context
        }
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return  StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
    }

    override fun getItemLayoutId(): Int {
        return R.layout.petal_cardview_image_item
    }

    override fun itemLayoutConvert(helper: BaseViewHolder, t: PinsMainInfo) {
        val img = helper.getView<SimpleDraweeView>(R.id.img_card_main)
        val txtGather = helper.getView<TextView>(R.id.txt_card_gather)
        var txtLike = helper.getView<TextView>(R.id.txt_card_like)
        val linearlayoutTitleInfo =  helper.getView<LinearLayout>(R.id.linearLayout_image_title_info)
        //只能在这里设置TextView的drawable了

        txtGather.setCompoundDrawablesRelativeWithIntrinsicBounds(CompatUtils.getTintListDrawable(context,
                R.drawable.ic_favorite_black_18dp,R.color.tint_list_grey),null,null,null)
        txtLike.setCompoundDrawablesRelativeWithIntrinsicBounds(CompatUtils.getTintListDrawable(context,
                R.drawable.ic_camera_black_18dp,R.color.tint_list_grey),null,null,null)

        val imgUrl = String.format(mUrlGeneralFormat,t.file!!.key)

        img.aspectRatio = t.imgRatio

        helper.getView<FrameLayout>(R.id.frame_layout_card_image).setOnClickListener {
            EventBus.getDefault().postSticky(t)
            mListener?.onClickPinsItemImage(t,it)
        }

        linearlayoutTitleInfo.setOnClickListener {
            EventBus.getDefault().postSticky(t)
            mListener?.onClickPinsItemText(t,it)
        }

        txtGather.setOnClickListener {
            if (!isLogin){
                toast("请先登录再操作")
                return@setOnClickListener
            }
            //不做like操作了，
            t.repin_count ++
            txtGather.text = t.repin_count.toString()
            txtGather.setCompoundDrawablesRelativeWithIntrinsicBounds(CompatUtils.getTintListDrawable(context,
                    R.drawable.ic_favorite_black_18dp,R.color.tint_list_pink),null,null,null)
        }

        txtLike.setOnClickListener {
            if (!isLogin){
                toast("请先登录再操作")
                return@setOnClickListener
            }
            //不做like操作了，
            t.like_count ++
            helper.setText(R.id.txt_card_like,t.like_count.toString())
            txtLike.setCompoundDrawablesRelativeWithIntrinsicBounds(CompatUtils.getTintListDrawable(context,
                    R.drawable.ic_camera_black_18dp,R.color.tint_list_pink),null,null,null)
        }

        if (t.raw_text.isNullOrEmpty() && t.like_count <= 0 && t.repin_count <= 0){
            linearlayoutTitleInfo.visibility = View.GONE
        }
        else{
            linearlayoutTitleInfo.visibility = View.VISIBLE
            if (!t.raw_text.isNullOrEmpty()){
                helper.getView<TextView>(R.id.txt_card_title).text = t.raw_text!!
                helper.getView<TextView>(R.id.txt_card_title).visibility = View.VISIBLE
            }
            else{
                helper.getView<TextView>(R.id.txt_card_title).visibility = View.GONE
            }
            helper.setText(R.id.txt_card_gather,t.repin_count.toString())
            helper.setText(R.id.txt_card_like,t.like_count.toString())
        }

        var imgType = t.file?.type
        if (!imgType.isNullOrEmpty()){
            if (imgType!!.toLowerCase().contains("gif")  )  {
               helper.getView<ImageButton>(R.id.imgbtn_card_gif).visibility = View.VISIBLE
            }
            else{
                helper.getView<ImageButton>(R.id.imgbtn_card_gif).visibility = View.INVISIBLE
            }
        }

        ImageLoadBuilder.Start(context,img,imgUrl).setProgressBarImage(progressLoading).build()

    }

    override fun requestListData(page: Int): Subscription {
        val request = RetrofitClient.createService(PetalAPI::class.java)
        var result :  Observable<ListPinsBean>
        if (page == 0){
            result = request.httpsTypeLimitRx(mAuthorization!!,mKey,mLimit)
        }
        else{
            result = request.httpsTypeMaxLimitRx(mAuthorization!!,mKey,maxId, mLimit)
        }
        return result.map { it.pins }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object:Subscriber<List<PinsMainInfo>>(){
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable?) {
                        e?.printStackTrace()
                        loadError()
                        checkException(e)
                    }

                    override fun onNext(t: List<PinsMainInfo>?) {
                        if (t == null){
                            loadError()
                            return
                        }
                        if( t!!.size > 0 ){
                            maxId = t!!.last()!!.pin_id
                        }
                        loadSuccess(t!!)
                    }

                })
    }






}
