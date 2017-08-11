package stan.androiddemo.UI

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.listener.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_base_recycler.*
import rx.Subscription
import stan.androiddemo.R


abstract class BasePetalRecyclerFragment<T> : BasePetalFragment() {

    abstract fun getLayoutManager(): RecyclerView.LayoutManager

    abstract fun getItemLayoutId(): Int

    abstract fun itemLayoutConvert(helper: BaseViewHolder, t: T)

    abstract fun requestListData(page: Int): Subscription

    lateinit var mAdapter:BaseQuickAdapter<T,BaseViewHolder>

    lateinit var loadingView:View

    lateinit var errorView:View

    lateinit var errorText:TextView

    var index = 0

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_base_recycler, container, false)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()

        initData()
    }

    open  protected fun initView(){
        recycler_base_fragment.setBackgroundColor(getBackgroundColor())
        index = startPageNumber()
        mAdapter = object:BaseQuickAdapter<T,BaseViewHolder>(getItemLayoutId()){
            override fun convert(helper: BaseViewHolder, item: T) {
                itemLayoutConvert(helper,item)
            }
        }

        recycler_base_fragment.layoutManager = getLayoutManager()
        if (getItemDecoration() != null){
            recycler_base_fragment.addItemDecoration(getItemDecoration())
        }
        recycler_base_fragment.adapter = mAdapter

        recycler_base_fragment.addOnItemTouchListener( object:OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                    onRecyclerViewItemClick(mAdapter.getItem(position)!!)
            }

            override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                onRecyclerViewItemChildClick(view!!,mAdapter.getItem(position)!!)
            }

        })

        loadingView = layoutInflater.inflate(R.layout.list_loading_hint,recycler_base_fragment,false)
        errorView = layoutInflater.inflate(R.layout.list_empty_hint,recycler_base_fragment,false)

        errorText = errorView.findViewById<TextView>(R.id.txt_hint)


        if (isNeedPaging()){
            mAdapter.setEnableLoadMore(true)
            mAdapter.setOnLoadMoreListener(BaseQuickAdapter.RequestLoadMoreListener {
                addSubscription(requestListData(index))
            },recycler_base_fragment)
        }

        mAdapter.emptyView = loadingView

    }

    open fun initData(){
        addSubscription(requestListData(index))
    }

    protected fun getBackgroundColor(): Int {
        return Color.WHITE
    }

    protected fun onRecyclerViewItemClick(item: T) {

    }

    protected fun onRecyclerViewItemChildClick(view: View, item: T) {

    }

    //这里可以使用一个统一的配置文件好一些

    protected fun isNeedPaging(): Boolean {
        return true
    }

    protected fun startPageNumber(): Int {
        return 0
    }

    open fun getItemDecoration(): RecyclerView.ItemDecoration?{
        return  null
    }

    fun refresh(){
        index = 0
        initData()
    }

}
