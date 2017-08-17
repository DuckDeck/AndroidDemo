package stan.androiddemo.project.petal.Module.Search


import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageButton
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
import stan.androiddemo.project.petal.Observable.ErrorHelper
import stan.androiddemo.tool.CompatUtils
import stan.androiddemo.tool.ImageLoad.ImageLoadBuilder


class SearchPetalResultPeopleFragment : BasePetalRecyclerFragment<SearchPeopleBean.UsersBean>() {
    lateinit var mKey: String//用于联网查询的关键字
    var mLimit = Config.LIMIT
    lateinit var mFansFormat: String
    lateinit var mHttpRoot: String

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
        mKey = arguments.getString("type")
        mFansFormat = context.resources.getString(R.string.text_fans_number)
        mHttpRoot = context.resources.getString(R.string.httpRoot)
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(context)
    }

    override fun getItemLayoutId(): Int {
        return R.layout.petal_cardview_user_item
    }

    override fun itemLayoutConvert(helper: BaseViewHolder, t: SearchPeopleBean.UsersBean) {
        helper.getView<ImageButton>(R.id.ibtn_image_user_chevron_right).setImageDrawable(
                CompatUtils.getTintListDrawable(context,R.drawable.ic_chevron_right_black_36dp,R.color.tint_list_grey)
        )
        helper.setText(R.id.txt_image_user,t.username)
        helper.setText(R.id.txt_image_about,String.format(mFansFormat,t.follower_count))
        var url = t.avatar
        val img = helper.getView<SimpleDraweeView>(R.id.img_petal_user)
        if (!url.isNullOrEmpty()){
            if (!url!!.contains(mHttpRoot)){
                url = String.format(mUrlSmallFormat,url)
            }
            ImageLoadBuilder.Start(context,img,url).setPlaceHolderImage(CompatUtils.getTintDrawable(context,
                            R.drawable.ic_account_circle_gray_48dp, Color.GRAY))
                    .setIsCircle(true)
                    .build()
        }
        else{
            img.hierarchy.setPlaceholderImage(R.drawable.ic_account_circle_gray_48dp)
        }
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
