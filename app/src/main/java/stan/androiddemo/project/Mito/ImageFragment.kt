package stan.androiddemo.project.Mito

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.fragment_image.*
import stan.androiddemo.Model.ResultInfo
import stan.androiddemo.R

import stan.androiddemo.project.Mito.Model.ImageSetInfo

import stan.androiddemo.project.Mito.Model.Resolution

class ImageFragment : Fragment() {


    var cat = "全部"
    var arrImageSet = ArrayList<ImageSetInfo>()
    lateinit var mAdapter:BaseQuickAdapter<ImageSetInfo,BaseViewHolder>
    lateinit var failView: View
    lateinit var loadingView: View
    var index = 0
    companion object {
        fun createFragment():ImageFragment{
            val fg = ImageFragment()
            return fg
        }
    }



    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_image, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cat =  arguments.getString("cat")
        mAdapter = object:BaseQuickAdapter<ImageSetInfo,BaseViewHolder>(R.layout.image_set_item,arrImageSet){
            override fun convert(helper: BaseViewHolder, item: ImageSetInfo) {
                Glide.with(this@ImageFragment).load(item.mainImage).into(helper.getView(R.id.img_set))
                helper.setText(R.id.txt_image_title,item.title)
                helper.setText(R.id.txt_image_tag,item.category.toString())
                helper.setText(R.id.txt_image_resolution,item.resolutionStr)
                helper.setText(R.id.txt_image_theme,item.theme.toString())
            }
        }
        swipe_refresh_mito.setColorSchemeResources(R.color.colorPrimary)
        swipe_refresh_mito.setOnRefreshListener {
            index = 0
            loadData()
        }
        recycler_images.layoutManager = GridLayoutManager(this@ImageFragment.context,2)
        recycler_images.adapter = mAdapter
        loadingView = View.inflate(this@ImageFragment.context,R.layout.list_loading_hint,null)
        failView = View.inflate(this@ImageFragment.context,R.layout.list_empty_hint,null)
        failView.setOnClickListener {
            mAdapter.emptyView = loadingView
            index = 0
            loadData()
        }
        mAdapter.emptyView = loadingView
        mAdapter.setEnableLoadMore(true)
        mAdapter.setOnLoadMoreListener({
            loadData()
        },recycler_images)

        mAdapter.setOnItemClickListener { adapter, view, position ->
            val set = arrImageSet[position]
            val intent = Intent(this@ImageFragment.context,ImageSetActivity::class.java)
            intent.putExtra("set",set)
            startActivity(intent)
        }

        loadData()
    }

    fun loadData(){
        ImageSetInfo.imageSets(cat,Resolution(),"全部",index,{ v: ResultInfo ->
            activity.runOnUiThread {
                swipe_refresh_mito.isRefreshing = false
                if (v.code != 0) {
                    Toast.makeText(this@ImageFragment.context,v.message, Toast.LENGTH_LONG).show()
                    mAdapter.emptyView = failView
                    return@runOnUiThread
                }
                val imageSets = v.data!! as ArrayList<ImageSetInfo>
                if (imageSets.size <= 0){
                    if (index == 0){
                        mAdapter.emptyView = failView
                        return@runOnUiThread
                    }
                    else{
                        mAdapter.loadMoreEnd()
                    }
                }
                if (index == 0){
                    arrImageSet.clear()
                }
                else{
                    mAdapter.loadMoreComplete()
                }
                index ++
                arrImageSet.addAll(imageSets)
                mAdapter.notifyDataSetChanged()
            }
        })
    }


}
