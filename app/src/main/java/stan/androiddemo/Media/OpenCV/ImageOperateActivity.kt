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

    fun  scaleAbsMat(){
        reset()
        val m1 = imgOperate.getMat()
        val m2 = Mat()
        Core.convertScaleAbs(m1,m2)
        imgResult.setMat(m2)
        m1.release()
        m2.release()

    }

    fun reset(){
        imgOperate.setImageResource(R.drawable.petal_header_bg)
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
