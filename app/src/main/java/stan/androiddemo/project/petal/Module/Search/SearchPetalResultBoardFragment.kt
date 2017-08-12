package stan.androiddemo.project.petal.Module.Search


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
import android.widget.ImageView
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
import stan.androiddemo.project.petal.HttpUtiles.RetrofitClient
import stan.androiddemo.project.petal.Model.BoardPinsInfo
import stan.androiddemo.project.petal.Model.PinsMainInfo
import stan.androiddemo.project.petal.Observable.ErrorHelper
import stan.androiddemo.tool.ImageLoad.ImageLoadBuilder



class SearchPetalResultBoardFragment : BasePetalRecyclerFragment<BoardPinsInfo>() {

    lateinit var mKey: String//用于联网查询的关键字
    lateinit var  mAttentionFormat :String
    lateinit var  mGatherFormat :String
    lateinit var  mUsernameFormat :String
    lateinit var progressLoading: Drawable
    var mLimit = Config.LIMIT

    override fun getTheTAG(): String {
        return  this.toString()
    }

    companion object {
        fun newInstance(type:String): SearchPetalResultBoardFragment {
            val fragment = SearchPetalResultBoardFragment()
            val bundle = Bundle()
            bundle.putString("type",type)
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }


    override fun initView() {
        super.initView()
        val d0 = VectorDrawableCompat.create(resources,R.drawable.ic_toys_black_24dp,null)
        progressLoading = DrawableCompat.wrap(d0!!.mutate())
        DrawableCompat.setTint(progressLoading,resources.getColor(R.color.tint_list_pink))
        mKey = arguments.getString("type")
        mAttentionFormat = context.resources.getString(R.string.text_attention_number)
        mGatherFormat = context.resources.getString(R.string.text_gather_number)
        mUsernameFormat = context.resources.getString(R.string.text_by_username)


    }

    override fun getItemLayoutId(): Int {
       return R.layout.petal_cardview_board_item
    }

    override fun itemLayoutConvert(helper: BaseViewHolder, t: BoardPinsInfo) {
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
