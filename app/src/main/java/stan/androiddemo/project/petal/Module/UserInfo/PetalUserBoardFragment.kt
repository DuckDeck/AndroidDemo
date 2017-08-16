package stan.androiddemo.project.petal.Module.UserInfo


import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import com.chad.library.adapter.base.BaseViewHolder
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import stan.androiddemo.R
import stan.androiddemo.UI.BasePetalRecyclerFragment
import stan.androiddemo.project.petal.API.UserAPI
import stan.androiddemo.project.petal.Config.Config
import stan.androiddemo.project.petal.HttpUtiles.RetrofitClient


class PetalUserBoardFragment : BasePetalRecyclerFragment<UserBoardItemBean>() {
    private var mMaxId: Int = 0
    private var isMe: Boolean = false
    lateinit var mKey: String//用于联网查询的关键字
    var mLimit = Config.LIMIT
    var maxId = 0

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

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is PetalUserInfoActivity){
            mAuthorization = context.mAuthorization
            isMe = context.isMe
        }
    }



    override fun getItemLayoutId(): Int {
        return R.layout.petal_cardview_board_user_item
    }

    override fun itemLayoutConvert(helper: BaseViewHolder, userBoardItemBean: UserBoardItemBean) {

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
        return StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
    }
}
