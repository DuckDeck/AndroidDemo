package stan.androiddemo.project.petal.Module.PetalList

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import com.chad.library.adapter.base.BaseViewHolder
import retrofit2.Retrofit
import rx.Subscription
import stan.androiddemo.R
import stan.androiddemo.UI.BaseRecyclerFragment
import stan.androiddemo.project.petal.API.PetalAPI
import stan.androiddemo.project.petal.HttpUtiles.RetrofitClient
import stan.androiddemo.project.petal.Model.PinsMainInfo


class PetalListFragment : BaseRecyclerFragment<PinsMainInfo>() {
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

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return  StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
    }

    override fun getItemLayoutId(): Int {
        return R.layout.petal_cardview_image_item
    }

    override fun itemLayoutConvert(helper: BaseViewHolder, t: PinsMainInfo) {

    }

    override fun requestListData(page: Int): Subscription {
//        return RetrofitClient.createService(PetalAPI::class.java)
    }


}
