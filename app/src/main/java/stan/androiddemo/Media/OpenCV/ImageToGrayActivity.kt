package stan.androiddemo.Media.OpenCV

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_image_to_gray.*


import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import stan.androiddemo.R
import kotlin.experimental.and

class ImageToGrayActivity : AppCompatActivity() {

    var currentChannelIndex = 0
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_to_gray)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        loadOpenCV()
        btnGray.setOnClickListener {
            toGray()
        }

        btnTakeImage.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        }
        btnChooseImage.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent,"图片选择..."),0x0001)

        }

        btnReviseImage.setOnClickListener {
            imgGray.isDrawingCacheEnabled = true
            val bitmap = Bitmap.createBitmap(imgGray.drawingCache)
            imgGray.isDrawingCacheEnabled = false
            val newBitmap = reverseImage(bitmap)
            imgGray.setImageBitmap(newBitmap)
        }

        btnGetChannel.setOnClickListener {
           val bitmap = getCurrentBitmap()
            val src = Mat()
            Utils.bitmapToMat(bitmap,src)
            txtImageInfo.text = "Channels:${src.channels()}-----Width:${src.width()}-----Height:${src.height()}"
            val channels = mutableListOf<Mat>()
            Core.split(src,channels)

            val m = channels[currentChannelIndex]
            val bm = Bitmap.createBitmap(m.cols(),m.rows(),Bitmap.Config.ARGB_8888)

            Log.i("channels",m.channels().toString())

            Utils.matToBitmap(m,bm)
            imgGray.setImageBitmap(bm)
            m.release()

            src.release()//不明白为什么都是一样的，书上连个图也不给一个
            currentChannelIndex++
            if (currentChannelIndex == channels.count()){
                currentChannelIndex = 0
            }
        }
    }

    fun getCurrentBitmap():Bitmap{
        imgGray.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(imgGray.drawingCache)
        imgGray.isDrawingCacheEnabled = false
        return  bitmap
    }

    @SuppressLint("SetTextI18n")
    fun toGray(){
        imgGray.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(imgGray.drawingCache)
        imgGray.isDrawingCacheEnabled = false

        val src = Mat()

        val dst = Mat()
        Utils.bitmapToMat(bitmap,src)
        Imgproc.cvtColor(src,dst, Imgproc.COLOR_BGRA2GRAY)
        Utils.matToBitmap(dst,bitmap)
        imgGray.setImageBitmap(bitmap)
        txtImageInfo.text = "Channels:${src.channels()}-----Width:${src.width()}-----Height:${src.height()}"
        src.release()
        dst.release()
    }



    @SuppressLint("SetTextI18n")
    fun reverseImage(bitmap: Bitmap):Bitmap{
        val src = Mat()
        Utils.bitmapToMat(bitmap,src)
        txtImageInfo.text = "Channels:${src.channels()}-----Width:${src.width()}-----Height:${src.height()}"
        val data = ByteArray(src.channels()*src.width()*src.height())
        src.get(0,0,data)
        val reversedData = data.map {
            (255 - (it and  127).toInt()).toByte()
        }
        src.put(0,0,reversedData.toByteArray())
        Utils.matToBitmap(src,bitmap)
        src.release()
        return  bitmap
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0x0001){
            if (resultCode == Activity.RESULT_OK){
                if (data != null){
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,data!!.data)
                    imgGray.setImageBitmap(bitmap)
                }
            }
        }
    }



    private fun loadOpenCV(){
        val success = OpenCVLoader.initDebug()
        if (success){
            Log.i("cvtag","OpenCV Libraries loaded...")
        }
        else{
            Toast.makeText(this.applicationContext,"Warning:Could not load the OpenCV Libraries", Toast.LENGTH_SHORT).show()
        }
    }
}
