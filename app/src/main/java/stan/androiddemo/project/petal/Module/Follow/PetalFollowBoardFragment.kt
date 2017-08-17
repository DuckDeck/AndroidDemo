package stan.androiddemo.project.petal.Module.Follow


import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import com.chad.library.adapter.base.BaseViewHolder
import com.facebook.drawee.view.SimpleDraweeView
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import stan.androiddemo.R
import stan.androiddemo.UI.BasePetalRecyclerFragment
import stan.androiddemo.project.petal.API.FollowAPI
import stan.androiddemo.project.petal.Config.Config
import stan.androiddemo.project.petal.Event.OnBoardFragmentInteractionListener
import stan.androiddemo.project.petal.HttpUtiles.RetrofitClient
import stan.androiddemo.project.petal.Model.BoardPinsInfo
import stan.androiddemo.tool.ImageLoad.ImageLoadBuilder


class PetalFollowBoardFragment : BasePetalRecyclerFragment<BoardPinsInfo>() {

     var mLimit = Config.LIMIT
    private val mAttentionFormat: String = resources.getString(R.string.text_gather_number)
    private val mGatherFormat: String = resources.getString(R.string.text_attention_number)
    private val mUsernameFormat: String = resources.getString(R.string.text_by_username)

    private var mListener: OnBoardFragmentInteractionListener<BoardPinsInfo>? = null


    companion object {
        fun newInstance():PetalFollowBoardFragment{
            return PetalFollowBoardFragment()
        }
    }

    override fun getTheTAG(): String {
        return this.toString()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnBoardFragmentInteractionListener<*>){
            mListener = context as OnBoardFragmentInteractionListener<BoardPinsInfo>
        }
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(context,2)
    }

    override fun getItemLayoutId(): Int {
        return R.layout.petal_cardview_board_item
    }

    override fun itemLayoutConvert(helper: BaseViewHolder, t: BoardPinsInfo) {
        helper.getView<TextView>(R.id.txt_board_title).text = t.title
        helper.getView<TextView>(R.id.txt_board_gather).text = String.format(mGatherFormat,t.pin_count)
        helper.getView<TextView>(R.id.txt_board_attention).text = String.format(mAttentionFormat,t.follow_count)
        helper.getView<TextView>(R.id.txt_board_username).text  = String.format(mUsernameFormat,t.user?.username)
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
        return   RetrofitClient.createService(FollowAPI::class.java).httpsMyFollowingBoardRx(mAuthorization!!,page,mLimit)
       .map { it.boards }.subscribeOn(Schedulers.io())
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
