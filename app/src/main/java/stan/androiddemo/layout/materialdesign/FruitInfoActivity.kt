package stan.androiddemo.layout.materialdesign

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_fruit_info.*
import stan.androiddemo.R

class FruitInfoActivity : AppCompatActivity() {

    val FRUIT_NAME = "fruit_name"
    val FRUIT_IMAGE_ID = "fruit_image_id"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fruit_info)
        val fruitName = intent.getStringExtra(FRUIT_NAME)
        val fruitImageId = intent.getIntExtra(FRUIT_IMAGE_ID,0)

        setSupportActionBar(toolbar)
//        val actionBar = supportActionBar
//        if (actionBar != null){
//            actionBar.setDisplayHomeAsUpEnabled(true)
        //}
        collapsing_toolbarLayout.title = fruitName
        Glide.with(this).load(fruitImageId).dontAnimate().into(img_fruit)
        txt_fruit_content.text = createFruitContent(fruitName)

    }

    fun createFruitContent(fruitName:String):String{
        val contnet = StringBuilder()
        for (i in 0 until 1000){
            contnet.append(fruitName)
        }
        return contnet.toString()
    }

    //这里就是调用的返回铵键的方法，也可用preaabock的方法返回
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            android.R.id.home->{
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
