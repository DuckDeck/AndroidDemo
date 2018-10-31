package stan.androiddemo.Media.OpenCV

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
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import stan.androiddemo.R

class ImageToGrayActivity : AppCompatActivity() {

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
    }

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
        src.release()
        dst.release()
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
