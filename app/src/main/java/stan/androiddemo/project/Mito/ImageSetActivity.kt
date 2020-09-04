package stan.androiddemo.project.Mito

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.support.graphics.drawable.VectorDrawableCompat
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.facebook.drawee.view.SimpleDraweeView
import kotlinx.android.synthetic.main.activity_image_set.*
import stan.androiddemo.Model.ResultInfo
import stan.androiddemo.R
import stan.androiddemo.project.Mito.Model.ImageSetInfo
import stan.androiddemo.tool.ImageLoad.ImageLoadBuilder
import java.io.File

class ImageSetActivity : AppCompatActivity() {
    lateinit var imageSet:ImageSetInfo
    var arrImageUrl = ArrayList<String>()
    lateinit var mAdapter:BaseQuickAdapter<String,BaseViewHolder>
    lateinit var failView: View
    lateinit var loadingView: View
    var ratio:Float = 1F
    lateinit var progressLoading:Drawable
    var currentUrl = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_set)
        title = ""
        toolbar.setNavigationOnClickListener { onBackPressed() }
        imageSet = intent.getParcelableExtra("set")
        ratio = imageSet.resolution.pixelX.toFloat() / imageSet.resolution.pixelY.toFloat()
        val d0 = VectorDrawableCompat.create(resources,R.drawable.ic_toys_black_24dp,null)
        progressLoading = DrawableCompat.wrap(d0!!.mutate())
        DrawableCompat.setTint(progressLoading,resources.getColor(R.color.tint_list_pink))
        txt_toolbar_title.text = imageSet.title
        mAdapter = object:BaseQuickAdapter<String,BaseViewHolder>(R.layout.mito_image_solo_item,arrImageUrl){
            override fun convert(helper: BaseViewHolder, item: String) {
                val imgDownload = helper.getView<ImageView>(R.id.img_download)
//                Glide.with(this@ImageSetActivity).load(item).crossFade().listener(object:RequestListener<String,GlideDrawable>{
//                    override fun onException(e: Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
//                        return false
//                    }
//
//                    override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
//                        imgDownload.visibility = View.VISIBLE
//                        return false
//                    }
//
//                }).into(helper.getView(R.id.img_mito))

                val img = helper.getView<SimpleDraweeView>(R.id.img_mito)
                img.aspectRatio = ratio
                ImageLoadBuilder.Start(this@ImageSetActivity,img,item).setProgressBarImage(progressLoading).build()

                imgDownload.setOnClickListener {
                    if (ContextCompat.checkSelfPermission(this@ImageSetActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                            PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(this@ImageSetActivity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),1)
                        currentUrl = item
                        return@setOnClickListener
                    }
                    downloadImg(item)
                }
            }
        }

        float_button_search.setOnClickListener {

            if (ContextCompat.checkSelfPermission(this@ImageSetActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this@ImageSetActivity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),1)
                return@setOnClickListener
            }

            MaterialDialog.Builder(this).title("批量下载").content("你确定要批量下载这批图片吗？").positiveText("确定")
                    .negativeText("取消").onPositive { dialog, which ->
                for (i in 0 until  arrImageUrl.size){
                    if (i < arrImageUrl.size -1){
                        downloadImg(arrImageUrl[i],false)
                    }
                    else{
                        downloadImg(arrImageUrl[i],true)
                    }
                }
            }.show()

        }

        mAdapter.setOnItemClickListener { adapter, view, position ->

        }

        loadingView = View.inflate(this,R.layout.list_loading_hint,null)
        failView = View.inflate(this,R.layout.list_empty_hint,null)
        failView.setOnClickListener {
            mAdapter.emptyView = loadingView
            getImages()
        }
        mAdapter.emptyView = loadingView
        recycler_view_images.layoutManager = LinearLayoutManager(this)
        recycler_view_images.adapter = mAdapter
        getImages()
    }

    fun getImages(){
        ImageSetInfo.imageSet(imageSet) { v: ResultInfo ->
            runOnUiThread {
                if (v.code != 0) {
                    Toast.makeText(this,v.message, Toast.LENGTH_LONG).show()
                    mAdapter.emptyView = failView
                    return@runOnUiThread
                }
                val imageSets = v.data!! as ImageSetInfo
                if (imageSets.images.size <= 0){
                    mAdapter.emptyView = failView
                    return@runOnUiThread
                }
                arrImageUrl.addAll(imageSets.images)
                mAdapter.notifyDataSetChanged()
            }
        }
    }

    fun downloadImg(url:String,showSuccess:Boolean = true){
        object:AsyncTask<String,Int,File?>(){
            override fun doInBackground(vararg p0: String?): File? {
                var file:File? = null
                try {
                    val future = Glide.with(this@ImageSetActivity).load(url).downloadOnly(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL)
                    file = future.get()
                    val pictureFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absoluteFile
                    val fileDir = File(pictureFolder,"Mito")
                    if (!fileDir.exists()){
                        fileDir.mkdir()
                    }
                    val fileName = System.currentTimeMillis().toString() + ".jpg"
                    val destFile = File(fileDir,fileName)
                    file.copyTo(destFile)
                    sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.fromFile(File(destFile.path))))
                }
                catch (e:Exception){
                    e.printStackTrace()
                }
                return file
            }

            override fun onPostExecute(result: File?) {
                if (showSuccess){
                    Toast.makeText(this@ImageSetActivity,"保存图片成功",Toast.LENGTH_LONG).show()
                }
            }

        }.execute()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1->{
                if (grantResults.isNotEmpty() &&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    downloadImg(currentUrl)
                }
                else{
                    Toast.makeText(this@ImageSetActivity,"你没有允许保存文件",Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}
