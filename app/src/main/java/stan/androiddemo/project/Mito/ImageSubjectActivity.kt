package stan.androiddemo.project.Mito

import android.content.Intent
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.graphics.drawable.VectorDrawableCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.widget.Toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.facebook.drawee.view.SimpleDraweeView
import kotlinx.android.synthetic.main.activity_image_subject.*
import stan.androiddemo.Model.ResultInfo
import stan.androiddemo.R
import stan.androiddemo.project.Mito.Model.ImageSubjectInfo
import stan.androiddemo.tool.ImageLoad.ImageLoadBuilder

class ImageSubjectActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    //这个是专题合集列表
    var arrImageSubs = ArrayList<ImageSubjectInfo>()
    lateinit var mAdapter: BaseQuickAdapter<ImageSubjectInfo, BaseViewHolder>
    lateinit var failView: View
    lateinit var loadingView: View
    var index = 0
    lateinit var progressLoading: Drawable
    var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_subject)
        toolbar.setNavigationOnClickListener { onBackPressed() }


        val d0 = VectorDrawableCompat.create(resources,R.drawable.ic_toys_black_24dp,null)
        progressLoading = DrawableCompat.wrap(d0!!.mutate())
        DrawableCompat.setTint(progressLoading,resources.getColor(R.color.tint_list_pink))
        mAdapter = object:BaseQuickAdapter<ImageSubjectInfo,BaseViewHolder>(R.layout.mito_subject_image,arrImageSubs){
            override fun convert(helper: BaseViewHolder, item: ImageSubjectInfo) {
                val img = helper.getView<SimpleDraweeView>(R.id.img_mito)
                img.aspectRatio = 1.2f
                ImageLoadBuilder.Start(this@ImageSubjectActivity,img,item.subjectImage).setProgressBarImage(progressLoading).build()
                helper.setText(R.id.txt_subject_title,item.subjectName)

            }
        }

        swipe_refresh_subject_mito.setColorSchemeResources(R.color.colorPrimary)
        swipe_refresh_subject_mito.setOnRefreshListener(this)

        recycler_view_subject_images.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recycler_view_subject_images.adapter = mAdapter

        loadingView = View.inflate(this@ImageSubjectActivity,R.layout.list_loading_hint,null)
        failView = View.inflate(this@ImageSubjectActivity,R.layout.list_empty_hint,null)
        failView.setOnClickListener {
            mAdapter.emptyView = loadingView
            index = 0
            loadData()
        }
        mAdapter.emptyView = loadingView
        mAdapter.setEnableLoadMore(true)
        mAdapter.setOnLoadMoreListener({
            loadData()
        },recycler_view_subject_images)

        mAdapter.setOnItemClickListener { _, _, position ->
            val set = arrImageSubs[position]
            val intent = Intent(this@ImageSubjectActivity,ImageSubjectInfoActivity::class.java)
            intent.putExtra("subject",set)
            startActivity(intent)

        }
        loadData()
    }

    override fun onRefresh() {
        index = 0
        loadData()
    }

    fun loadData(){
        var url = "http://www.5857.com/zhuanti.html"


        if (index >= 1){
            if (count <= 0){
                mAdapter.loadMoreEnd()
                return
            }
            if (arrImageSubs.count() >= count){
                mAdapter.loadMoreEnd()
                return
            }
            else{
                url = "http://www.5857.com/zhuanti_"+(index + 1)+".html"
            }
        }

        ImageSubjectInfo.getSubjects(url) { result:ResultInfo ->
            runOnUiThread {
                count = result.count
                if (swipe_refresh_subject_mito != null){
                    swipe_refresh_subject_mito.isRefreshing = false
                }

                if (result.code != 0) {
                    Toast.makeText(this@ImageSubjectActivity,result.message, Toast.LENGTH_LONG).show()
                    mAdapter.emptyView = failView
                    return@runOnUiThread
                }
                val imageSubs = result.data!! as ArrayList<ImageSubjectInfo>
                if (imageSubs.size <= 0){
                    if (index == 0){
                        mAdapter.emptyView = failView
                        return@runOnUiThread
                    }
                    else{
                        mAdapter.loadMoreEnd()
                    }
                }
                if (index == 0){
                    arrImageSubs.clear()
                }
                else{
                    mAdapter.loadMoreComplete()
                }
                index ++
                arrImageSubs.addAll(imageSubs)
                mAdapter.notifyDataSetChanged()
            }
        }


    }
}
