package stan.androiddemo.Media.OpenCV

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.view.GravityCompat
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_image_operate.*
import org.opencv.android.OpenCVLoader
import org.opencv.core.*
import org.opencv.imgproc.Imgproc


import stan.androiddemo.R
import stan.androiddemo.tool.getBitmap
import stan.androiddemo.tool.getMat
import stan.androiddemo.tool.setMat
import java.util.*

class ImageOperateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_operate)


        setSupportActionBar(toolbar)
        loadOpenCV()
        title = ""
//        "取反","与操作","或操作","异或操作"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navigation_view.setNavigationItemSelectedListener {
            drawer_layout_fixed.closeDrawers()
            println(it)
            when (it.title){
                "取反"->{
                    reverseMat()
                }
                "与操作"->{
                    andMat()
                }
                "或操作"->{
                    orMat()
                }
                "异或操作"->{
                    norMat()
                }
                "线性绝对值放缩变换"->{
                    scaleAbsMat()
                }
                "归一化"->{
                    normalizeMat()
                }
                "均值模糊"->{
                    avgBlur()
                }
                "高斯模糊"->{
                    gothBur()
                }
                "中值滤波"->{
                    middleBlur()
                }
                "最大值滤波"->{
                    minBlur()
                }
                "最小值滤波"->{
                    maxBlur()
                }

                "高斯双边滤波"->{
                    bilateralBlur()
                }
                "均值迁移滤波"->{
                    shiftFilter()
                }
                "自定义滤波"->{
                    customFilter()
                }
                "阈值相关"->{
                thresholdFilter()
            }
            }
            return@setNavigationItemSelectedListener true
        }
    }


    private fun reverseMat(){
        reset()
        val m1 = imgOperate.getMat()
        val m2 = Mat()
        Core.bitwise_not(m1,m2)
        //可能是因为同时操作四个通道，本来是不透明的，因为取反就变成完全透明了
        val m3 = Mat()
        Imgproc.cvtColor(m2,m3,Imgproc.COLOR_RGBA2RGB)
        imgResult.setMat(m3)
        m1.release()
        m2.release()
        m3.release()
    }


    private fun andMat(){
        //两者分辨率要一样才行
        reset()
        val m1 = imgOperate.getMat()
        val m2 = imgResult.getMat()
        if (m1.width() != m2.width() || m1.height() != m2.height()){
            Toast.makeText(this,"两者分辨率不一样！",Toast.LENGTH_SHORT).show()
            return
        }
        val m3 = Mat()
        Core.bitwise_and(m1,m2,m3)
        imgResult.setMat(m3)
        m1.release()
        m2.release()
        m3.release()
    }

    private fun orMat(){
        reset()
        val m1 = imgOperate.getMat()
        val m2 = imgResult.getMat()
        if (m1.width() != m2.width() || m1.height() != m2.height()){
            Toast.makeText(this,"两者分辨率不一样！",Toast.LENGTH_SHORT).show()
            return
        }
        val m3 = Mat()
        Core.bitwise_or(m1,m2,m3)
        imgResult.setMat(m3)
        m1.release()
        m2.release()
        m3.release()
    }

    private fun norMat(){
        reset()
        val m1 = imgOperate.getMat()
        val m2 = imgResult.getMat()
        if (m1.width() != m2.width() || m1.height() != m2.height()){
            Toast.makeText(this,"两者分辨率不一样！",Toast.LENGTH_SHORT).show()
            return
        }
        val m3 = Mat()
        Core.bitwise_xor(m1,m2,m3)
        val m4 = Mat()
        Imgproc.cvtColor(m3,m4,Imgproc.COLOR_RGBA2RGB)
        imgResult.setMat(m4)
        m1.release()
        m2.release()
        m3.release()
        m4.release()
    }

    private fun  scaleAbsMat(){
        reset()
        val m1 = imgOperate.getMat()
        val m2 = Mat()
        Core.convertScaleAbs(m1,m2)
        imgResult.setMat(m2)
        m1.release()
        m2.release()

    }

    private fun normalizeMat() {
        //创建随机浮点数图像
        val m1 = Mat.zeros(400, 400, CvType.CV_32FC3)
        val data = FloatArray(400*400*3)
        val random = Random()
        for (i in 0 until data.count()){
            data[i] = random.nextGaussian().toFloat()
        }
        m1.put(0,0,data)

        //将值归化到0-255之间
        val m2 = Mat()

        m1.convertTo(m2,CvType.CV_8UC3)
        imgOperate.setMat(m2)
        Core.normalize(m1,m2,0.0,255.0,Core.NORM_MINMAX,-1,Mat())
        val m3 = Mat()
        m2.convertTo(m3,CvType.CV_8UC3)
        imgResult.setMat(m3)
        m1.release()
        m2.release()
        m3.release()
    }

    private fun avgBlur(){
        val m1 = imgOperate.getMat()
        val m2 = Mat()
        Imgproc.blur(m1,m2, Size(15.0,15.0), Point(-1.0,-1.0),Core.BORDER_REFLECT)
        //水平方向模糊
        //Imgproc.blur(m1,m2, Size(15.0,1.0), Point(-1.0,-1.0),Core.BORDER_DEFAULT)
        //垂直方向模糊
        //Imgproc.blur(m1,m2, Size(1.0,15.0), Point(-1.0,-1.0),Core.BORDER_DEFAULT)
        imgResult.setMat(m2)
        m1.release()
        m2.release()
    }

    private fun gothBur(){
        val m1 = imgOperate.getMat()
        val m2 = Mat()
        Imgproc.GaussianBlur(m1,m2,Size(0.0,0.0),10.0)
        //Imgproc.GaussianBlur(m1,m2,Size(10.0,10.0),0.0)//不知道为什么这样不行
        imgResult.setMat(m2)
        m1.release()
        m2.release()
    }

    fun middleBlur(){
        reset()
        val m1 = imgOperate.getMat()
        val m2 = Mat()
        Imgproc.medianBlur(m1,m2,11)
        imgResult.setMat(m2)
        m1.release()
        m2.release()
    }

    fun minBlur(){
        reset()
        val m1 = imgOperate.getMat()
        val m2 = Mat()
        val kernal = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, Size(5.0,5.0))
        Imgproc.dilate(m1,m2,kernal)
        imgResult.setMat(m2)
        m1.release()
        m2.release()
        kernal.release()
    }

    fun maxBlur(){
        reset()
        val m1 = imgOperate.getMat()
        val m2 = Mat()
        val kernal = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, Size(5.0,5.0))
        Imgproc.erode(m1,m2,kernal)
        imgResult.setMat(m2)
        m1.release()
        m2.release()
        kernal.release()
    }

    fun bilateralBlur(){
        val m1 = imgOperate.getMat()
        val m2 = Mat()
        Imgproc.cvtColor(m1,m2,Imgproc.COLOR_BGRA2BGR)
        val m3 = Mat()
        Imgproc.bilateralFilter(m2,m3,0,150.0,15.0)
        imgResult.setMat(m3)
        m1.release()
        m2.release()
        m3.release()
    }

    fun shiftFilter(){
        val m1 = imgOperate.getMat()
        val m2 = Mat()
        // Only 8-bit, 3-channel images are supported in function
        Imgproc.cvtColor(m1,m2,Imgproc.COLOR_BGRA2BGR)
        var m3 = Mat()
        Imgproc.pyrMeanShiftFiltering(m2,m3,10.0,50.0)
        imgResult.setMat(m3)
        m1.release()
        m2.release()
        m2.release()
    }

    fun customFilter(){
        //定义模糊卷积
        val k = Mat(3,3,CvType.CV_32FC1);
        val data = floatArrayOf(1.0f/9.0f,1.0f/9.0f,1.0f/9.0f,1.0f/9.0f,1.0f/9.0f,1.0f/9.0f,1.0f/9.0f,1.0f/9.0f,1.0f/9.0f)  //均值模糊卷积
        //这个模糊卷积看起来就是模糊了一点点
        val data2 = floatArrayOf(0f,1.0f/8.0f,0f,1.0f/8.0f,0.5f,1.0f/8.0f,0f,1.0f/8.0f,0f)  //高斯模糊卷积
        //这个高斯模糊卷积看起来就是模糊了一点点,没什么区别
        val data3 = floatArrayOf(0f,-1f,0f,-1f,5f,-1f,0f,-1f,0f)  //锐化
        //这个锐化卷积看起来效果还是很明显的
        k.put(0,0,data3)


        //梯度卷积
        val mx = Mat(3,3,CvType.CV_32FC1)
        val my = Mat(3,3,CvType.CV_32FC1)
        //X方向梯度算子
        val robert_x = floatArrayOf(-1f,0f,0f,-1f)
        mx.put(0,0,robert_x)
        //y方向梯度算子
        val robert_y = floatArrayOf(0f,1f,-1f,0f)
        my.put(0,0,robert_y)
        //这本书上有问题，并没有使用这两个卷积
        val m1 = imgOperate.getMat()
        val m2 = Mat()
        Imgproc.filter2D(m1,m2,-10,k)
        imgResult.setMat(m2)
        k.release()
        m1.release()
        m2.release()

    }


    fun thresholdFilter(){

    }

    fun reset(){
        imgResult.setImageResource(R.drawable.test1)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.menu_delete->{
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent,"图片选择..."),0x0001)
            }
            R.id.menu_save->{
                Toast.makeText(this,"产生一张对比图", Toast.LENGTH_LONG).show()
                val m0 = imgOperate.getMat()
                val mat = Mat.zeros(m0.rows(),m0.cols(),m0.type())
                m0.release()
                Imgproc.circle(mat, Point(mat.width() / 2.0,mat.width() /2.0),50, Scalar(255.0,0.0,0.0),5,8,0)
                imgOperate.setMat(mat)
                mat.release()
            }
            R.id.menu_setting->{
                Toast.makeText(this,"You click menu_setting", Toast.LENGTH_LONG).show()
            }
            android.R.id.home->{
                drawer_layout_fixed.openDrawer(GravityCompat.START)
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0x0001){
            if (resultCode == Activity.RESULT_OK){
                if (data != null){
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,data!!.data)
                    imgOperate.setImageBitmap(bitmap)
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
