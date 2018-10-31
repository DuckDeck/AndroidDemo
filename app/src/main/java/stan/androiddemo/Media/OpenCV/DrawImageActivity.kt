package stan.androiddemo.Media.OpenCV

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_draw_image.*
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import stan.androiddemo.R


class DrawImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw_image)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = ""
        imgDraw.setImageBitmap(drawBitmapWithMat())
    }
    fun drawBitmapWithMat():Bitmap{
        val src = Mat.zeros(500,500,CvType.CV_8UC3)
        //draw ellipse
        Imgproc.ellipse(src, Point(250.0,250.0), Size(100.0,50.0),
                360.0,0.0,360.0, Scalar(0.0,0.0,255.0),2,8,0)
        Imgproc.putText(src,"stan rey draw the text", Point(20.0,20.0),Core.FONT_HERSHEY_PLAIN,1.0,
                Scalar(255.0,0.0,0.0),2)

        val rect = Rect(50,50,100,100)
        Imgproc.rectangle(src,rect.tl(),rect.br(), Scalar(0.0,255.0,0.0),2,8,0)
        Imgproc.circle(src, Point(400.0,400.0),50, Scalar(0.0,255.0,0.0),2,8,0)
        Imgproc.line(src, Point(10.0,10.0), Point(490.0,490.0), Scalar(0.0,255.0,0.0),2,8,0)
        Imgproc.line(src, Point(490.0,10.0), Point(10.0,490.0),Scalar(0.0,255.0,0.0),2,8,0)
        val bitmap = Bitmap.createBitmap(500,500,Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src,bitmap)
        src.release()
        return bitmap
    }

    fun drawBitmapWithCanvas(){
        //need canvas convert to bitmap do it later
    }
}
