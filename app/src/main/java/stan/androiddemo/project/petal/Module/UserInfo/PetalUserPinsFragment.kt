package stan.androiddemo.project.petal.Module.UserInfo


import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import com.chad.library.adapter.base.BaseViewHolder
import rx.Subscription
import stan.androiddemo.UI.BasePetalRecyclerFragment
import stan.androiddemo.project.petal.Model.PinsMainInfo


/**
 * A simple [Fragment] subclass.
 */
class PetalUserPinsFragment : BasePetalRecyclerFragment<PinsMainInfo>() {
    override fun getTheTAG(): String {
        return this.toString()
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemLayoutId(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun itemLayoutConvert(helper: BaseViewHolder, t: PinsMainInfo) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun requestListData(page: Int): Subscription {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}// Required empty public constructor
