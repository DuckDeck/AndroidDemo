package stan.androiddemo.Media.OpenCV

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_basic_feature_detection.*
import org.opencv.android.OpenCVLoader
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import stan.androiddemo.R
import stan.androiddemo.tool.getMat
import stan.androiddemo.tool.setMat

class BasicFeatureDetectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_feature_detection)

        setSupportActionBar(toolbar)
        loadOpenCV()
        title = ""
//        "取反","与操作","或操作","异或操作"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navigation_view.setNavigationItemSelectedListener {
            drawer_layout_fixed.closeDrawers()
            println(it)
            when (it.title){
                "梯度计算"->{
                    gradient()
                }


            }
            return@setNavigationItemSelectedListener true
        }

    }

    fun gradient(){
        val m1 = imgOperate.getMat()
        val gradx = Mat()
        //Imgproc.Sobel(m1,gradx,CvType.CV_32F,1,0)
        Imgproc.Scharr(m1,gradx,CvType.CV_32F,1,0)
        Core.convertScaleAbs(gradx,gradx)
        Log.i("OpenCV","XGradient...")
        val grady = Mat()
        //Imgproc.Sobel(m1,grady,CvType.CV_32F,0,1)
        Imgproc.Scharr(m1,grady,CvType.CV_32F,0,1)
        Core.convertScaleAbs(grady,grady)
        Log.i("OpenCV","YGradient...")
        val m2 = Mat()
        Core.addWeighted(gradx,0.5,grady,0.5,0.0,m2)
        imgResult.setMat(m2)
        m1.release()
        m2.release()
        gradx.release()
        grady.release()
        //不明白为何变成空白了，这本书其他很烂
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){

            android.R.id.home->{
                drawer_layout_fixed.openDrawer(GravityCompat.START)
            }
        }
        return true
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
