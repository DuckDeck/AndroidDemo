package stan.androiddemo.Media.OpenCV

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_string_image.*
import org.opencv.android.OpenCVLoader
import stan.androiddemo.R

class StringImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_string_image)
        setSupportActionBar(toolbar)
        loadOpenCV()
        title = ""




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
