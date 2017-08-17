package stan.androiddemo.project.petal.Module.BoardDetail


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
import stan.androiddemo.project.petal.API.BoardDetailAPI
import stan.androiddemo.project.petal.API.ListPinsBean
import stan.androiddemo.project.petal.Config.Config
import stan.androiddemo.project.petal.Event.OnBoardDetailFragmentInteractionListener
import stan.androiddemo.project.petal.HttpUtiles.RetrofitClient
import stan.androiddemo.project.petal.Model.PinsMainInfo
import stan.androiddemo.tool.CompatUtils
import stan.androiddemo.tool.ImageLoad.ImageLoadBuilder


class BoardDetailFragment : BasePetalRecyclerFragment<PinsMainInfo>() {


    lateinit var mKey: String//用于联网查询的关键字
    var mLimit = Config.LIMIT
    var maxId = 0
    lateinit var mLLBoardUser: LinearLayout
    lateinit var mImageUser: SimpleDraweeView
    lateinit var mTVUserName: TextView
    lateinit var mTVBoardDescribe: TextView
    lateinit var mTVBoardAttention: TextView
    lateinit var mTVBoardGather: TextView
    internal val mStringGatherNumber = resources.getString(R.string.text_gather_number)
    internal val mStringAttentionNumber =  resources.getString(R.string.text_attention_number)
    private var mStringUserKey   = ""
    private var mStringUserTitle = ""

     var mListener: OnBoardDetailFragmentInteractionListener? = null

    override fun getTheTAG(): String {return this.toString()}

    companion object {
        fun newInstance(key:String):BoardDetailFragment{
            val fragment = BoardDetailFragment()
            val bundle = Bundle()
            bundle.putString("key",key)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun headView(): View? {
        val headView = layoutInflater.inflate(R.layout.petal_board_detail_head_info,null)
        mLLBoardUser = headView.findViewById(R.id.linearlayout_board_user)
        mImageUser = headView.findViewById(R.id.img_board_user)
        mTVUserName = headView.findViewById(R.id.tv_board_user)
        mTVBoardDescribe = headView.findViewById(R.id.tv_board_describe)
        mTVBoardAttention = headView.findViewById(R.id.tv_board_attention)
        mTVBoardGather = headView.findViewById(R.id.tv_board_gather)
        return headView
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnBoardDetailFragmentInteractionListener){
            mListener = context
        }
    }

    override fun initListener() {
        super.initListener()
        mLLBoardUser.setOnClickListener {
            mListener?.onClickUserField(mStringUserKey,mStringUserTitle)
        }
    }

    override fun initData() {
        super.initData()
        requestOtherData()
    }

    fun setBoardInfo(bean:BoardDetailBean){
        mListener?.onHttpBoardAttentionState(bean.board!!.user_id.toString(),bean.board!!.isFollowing)
        val urlHead = String.format(mUrlSmallFormat,bean.board?.user?.avatar)
        ImageLoadBuilder.Start(context,mImageUser,urlHead).setIsCircle(true).build()
        mTVUserName.text = bean.board?.user?.username
        if (!bean.board!!.description!!.isNullOrEmpty()){
            mTVBoardDescribe.text = bean.board!!.description!!
        }
        else{
            mTVBoardDescribe.text = resources.getString(R.string.text_board_describe_null)
        }
        mTVBoardGather.text = String.format(mStringGatherNumber,bean.board!!.pin_count)
        mTVBoardAttention.text = String.format(mStringAttentionNumber,bean.board!!.follow_count)
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return  StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
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

    fun requestOtherData(): Subscription{
        return  RetrofitClient.createService(BoardDetailAPI::class.java).httpsBoardDetailRx(mAuthorization!!,mKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object:Subscriber<BoardDetailBean>(){
                    override fun onNext(t: BoardDetailBean?) {

                    }

                    override fun onError(e: Throwable?) {
                        //// TODO: 2016/3/29 0029 从父activity应该能取出缓存 设值
                    }
                    override fun onCompleted() {

                    }
                })
    }

    override fun requestListData(page: Int): Subscription {
        val request = RetrofitClient.createService(BoardDetailAPI::class.java)
        var result : Observable<ListPinsBean>
        if (page == 0){
            result = request.httpsBoardPinsRx(mAuthorization!!,mKey,mLimit)
        }
        else{
            result = request.httpsBoardPinsMaxRx(mAuthorization!!,mKey,maxId, mLimit)
        }
        return result.map { it.pins }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object: Subscriber<List<PinsMainInfo>>(){
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
