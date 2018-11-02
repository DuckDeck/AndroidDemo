package stan.androiddemo.Media.OpenCV

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_image_operate.*
import org.opencv.android.OpenCVLoader
import org.opencv.core.Core
import org.opencv.core.Mat


import stan.androiddemo.R
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

            }
            return@setNavigationItemSelectedListener true
        }


    }


    fun reverseMat(){
        val m1 = imgOperate.getMat()
        val m2 = Mat()
        Core.bitwise_not(m1,m2)
        imgOperate.setMat(m2)
        m1.release()
        m2.release()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.menu_delete->{
                Toast.makeText(this,"You click delete", Toast.LENGTH_LONG).show()
            }
            R.id.menu_save->{
                Toast.makeText(this,"You click menu_save", Toast.LENGTH_LONG).show()
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
