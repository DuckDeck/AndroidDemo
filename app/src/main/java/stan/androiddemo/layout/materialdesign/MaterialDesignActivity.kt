package stan.androiddemo.layout.materialdesign

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.activity_material_design.*
import stan.androiddemo.R
import java.util.*
import kotlin.concurrent.thread

class MaterialDesignActivity : AppCompatActivity() {
    val FRUIT_NAME = "fruit_name"
    val FRUIT_IMAGE_ID = "fruit_image_id"

    lateinit var mAdapter:BaseQuickAdapter<Fruit,BaseViewHolder>
    var arrFruits = ArrayList<Fruit>()
    var fruits =  ArrayList<Fruit>()
    val random = Random()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_material_design)
        title = ""
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher)
        }
        navigation_view.setCheckedItem(R.id.nav_call)
        navigation_view.setNavigationItemSelectedListener {
            println(it)
            drawer_layout_fixed.closeDrawers()
            return@setNavigationItemSelectedListener true
        }

        button_float.setOnClickListener {

            val f = fruits[random.nextInt(fruits.size)]
            arrFruits.add(f)
            mAdapter.notifyDataSetChanged()

            Snackbar.make(it,"Data Deleted",Snackbar.LENGTH_SHORT).setAction("Undo",View.OnClickListener {

                arrFruits.removeAt(arrFruits.size - 1)
                mAdapter.notifyDataSetChanged()
                Toast.makeText(this,"Data restored",Toast.LENGTH_LONG).show()
            }).show()
        }

        var fruit = Fruit()
        fruit.name = "banana"
        fruit.imageId = R.mipmap.banana
        fruits.add(fruit)
        fruit = Fruit()
        fruit.name = "apple"
        fruit.imageId = R.mipmap.apple
        fruits.add(fruit)
        fruit = Fruit()
        fruit.name = "orange"
        fruit.imageId = R.mipmap.orange
        fruits.add(fruit)
        fruit = Fruit()
        fruit.name = "papaya"
        fruit.imageId = R.mipmap.papaya
        fruits.add(fruit)
        fruit = Fruit()
        fruit.name = "pineapple"
        fruit.imageId = R.mipmap.pineapple
        fruits.add(fruit)
        fruit = Fruit()
        fruit.name = "watermelon"
        fruit.imageId = R.mipmap.watermelon
        fruits.add(fruit)

        for (i in 0 until 50){
            val index = random.nextInt(fruits.size)
            arrFruits.add(fruits[index])
        }

        mAdapter = object:BaseQuickAdapter<Fruit,BaseViewHolder>(R.layout.fruit_item,arrFruits){
            override fun convert(helper: BaseViewHolder, item: Fruit) {
                helper.setText(R.id.txt_fruit_name,item.name)
                Glide.with(this@MaterialDesignActivity).load(item.imageId)
                        .into(helper.getView(R.id.img_fruit))
            }
        }

        recycler_view.layoutManager = GridLayoutManager(this,2)
        recycler_view.adapter = mAdapter

        mAdapter.setOnItemClickListener { adapter, view, position ->
            val fruit = arrFruits[position]
            val intent = Intent(this,FruitInfoActivity::class.java)
            intent.putExtra(FRUIT_NAME,fruit.name)
            intent.putExtra(FRUIT_IMAGE_ID,fruit.imageId)
            startActivity(intent)
        }

        swipe_refresh_fruit.setColorSchemeResources(R.color.colorPrimary)
        swipe_refresh_fruit.setOnRefreshListener {
            refreshFruits()
        }


    }

    //图标不能用太大的，显示不出来，还会变形
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.menu_delete->{
                Toast.makeText(this,"You click delete",Toast.LENGTH_LONG).show()
            }
            R.id.menu_save->{
                Toast.makeText(this,"You click menu_save",Toast.LENGTH_LONG).show()
            }
            R.id.menu_setting->{
                Toast.makeText(this,"You click menu_setting",Toast.LENGTH_LONG).show()
            }
            android.R.id.home->{
                drawer_layout_fixed.openDrawer(GravityCompat.START)
            }
        }
        return true
    }

    fun refreshFruits(){
        thread {
            try {
                Thread.sleep(2000)
            }
            catch (e:InterruptedException){
                e.printStackTrace()
            }
            runOnUiThread {
                arrFruits.clear()
                for (i in 0 until 30){
                    val index = random.nextInt(fruits.size)
                    arrFruits.add(fruits[index])
                }
                mAdapter.notifyDataSetChanged()
                swipe_refresh_fruit.isRefreshing = false
            }
        }
    }
}

class Fruit{
    var name = ""
    var imageId = 0

}