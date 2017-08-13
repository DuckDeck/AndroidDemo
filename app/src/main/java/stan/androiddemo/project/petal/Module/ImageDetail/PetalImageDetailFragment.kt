package stan.androiddemo.project.petal.Module.ImageDetail


import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.graphics.drawable.VectorDrawableCompat
import android.support.v4.app.Fragment
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.chad.library.adapter.base.BaseViewHolder
import com.facebook.drawee.view.SimpleDraweeView
import licola.demo.com.huabandemo.Module.ImageDetail.PinsDetailBean
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

import stan.androiddemo.R
import stan.androiddemo.UI.BasePetalRecyclerFragment
import stan.androiddemo.project.petal.API.ImageDetailAPI
import stan.androiddemo.project.petal.Config.Config
import stan.androiddemo.project.petal.HttpUtiles.RetrofitClient
import stan.androiddemo.project.petal.Model.PinsMainInfo
import stan.androiddemo.project.petal.Module.Main.PetalActivity
import stan.androiddemo.project.petal.Module.Type.PetalTypeActivity
import stan.androiddemo.tool.CompatUtils
import stan.androiddemo.tool.ImageLoad.ImageLoadBuilder
import stan.androiddemo.tool.Logger
import stan.androiddemo.tool.TimeUtils


class PetalImageDetailFragment : BasePetalRecyclerFragment<PinsMainInfo>() {
    lateinit var mKey: String//用于联网查询的关键字
    var mLimit = Config.LIMIT
    var maxId = 0
    private var mPinsBean: PinsMainInfo? = null

    lateinit var  txt_image_text:TextView
    lateinit var  txt_image_link:TextView
    lateinit var  txt_image_gather:TextView
    lateinit var  txt_image_like:TextView
    lateinit var  txt_image_user:TextView
    lateinit var  txt_image_time:TextView
    lateinit var  txt_image_board:TextView
    lateinit var  img_image_user:SimpleDraweeView
    lateinit var  img_image_board_1:SimpleDraweeView
    lateinit var  img_image_board_2:SimpleDraweeView
    lateinit var  img_image_board_3:SimpleDraweeView
    lateinit var  img_image_board_4:SimpleDraweeView
    lateinit var ibtn_image_board_chevron_right:ImageButton
    lateinit var ibtn_image_user_chevron_right:ImageButton
    lateinit var mRLImageUser:RelativeLayout
    lateinit var mRLImageBoard:RelativeLayout

    private var mBoardId: String? = null
    private var mUserId: String? = null

    private var mBoardName: String? = null
    private var mUserName: String? = null
    lateinit var progressLoading: Drawable


