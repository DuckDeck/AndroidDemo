package stan.androiddemo.project.petal.Module.Search


import android.content.Context
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.FrameLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseViewHolder
import com.facebook.drawee.view.SimpleDraweeView
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import stan.androiddemo.R
import stan.androiddemo.UI.BasePetalRecyclerFragment
import stan.androiddemo.project.petal.API.SearchAPI
import stan.androiddemo.project.petal.Config.Config
import stan.androiddemo.project.petal.Event.OnBoardFragmentInteractionListener
import stan.androiddemo.project.petal.HttpUtiles.RetrofitClient
import stan.androiddemo.project.petal.Model.BoardPinsInfo
import stan.androiddemo.project.petal.Observable.ErrorHelper
import stan.androiddemo.tool.ImageLoad.ImageLoadBuilder



class SearchPetalResultBoardFragment : BasePetalRecyclerFragment<BoardPinsInfo>() {
    lateinit var  mAttentionFormat :String
    lateinit var  mGatherFormat :String
    lateinit var  mUsernameFormat :String

    var mLimit = Config.LIMIT
    private var mListener: OnBoardFragmentInteractionListener<BoardPinsInfo>? = null
    override fun getTheTAG(): String {
        return  this.toString()
    }

    companion object {
        fun newInstance(type:String): SearchPetalResultBoardFragment {
            val fragment = SearchPetalResultBoardFragment()
            val bundle = Bundle()
            bundle.putString("key",type)
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(context,2)
    }


    override fun initView() {
        super.initView()
        mAttentionFormat = context.resources.getString(R.string.text_attention_number)
        mGatherFormat = context.resources.getString(R.string.text_gather_number)
        mUsernameFormat = context.resources.getString(R.string.text_by_username)
    }

    override fun getItemLayoutId(): Int {
       return R.layout.petal_cardview_board_item
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnBoardFragmentInteractionListener<*>)
        {
            mListener = context as  OnBoardFragmentInteractionListener<BoardPinsInfo>
        }
    }

    override fun itemLayoutConvert(helper: BaseViewHolder, t: BoardPinsInfo) {

        helper.getView<FrameLayout>(R.id.frame_layout_board).setOnClickListener {
            mListener?.onClickBoardItemImage(t,it)
        }

        helper.getView<TextView>(R.id.txt_board_username).setOnClickListener {
            mListener?.onClickBoardItemImage(t,it)
        }

        helper.setText(R.id.txt_board_title,t.title)
        helper.setText(R.id.txt_board_gather, String.format(mGatherFormat,t.pin_count))
        helper.setText(R.id.txt_board_attention, String.format(mAttentionFormat,t.follow_count))
        helper.setText(R.id.txt_board_username, String.format(mUsernameFormat,t.user?.username))
        var url = ""
        if (t.pins!= null && t.pins!!.size > 0){
            if (t.pins!!.first().file?.key != null){
                url = t.pins!!.first().file!!.key!!
            }
        }
        url =  String.format(mUrlGeneralFormat,url)
        val img = helper.getView<SimpleDraweeView>(R.id.img_card_board)
        img.aspectRatio = 1F
        ImageLoadBuilder.Start(context,img,url).setProgressBarImage(progressLoading).build()
    }

    override fun requestListData(page: Int): Subscription {
        return RetrofitClient.createService(SearchAPI::class.java).httpsBoardSearchRx(mAuthorization!!,mKey,page, mLimit)
                .flatMap {
                    ErrorHelper.getCheckNetError(it)
                }.map { it.boards }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object: Subscriber<List<BoardPinsInfo>>(){
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable?) {
                        e?.printStackTrace()
                        loadError()
                        checkException(e)
                    }

                    override fun onNext(t: List<BoardPinsInfo>?) {
                        if (t == null){
                            loadError()
                            return
                        }
                        loadSuccess(t!!)
                    }

                })
    }


}
