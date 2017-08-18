package stan.androiddemo.project.petal.Module.UserInfo


import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseViewHolder
import com.facebook.drawee.view.SimpleDraweeView
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import stan.androiddemo.R
import stan.androiddemo.UI.BasePetalRecyclerFragment
import stan.androiddemo.project.petal.API.OperateAPI
import stan.androiddemo.project.petal.API.UserAPI
import stan.androiddemo.project.petal.Config.Config
import stan.androiddemo.project.petal.Event.OnBoardFragmentInteractionListener
import stan.androiddemo.project.petal.Event.OnEditDialogInteractionListener
import stan.androiddemo.project.petal.HttpUtiles.RetrofitClient
import stan.androiddemo.project.petal.Widget.BoardEditDialogFragment
import stan.androiddemo.tool.CompatUtils
import stan.androiddemo.tool.DialogUtils
import stan.androiddemo.tool.ImageLoad.ImageLoadBuilder
import stan.androiddemo.tool.Logger


class PetalUserBoardFragment : BasePetalRecyclerFragment<UserBoardItemBean>(), OnEditDialogInteractionListener {


    private var mMaxId: Int = 0
    private var isMe: Boolean = false
    var mLimit = Config.LIMIT
    var maxId = 0

    lateinit var mAttentionFormat: String//关注数量
    lateinit var mGatherFormat: String//采集数量
    lateinit var mOperateEdit: String//编辑
    lateinit var mOperateFollowing: String//关注
    lateinit var mOperateFollowed: String//已关注

    lateinit var mDrawableBlock: Drawable
    lateinit var mDrawableEdit: Drawable
    lateinit var mDrawableFollowing: Drawable
    lateinit var mDrawableFollowed: Drawable
    private var mListener: OnBoardFragmentInteractionListener<UserBoardItemBean>? = null
    companion object {
        fun newInstance(key:String):PetalUserBoardFragment{
            val fragment = PetalUserBoardFragment()
            val bundle = Bundle()
            bundle.putString("key",key)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getTheTAG(): String {
        return this.toString()
    }

    override fun initView() {
        super.initView()
        mGatherFormat = resources.getString(R.string.text_gather_number)
        mAttentionFormat = resources.getString(R.string.text_attention_number)
        mOperateEdit = resources.getString(R.string.text_edit)
        mOperateFollowing = resources.getString(R.string.text_following)
        mOperateFollowed = resources.getString(R.string.text_followed)
        mDrawableBlock = CompatUtils.getTintListDrawable(context,R.drawable.ic_block_black_24dp,R.color.tint_list_grey)
        mDrawableEdit = CompatUtils.getTintListDrawable(context,R.drawable.ic_mode_edit_black_24dp,R.color.tint_list_grey)
        mDrawableFollowing = CompatUtils.getTintListDrawable(context,R.drawable.ic_add_black_24dp,R.color.tint_list_grey)
        mDrawableFollowed = CompatUtils.getTintListDrawable(context,R.drawable.ic_check_black_24dp,R.color.tint_list_grey)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnBoardFragmentInteractionListener<*>){
            mListener = context as OnBoardFragmentInteractionListener<UserBoardItemBean>
        }
        if (context is PetalUserInfoActivity){
            mAuthorization = context.mAuthorization
            isMe = context.isMe
        }
    }


    override fun initListener() {
        super.initListener()

    }

    override fun getItemLayoutId(): Int {
        return R.layout.petal_cardview_board_user_item
    }

    override fun itemLayoutConvert(helper: BaseViewHolder, userBoardItemBean: UserBoardItemBean) {
        var isOperate = false
        if (userBoardItemBean.deleting != 0){
            isOperate = true
        }
        val isFollowing = userBoardItemBean.following
        val drawable: Drawable
        val text: String

        if (isOperate) {
            if (isMe) {
                text = mOperateEdit
                drawable = mDrawableEdit
            } else {
                if (isFollowing) {
                    text = mOperateFollowed
                    drawable = mDrawableFollowed
                } else {
                    text = mOperateFollowing
                    drawable = mDrawableFollowing
                }
            }
        } else {
            drawable = mDrawableBlock
            text = ""
        }

        helper.getView<TextView>(R.id.tv_board_operate).text = text
        helper.getView<LinearLayout>(R.id.linearlayout_group).tag = isOperate
        helper.getView<TextView>(R.id.tv_board_operate).setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null)
        helper.getView<TextView>(R.id.tv_board_title).text = userBoardItemBean.title
        helper.getView<TextView>(R.id.tv_board_gather).text = String.format(mGatherFormat,userBoardItemBean.pin_count)
        helper.getView<TextView>(R.id.tv_board_attention).text = String.format(mAttentionFormat,userBoardItemBean.follow_count)
        val img = helper.getView<SimpleDraweeView>(R.id.img_card_image)

        img.setOnClickListener {
            mListener?.onClickBoardItemImage(userBoardItemBean,it)
        }

        helper.getView<TextView>(R.id.tv_board_operate).setOnClickListener {
            handleOperate(userBoardItemBean,it)
        }

        if (userBoardItemBean.pins!!.size > 0){
            val url = String.format(mUrlGeneralFormat,userBoardItemBean.pins!![0].file!!.key)
            img.aspectRatio = 1F
            ImageLoadBuilder.Start(context,img,url).setPlaceHolderImage(progressLoading).build()
        }
        else{
            ImageLoadBuilder.Start(context,img,"").setPlaceHolderImage(progressLoading).build()
        }

    }

