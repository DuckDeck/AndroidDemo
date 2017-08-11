package stan.androiddemo.UI

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.listener.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_base_recycler.*
import rx.Subscription
import stan.androiddemo.R
import stan.androiddemo.project.petal.Event.OnFragmentRefreshListener


abstract class BasePetalRecyclerFragment<T> : BasePetalFragment(),OnFragmentRefreshListener{

    abstract fun getLayoutManager(): RecyclerView.LayoutManager

    abstract fun getItemLayoutId(): Int

    abstract fun itemLayoutConvert(helper: BaseViewHolder, t: T)

    abstract fun requestListData(page: Int): Subscription

    lateinit var mAdapter:BaseQuickAdapter<T,BaseViewHolder>

    lateinit var loadingView:View

    lateinit var errorView:View

    lateinit var errorText:TextView

    var arrItem = ArrayList<T>()

    var index = 0



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

    fun loadError(msg:String="加载失败，请重新再试"){
        errorText.text = msg
        mAdapter.emptyView = errorView
    }

    fun loadSuccess(list:List<T>){
        if (list.size <= 0 && index == 0){
            errorText.text = "暂无数据"
            mAdapter.emptyView = errorView
            return
        }
        else if(list.size <= 0 && index != 0){
            mAdapter.loadMoreComplete()
            return
        }
        if (index == 0){
            arrItem.clear()
        }
        arrItem.addAll(list)
        mAdapter.notifyDataSetChanged()
    }

    override fun refresh() {
        index = 0
        initData()
    }

}
