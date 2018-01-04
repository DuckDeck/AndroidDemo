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
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.Toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.facebook.drawee.view.SimpleDraweeView
import com.jakewharton.rxbinding.widget.RxTextView
import kotlinx.android.synthetic.main.activity_image_search.*
import org.litepal.crud.DataSupport
import rx.functions.Func1
import stan.androiddemo.Model.ResultInfo
import stan.androiddemo.R
import stan.androiddemo.project.Mito.Model.ImageSetInfo
import stan.androiddemo.tool.ConvertUrl
import stan.androiddemo.tool.ImageLoad.ImageLoadBuilder
import stan.androiddemo.tool.KeyboardTool
import java.util.concurrent.TimeUnit

class ImageSearchActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {



    var arrImageSet = ArrayList<ImageSetInfo>()
    lateinit var mAdapter:BaseQuickAdapter<ImageSetInfo,BaseViewHolder>
    lateinit var failView: View
    lateinit var loadingView: View
    var index = 0
    var imageCat = 0
    lateinit var progressLoading: Drawable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_search)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        RxTextView.editorActions(auto_txt_search_mito, Func1 {
            it == EditorInfo.IME_ACTION_SEARCH
        }).throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe {
                    swipe_refresh_search_mito.isRefreshing = true
                    onRefresh()
                }


        val d0 = VectorDrawableCompat.create(resources,R.drawable.ic_toys_black_24dp,null)
        progressLoading = DrawableCompat.wrap(d0!!.mutate())
        DrawableCompat.setTint(progressLoading,resources.getColor(R.color.tint_list_pink))
        mAdapter = object:BaseQuickAdapter<ImageSetInfo,BaseViewHolder>(R.layout.image_set_item,arrImageSet){
            override fun convert(helper: BaseViewHolder, item: ImageSetInfo) {
                val img = helper.getView<SimpleDraweeView>(R.id.img_set)
                img.aspectRatio = item.resolution.pixelX.toFloat() / item.resolution.pixelY.toFloat()
                ImageLoadBuilder.Start(this@ImageSearchActivity,img,item.mainImage).setProgressBarImage(progressLoading).build()
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
                            Toast.makeText(this@ImageSearchActivity,"收藏成功",Toast.LENGTH_LONG).show()
                        }
                        else{
                            Toast.makeText(this@ImageSearchActivity,"收藏失败",Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
        swipe_refresh_search_mito.setColorSchemeResources(R.color.colorPrimary)
        swipe_refresh_search_mito.setOnRefreshListener(this)

        recycler_view_search_images.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        recycler_view_search_images.adapter = mAdapter
        loadingView = View.inflate(this@ImageSearchActivity,R.layout.list_loading_hint,null)
        failView = View.inflate(this@ImageSearchActivity,R.layout.list_empty_hint,null)
        failView.setOnClickListener {
            mAdapter.emptyView = loadingView
            index = 0
            searchImage()
        }
        //mAdapter.emptyView = loadingView
        mAdapter.setEnableLoadMore(true)
        mAdapter.setOnLoadMoreListener({
            searchImage()
        },recycler_view_search_images)

        mAdapter.setOnItemClickListener { _, _, position ->
            val set = arrImageSet[position]
            val intent = Intent(this@ImageSearchActivity,ImageSetActivity::class.java)
            intent.putExtra("set",set)
            startActivity(intent)
        }
    }

    fun searchImage(){
        mAdapter.emptyView = loadingView
        KeyboardTool.hideKeyboard(this@ImageSearchActivity)
        ImageSetInfo.searchImages(auto_txt_search_mito.text.toString(),index,{result:ResultInfo ->
            runOnUiThread {
                if (swipe_refresh_search_mito != null){
                    swipe_refresh_search_mito.isRefreshing = false
                }

                if (result.code != 0) {
                    Toast.makeText(this@ImageSearchActivity,result.message, Toast.LENGTH_LONG).show()
                    mAdapter.emptyView = failView
                    return@runOnUiThread
                }
                val imageSets = result.data!! as ArrayList<ImageSetInfo>
                if (imageSets.size <= 0){
                    if (index == 0){
                        mAdapter.emptyView = failView
                    }
                    else{
                        mAdapter.loadMoreEnd()
                    }
                    return@runOnUiThread
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
                    it.imgBelongCat = imageCat
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

    override fun onRefresh() {
       index = 0
       searchImage()
    }
}
