package stan.androiddemo.Media.OpenCV

import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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
        loadOpenCV()
        btnGray.setOnClickListener {
            val bitmap = BitmapFactory.decodeResource(this.resources,R.drawable.petal_header_bg)
            val src = Mat()
            val dst = Mat()
            Utils.bitmapToMat(bitmap,src)
            Imgproc.cvtColor(src,dst, Imgproc.COLOR_BGRA2GRAY)
            Utils.matToBitmap(dst,bitmap)
            imgGray.setImageBitmap(bitmap)
            src.release()
            dst.release()
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
