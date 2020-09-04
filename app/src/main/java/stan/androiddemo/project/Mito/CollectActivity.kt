package stan.androiddemo.project.Mito

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.graphics.drawable.VectorDrawableCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.facebook.drawee.view.SimpleDraweeView
import kotlinx.android.synthetic.main.activity_collect.*
import org.litepal.crud.DataSupport
import stan.androiddemo.R
import stan.androiddemo.project.Mito.Model.ImageSetInfo
import stan.androiddemo.project.Mito.Model.Resolution

import stan.androiddemo.tool.ImageLoad.ImageLoadBuilder

class CollectActivity : AppCompatActivity() {

    var arrImageSet = ArrayList<ImageSetInfo>()
    lateinit var mAdapter: BaseQuickAdapter<ImageSetInfo, BaseViewHolder>
    lateinit var failView: View
    lateinit var errorHintView:TextView
    lateinit var progressLoading: Drawable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collect)

        toolbar.setNavigationOnClickListener { onBackPressed() }

        val imgs = ArrayList(DataSupport.findAll(ImageSetInfo::class.java).map {
            it.resolution = Resolution(it.resolutionStr)
            it
        })
        arrImageSet.addAll(imgs)



        val d0 = VectorDrawableCompat.create(resources,R.drawable.ic_toys_black_24dp,null)
        progressLoading = DrawableCompat.wrap(d0!!.mutate())
        DrawableCompat.setTint(progressLoading,resources.getColor(R.color.tint_list_pink))

        mAdapter = object:BaseQuickAdapter<ImageSetInfo,BaseViewHolder>(R.layout.image_set_item,arrImageSet){
            override fun convert(helper: BaseViewHolder, item: ImageSetInfo) {
                val img = helper.getView<SimpleDraweeView>(R.id.img_set)
                img.aspectRatio = item.resolution.pixelX.toFloat() / item.resolution.pixelY.toFloat()
                ImageLoadBuilder.Start(this.mContext,img,item.mainImage).setProgressBarImage(progressLoading).build()
                helper.setText(R.id.txt_image_title,item.title)
                helper.setText(R.id.txt_image_tag,item.category)
                helper.setText(R.id.txt_image_resolution,item.resolutionStr)
                helper.setText(R.id.txt_image_theme,item.theme)

                val imgCollect = helper.getView<ImageView>(R.id.img_mito_collect)

                if (item.isCollected){
                    imgCollect.setImageDrawable(resources.getDrawable(R.drawable.ic_star_black_24dp))
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
                            Toast.makeText(this.mContext,"收藏成功", Toast.LENGTH_LONG).show()
                        }
                        else{
                            Toast.makeText(this.mContext,"收藏失败", Toast.LENGTH_LONG).show()
                        }
                    }

//                    val result =  DataSupport.deleteAll(ImageSetInfo::class.java,"url = ' " + item.url + "'")
//                    item.isCollected = false
//                    imgCollect.setImageDrawable(resources.getDrawable(R.drawable.ic_star_border_black_24dp))

                }
            }
        }



        recycler_collect.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recycler_collect.adapter = mAdapter
        failView = View.inflate(this,R.layout.list_empty_hint,null)
        errorHintView = failView.findViewById(R.id.txt_hint)
        if (arrImageSet.size <= 0){
            errorHintView.text = "暂无收藏"
            mAdapter.emptyView = failView
        }

        mAdapter.setOnItemClickListener { adapter, view, position ->
            val set = arrImageSet[position]
            val intent = Intent(this,ImageSetActivity::class.java)
            intent.putExtra("set",set)
            startActivity(intent)
        }

    }
}
