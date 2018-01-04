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
import android.widget.ImageView
import android.widget.Toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.facebook.drawee.view.SimpleDraweeView
import kotlinx.android.synthetic.main.activity_image_subject_info.*
import org.litepal.crud.DataSupport
import stan.androiddemo.Model.ResultInfo

import stan.androiddemo.R
import stan.androiddemo.project.Mito.Model.ImageSetInfo
import stan.androiddemo.project.Mito.Model.ImageSubjectInfo
import stan.androiddemo.tool.ConvertUrl
import stan.androiddemo.tool.ImageLoad.ImageLoadBuilder

class ImageSubjectInfoActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {


    var arrImageSet = ArrayList<ImageSetInfo>()
    lateinit var mAdapter: BaseQuickAdapter<ImageSetInfo, BaseViewHolder>
    lateinit var imageSubject:ImageSubjectInfo
    lateinit var failView: View
    lateinit var loadingView: View
    lateinit var progressLoading: Drawable
    var index = 0
    var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_subject_info)

        toolbar.setNavigationOnClickListener { onBackPressed() }
        imageSubject = intent.getParcelableExtra<ImageSubjectInfo>("subject")
        txt_toolbar_title.text = imageSubject.subjectName
        val d0 = VectorDrawableCompat.create(resources,R.drawable.ic_toys_black_24dp,null)
        progressLoading = DrawableCompat.wrap(d0!!.mutate())
        DrawableCompat.setTint(progressLoading,resources.getColor(R.color.tint_list_pink))
        mAdapter = object:BaseQuickAdapter<ImageSetInfo,BaseViewHolder>(R.layout.image_set_item,arrImageSet){
            override fun convert(helper: BaseViewHolder, item: ImageSetInfo) {
                val img = helper.getView<SimpleDraweeView>(R.id.img_set)
                img.aspectRatio = item.resolution.pixelX.toFloat() / item.resolution.pixelY.toFloat()
                ImageLoadBuilder.Start(this@ImageSubjectInfoActivity,img,item.mainImage).setProgressBarImage(progressLoading).build()
                helper.setText(R.id.txt_image_title,item.title)
                helper.setText(R.id.txt_image_tag,item.category)
                helper.setText(R.id.txt_image_resolution,item.resolutionStr)
                helper.setText(R.id.txt_image_theme,item.theme)



                val imgCollect = helper.getView<ImageView>(R.id.img_mito_collect)

                if (item.isCollected){
                    imgCollect.setImageDrawable(resources.getDrawable(R.drawable.ic_star_black_24dp))
                }
                else{
                    imgCollect.setImageDrawable(resources.getDrawable(R.drawable.ic_star_border_black_24dp))
                }

                imgCollect.setOnClickListener {
                    if (item.isCollected){
                        imgCollect.setImageDrawable(resources.getDrawable(R.drawable.ic_star_border_black_24dp))
                        DataSupport.deleteAll(ImageSetInfo::class.java,"hashId = " + item.hashId )
                        item.isCollected = !item.isCollected
                    }
                    else{
                        imgCollect.setImageDrawable(resources.getDrawable(R.drawable.ic_star_black_24dp))
                        item.isCollected = !item.isCollected
                        val result =  item.save()
                        if (result){
                            Toast.makeText(this@ImageSubjectInfoActivity,"收藏成功",Toast.LENGTH_LONG).show()
                        }
                        else{
                            Toast.makeText(this@ImageSubjectInfoActivity,"收藏失败",Toast.LENGTH_LONG).show()
                        }
                    }
                }

            }
        }
        swipe_refresh_subjectinfo_mito.setColorSchemeResources(R.color.colorPrimary)
        swipe_refresh_subjectinfo_mito.setOnRefreshListener(this)

        recycler_view_subjectinfo_images.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        recycler_view_subjectinfo_images.adapter = mAdapter
        loadingView = View.inflate(this@ImageSubjectInfoActivity,R.layout.list_loading_hint,null)
        failView = View.inflate(this@ImageSubjectInfoActivity,R.layout.list_empty_hint,null)
        failView.setOnClickListener {
            mAdapter.emptyView = loadingView
            index = 0
            loadData()
        }
        mAdapter.emptyView = loadingView
        mAdapter.setEnableLoadMore(true)
        mAdapter.setOnLoadMoreListener({
            loadData()
        },recycler_view_subjectinfo_images)

        mAdapter.setOnItemClickListener { _, _, position ->
            val set = arrImageSet[position]
            val intent = Intent(this@ImageSubjectInfoActivity,ImageSetActivity::class.java)
            intent.putExtra("set",set)
            startActivity(intent)
        }

        loadData()
    }

    override fun onRefresh() {

    }

    fun loadData(){
        var url = imageSubject.subjectUrl
        if (index >= 1){
            if (count <= 0){
                mAdapter.loadMoreEnd()
                return
            }
            if (arrImageSet.count() >= count){
                mAdapter.loadMoreEnd()
                return
            }
            else{
                url = url + "index-"+ (index + 1) +".html"
            }
        }
        ImageSubjectInfo.getImageSubject(url,{result:ResultInfo ->
            runOnUiThread {
                count = result.count
                if (swipe_refresh_subjectinfo_mito != null){
                    swipe_refresh_subjectinfo_mito.isRefreshing = false
                }

                if (result.code != 0) {
                    Toast.makeText(this@ImageSubjectInfoActivity,result.message, Toast.LENGTH_LONG).show()
                    mAdapter.emptyView = failView
                    return@runOnUiThread
                }
                val imageSets = result.data!! as ArrayList<ImageSetInfo>
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
                val collectedImages = DataSupport.findAll(ImageSetInfo::class.java)
                arrImageSet.addAll(imageSets.map {
                    val img = it
                    if (collectedImages.find { it.hashId == img.hashId } != null){
                        it.isCollected = true
                    }
                    it
                })
                mAdapter.notifyDataSetChanged()
            }
        })
    }
}