    companion object {
        fun newInstance(key:String):PetalImageDetailFragment{
            val fragment = PetalImageDetailFragment()
            val bundle = Bundle()
            bundle.putString("type",key)
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun getTheTAG(): String {return this.toString()}

    override fun initView() {
        super.initView()
        val d0 = VectorDrawableCompat.create(resources,R.drawable.ic_toys_black_24dp,null)
        progressLoading = DrawableCompat.wrap(d0!!.mutate())
        DrawableCompat.setTint(progressLoading,resources.getColor(R.color.tint_list_pink))
        mKey = arguments.getString("type")
    }

    override fun initData() {
        addSubscription(requestOtherData())
        super.initData()
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
    }

    override fun getItemLayoutId(): Int {
        return R.layout.petal_cardview_image_item
    }

    override fun headView(): View? {
        val headView = layoutInflater.inflate(R.layout.petal_image_detail_head_view,null)
        txt_image_text = headView.findViewById<TextView>(R.id.txt_image_text)
        txt_image_link = headView.findViewById<TextView>( R.id.txt_image_link)
        txt_image_gather = headView.findViewById<TextView>(R.id.txt_image_gather)
        txt_image_like = headView.findViewById<TextView>( R.id.txt_image_like)
        txt_image_user = headView.findViewById<TextView>( R.id.txt_image_user)
        txt_image_time = headView.findViewById<TextView>( R.id.txt_image_about)
        txt_image_board = headView.findViewById<TextView>( R.id.txt_image_board)

        img_image_user = headView.findViewById<SimpleDraweeView>( R.id.img_image_user)
        img_image_board_1 = headView.findViewById<SimpleDraweeView>( R.id.img_image_board_1)
        img_image_board_2 = headView.findViewById<SimpleDraweeView>(R.id.img_image_board_2)
        img_image_board_3 = headView.findViewById<SimpleDraweeView>(R.id.img_image_board_3)
        img_image_board_4 = headView.findViewById<SimpleDraweeView>( R.id.img_image_board_4)

        ibtn_image_board_chevron_right = headView.findViewById(R.id.ibtn_image_board_chevron_right)
        ibtn_image_user_chevron_right = headView.findViewById(R.id.ibtn_image_user_chevron_right)
        mRLImageUser = headView.findViewById(R.id.relativelayout_image_user)
        mRLImageBoard = headView.findViewById(R.id.relativelayout_image_board)


        txt_image_gather.setCompoundDrawablesRelativeWithIntrinsicBounds(
                CompatUtils.getTintListDrawable(context,R.drawable.ic_camera_black_24dp,
                        R.color.tint_list_grey
        ),null,null,null)
        txt_image_like.setCompoundDrawablesRelativeWithIntrinsicBounds(
                CompatUtils.getTintListDrawable(context,R.drawable.ic_favorite_black_24dp,
                        R.color.tint_list_grey
                ),null,null,null)
        ibtn_image_board_chevron_right.setImageDrawable(
                CompatUtils.getTintListDrawable(context,R.drawable.ic_chevron_right_black_36dp,R.color.tint_list_grey)
        )
        ibtn_image_user_chevron_right.setImageDrawable(
                CompatUtils.getTintListDrawable(context,R.drawable.ic_chevron_right_black_36dp,R.color.tint_list_grey)
        )
        txt_image_link.setCompoundDrawablesRelativeWithIntrinsicBounds(
                CompatUtils.getTintListDrawable(context,R.drawable.ic_insert_link_black_24dp,
                        R.color.tint_list_grey
                ),null,null,null)
        return headView
    }


    fun setImageDetailInfo(pinsDetailBean:PinsDetailBean){
        val pin = pinsDetailBean.pin ?: return
        setImageTextInfo(pin!!.raw_text,pin!!.link,pin!!.source,pin!!.repin_count,pin!!.like_count)
        setImageUserInfo(pin!!.user?.avatar,pin!!.user?.username,pin!!.created_at)
        val url1 = String.format(mUrlSmallFormat, pin!!.board?.pins!![0].file?.key)
        val url2 = String.format(mUrlSmallFormat, pin!!.board?.pins!![1].file?.key)
        val url3 = String.format(mUrlSmallFormat, pin!!.board?.pins!![2].file?.key)
        val url4 = String.format(mUrlSmallFormat, pin!!.board?.pins!![3].file?.key)
        setImageBoardInfo(url1,url2,url3,url4,pin!!.board?.title)
    }

    fun setImageDetailInfo(bean:PinsMainInfo){

        setImageTextInfo(bean.raw_text,bean.link,bean.source,bean.repin_count,bean.like_count)
        setImageUserInfo(bean.pinsUserInfo?.avatar,bean.pinsUserInfo?.username,bean.pinsUserInfo!!.created_at)
        val url = String.format(mUrlSmallFormat, bean.file?.key)

        setImageBoardInfo(url,url,url,url,bean.pinsBoardInfo?.title)
    }

    fun setImageTextInfo(raw:String?,link:String?,source:String?,gather:Int,like:Int){
        if (!raw.isNullOrEmpty()){
            txt_image_text.text = raw!!
        }
        else{
            txt_image_text.text = resources.getString(R.string.text_image_describe_null)
        }

        if (!link.isNullOrEmpty() && !source.isNullOrEmpty()){
            txt_image_link.text = source!!
            txt_image_link.tag = link!!
        }
        else{
            txt_image_link.visibility = View.GONE
        }
        txt_image_gather.text = String.format(resources.getString(R.string.text_gather_number),gather)
        txt_image_like.text = String.format(resources.getString(R.string.text_like_number),like)
    }

    fun setImageUserInfo(urlHead:String?,userName:String?,createTime:Int){
        var url = ""
        if (!urlHead.isNullOrEmpty()){
            url = urlHead!!
            if (!urlHead!!.contains(resources.getString(R.string.httpRoot))){
                url = String.format(mUrlSmallFormat,urlHead!!)
            }
            ImageLoadBuilder.Start(context,img_image_user,url).setPlaceHolderImage(
                    CompatUtils.getTintDrawable(context,R.drawable.ic_account_circle_black_48dp,Color.GRAY)
            ).setIsCircle(true)
                    .build()
        }
        txt_image_user.text = userName
        txt_image_time.text = TimeUtils.getTimeDifference(createTime,System.currentTimeMillis())
    }

    fun setImageBoardInfo(url1:String?,url2:String?,url3:String?,url4:String?,boardName:String?){
        if (!boardName.isNullOrEmpty()){
            txt_image_board.text = boardName!!
        }
        else{
            txt_image_board.text = "暂无画板信息"
        }
        ImageLoadBuilder.Start(context,img_image_board_1,url1).setIsRadius(true,5F).build()
        ImageLoadBuilder.Start(context,img_image_board_2,url2).setIsRadius(true,5F).build()
        ImageLoadBuilder.Start(context,img_image_board_3,url3).setIsRadius(true,5F).build()
        ImageLoadBuilder.Start(context,img_image_board_4,url4).setIsRadius(true,5F).build()

    }

    override fun itemLayoutConvert(helper: BaseViewHolder, t: PinsMainInfo) {

        val txtGather = helper.getView<TextView>(R.id.txt_card_gather)
        var txtLike = helper.getView<TextView>(R.id.txt_card_like)
        //只能在这里设置TextView的drawable了

        txtGather.setCompoundDrawablesRelativeWithIntrinsicBounds(CompatUtils.getTintListDrawable(context,
                R.drawable.ic_favorite_black_18dp,R.color.tint_list_grey),null,null,null)
        txtLike.setCompoundDrawablesRelativeWithIntrinsicBounds(CompatUtils.getTintListDrawable(context,
                R.drawable.ic_camera_black_18dp,R.color.tint_list_grey),null,null,null)

        val imgUrl = String.format(mUrlGeneralFormat,t.file!!.key)
        val img = helper.getView<SimpleDraweeView>(R.id.img_card_main)
        img.aspectRatio = t.imgRatio

        if (t.raw_text.isNullOrEmpty() && t.like_count <= 0 && t.repin_count <= 0){
            helper.getView<LinearLayout>(R.id.linearLayout_image_title_info).visibility = View.GONE
        }
        else{
            helper.getView<LinearLayout>(R.id.linearLayout_image_title_info).visibility = View.VISIBLE
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
        }
        else{
            helper.getView<ImageButton>(R.id.imgbtn_card_gif).visibility = View.INVISIBLE
        }

        ImageLoadBuilder.Start(context,img,imgUrl).setProgressBarImage(progressLoading).build()
    }

    fun requestOtherData(): Subscription{
        return  RetrofitClient.createService(ImageDetailAPI::class.java).httpsPinsDetailRx(mAuthorization!!,mKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object:Subscriber<PinsDetailBean>(){
                    override fun onError(e: Throwable?) {
                        setImageDetailInfo(mPinsBean!!)
                    }

                    override fun onNext(t: PinsDetailBean?) {
                        if (t == null){
                            setImageDetailInfo(mPinsBean!!)
                        }
                        else{
                            setImageDetailInfo(t!!)
                        }

                    }

                    override fun onCompleted() {

                    }
                })
    }

    override fun requestListData(page: Int): Subscription {
        return RetrofitClient.createService(ImageDetailAPI::class.java)
                .httpPinsRecommendRx(mAuthorization!!,mKey,index,mLimit)
                .filter { it.size > 0 }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object:Subscriber<List<PinsMainInfo>>(){
                    override fun onNext(t: List<PinsMainInfo>?) {
                        if (t == null){
                            loadError()
                            return
                        }
                        loadSuccess(t!!)                    }

                    override fun onCompleted() {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onError(e: Throwable?) {
                        loadError()
                        checkException(e)
                    }

                })
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Logger.d(context.toString())
        if (context is PetalActivity){
            mAuthorization = context.mAuthorization
            //看要不要把事件交给activity来完成
        }
        if (context is PetalImageDetailActivity){
            mAuthorization = context.mAuthorization
            mPinsBean = context.mPinsBean
        }
    }


}
