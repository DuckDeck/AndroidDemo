package stan.androiddemo.Media.OpenCV

import android.graphics.Bitmap
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_string_image.*
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import stan.androiddemo.R

class StringImageActivity : AppCompatActivity() {
    
    val strs = arrayListOf("$", "@", "B", "%", "8", "&", "W", "M", "#", "*", "o", "a", "h", "k", "b", "d", "p", "q", "w", "m", "Z", "0", "o", "Q", "L", "C", "J", "U", "Y", "X", "z", "c", "v", "u", "n", "x", "r", "j", "f", "t", "/", "\\", "|", "(", ")", "1", "{", "}", "[", "]", "?", "-", "_", "+", "~", "<", ">", "i", "!", "l", "I", ";", ":", ",", "\"", "^", "", "'", ".", " ")


    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_string_image)
        setSupportActionBar(toolbar)
        loadOpenCV()
        title = ""

        txt_image.typeface = Typeface.createFromAsset(assets,"MSYHMONO.ttf")
            
        btn_change_image.setOnClickListener {
            img_operate.isDrawingCacheEnabled = true
            val bitmap = Bitmap.createBitmap(img_operate.drawingCache)
            img_operate.isDrawingCacheEnabled = false

            val src = Mat()

            val dst = Mat()
            Utils.bitmapToMat(bitmap,src)

            Imgproc.resize(src,src, Size(46.0,46.0))
            Imgproc.cvtColor(src,dst, Imgproc.COLOR_BGRA2GRAY)
//            Utils.matToBitmap(dst,bitmap)
//            img_operate.setImageBitmap(bitmap)
            src.release()
            val row = dst.rows()
            val col = dst.cols()

            val arrStr = arrayListOf<String>()


            for (i in 0 until row){
                val item = arrayListOf<String>()
                for (j in 0 until col){
                    val gray = dst.get(i,j)[0]
                    val percent = gray / 255.0
                    var count = ((strs.count() - 1) * percent).toInt()
                    item.add(strs[count])

                }
                arrStr.add(item.joinToString(" "))
            }
            dst.release()
            var result = arrStr.joinToString("\n")
            txt_image.text = result

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