     fun handleOperate(bean: UserBoardItemBean, view: View) {

        if (isMe) {
            //如果是我的画板 弹出编辑对话框
            val fragment = BoardEditDialogFragment.create(bean.board_id.toString(), bean.title!!, bean.description!!, bean.category_id.toString())
            fragment.mListener = this//注入已经实现接口的 自己
            fragment.show(activity.supportFragmentManager, null)
        } else {
            //如果是其他用户的画板 直接操作
            Logger.d()
        }
    }

    override fun requestListData(page: Int): Subscription {
        val request = RetrofitClient.createService(UserAPI::class.java)
        var result : Observable<UserBoardListBean>
        if (page == 0){
            result = request.httpsUserBoardRx(mAuthorization!!,mKey,mLimit)
        }
        else{
            result = request.httpsUserBoardMaxRx(mAuthorization!!,mKey,maxId, mLimit)
        }
        return result.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .map { it.boards }
                .subscribe(object:Subscriber<List<UserBoardItemBean>>(){
                    override fun onNext(t: List<UserBoardItemBean>?) {
                        if (t == null){
                            loadError()
                            return
                        }
                        if( t!!.size > 0 ){
                            maxId = t!!.last()!!.board_id
                        }
                        loadSuccess(t!!)
                        if (t!!.size < mLimit){
                            setNoMoreData()
                        }
                    }

                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable?) {
                        e?.printStackTrace()
                        loadError()
                        checkException(e)
                    }

                })
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(context,2)
    }


    override fun onDialogPositiveClick(boardId: String, name: String, describe: String, selectType: String) {
        Logger.d("name=$name describe=$describe selectPosition=$selectType")
        RetrofitClient.createService(OperateAPI::class.java).httpsEditBoard(mAuthorization!!,boardId,name,describe,selectType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    setRefresh()
                }
    }

    override fun onDialogNeutralClick(boardId: String, boardTitle: String) {
        DialogUtils.showDeleteDialog(context, boardTitle, DialogInterface.OnClickListener { dialog, which -> startDeleteBoard(boardId) })
    }

    fun startDeleteBoard(boardId:String){
        RetrofitClient.createService(OperateAPI::class.java).httpsDeleteBoard(mAuthorization!!,boardId,Config.OPERATEDELETEBOARD)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    setRefresh()
                }
    }
}
