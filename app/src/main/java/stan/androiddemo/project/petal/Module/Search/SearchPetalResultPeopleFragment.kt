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
import android.widget.TextView
import com.chad.library.adapter.base.BaseViewHolder
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

import stan.androiddemo.R
import stan.androiddemo.UI.BasePetalRecyclerFragment
import stan.androiddemo.UI.BaseRecyclerFragment
import stan.androiddemo.project.petal.API.SearchAPI
import stan.androiddemo.project.petal.Config.Config
import stan.androiddemo.project.petal.HttpUtiles.RetrofitClient
import stan.androiddemo.project.petal.Model.BoardPinsInfo
import stan.androiddemo.project.petal.Observable.ErrorHelper


class SearchPetalResultPeopleFragment : BasePetalRecyclerFragment<SearchPeopleBean.UsersBean>() {
    lateinit var mKey: String//用于联网查询的关键字
    lateinit var progressLoading: Drawable
    var mLimit = Config.LIMIT
    override fun getTheTAG(): String {
        return  this.toString()
    }

    companion object {
        fun newInstance(type:String): SearchPetalResultPeopleFragment {
            val fragment = SearchPetalResultPeopleFragment()
            val bundle = Bundle()
            bundle.putString("type",type)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initView() {
        super.initView()
        val d0 = VectorDrawableCompat.create(resources,R.drawable.ic_toys_black_24dp,null)
        progressLoading = DrawableCompat.wrap(d0!!.mutate())
        DrawableCompat.setTint(progressLoading,resources.getColor(R.color.tint_list_pink))
        mKey = arguments.getString("type")

    }

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    override fun getItemLayoutId(): Int {
        return R.layout.petal_cardview_user_item
    }

    override fun itemLayoutConvert(helper: BaseViewHolder, t: SearchPeopleBean.UsersBean) {

    }

    override fun requestListData(page: Int): Subscription {
        return RetrofitClient.createService(SearchAPI::class.java).httpsPeopleSearchRx(mAuthorization!!,mKey,page, mLimit)
                .subscribeOn(Schedulers.io())
                .flatMap {
                    ErrorHelper.getCheckNetError(it)
                }.map { it.users }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object: Subscriber<List<SearchPeopleBean.UsersBean>>(){
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable?) {
                        e?.printStackTrace()
                        loadError()
                        checkException(e)
                    }

                    override fun onNext(t: List<SearchPeopleBean.UsersBean>?) {
                        if (t == null){
                            loadError()
                            return
                        }
                        loadSuccess(t!!)

                    }

                })
    }


}
