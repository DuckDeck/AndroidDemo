package stan.androiddemo.project.petal.Module.Main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.activity_petal.*
import rx.functions.Action1
import stan.androiddemo.R
import stan.androiddemo.project.petal.Base.BaseActivity
import stan.androiddemo.project.petal.Config.Config
import java.util.concurrent.TimeUnit


class PetalActivity : BaseActivity() {

    lateinit var fragmentManage: FragmentManager
    lateinit var types:Array<String>
    lateinit var titles:Array<String>
    override fun getTag(): String {return this.toString()}

    override fun getLayoutId(): Int {
        return R.layout.activity_petal
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        title = "首页"
        fragmentManage = supportFragmentManager
        initDrawer(toolbar)
    }

    //取出各种需要用的全局变量
    fun getData(){
        types = resources.getStringArray(R.array.type_array)
        titles = resources.getStringArray(R.array.type_array)

    }

    fun initDrawer(tb: android.support.v7.widget.Toolbar){
        val toggle = ActionBarDrawerToggle(this,drawer_layout_petal,tb,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close)
        drawer_layout_petal.addDrawerListener(toggle)
        toggle.syncState()

        navigation_view_petal.setNavigationItemSelectedListener {
            return@setNavigationItemSelectedListener true
        }
    }

    override fun initResAndListener() {
        float_button_search.setImageResource(R.drawable.ic_search_black_24dp)
        RxView.clicks(float_button_search).throttleFirst(Config.throttDuration.toLong(),TimeUnit.MILLISECONDS)
                .subscribe(Action1 {
                    println("11111111111")
                })
    }

    override fun onBackPressed() {
        if (drawer_layout_petal.isDrawerOpen(GravityCompat.START)){
            drawer_layout_petal.closeDrawers()
        }
        else{
            super.onBackPressed()
        }

    }

    companion object {
        fun launch(activity:Activity){
            val intent = Intent(activity,PetalActivity::class.java)
            activity.startActivity(intent)
        }
        fun launch(activity:Activity,flag:Int){
            val intent = Intent(activity,PetalActivity::class.java)
            intent.flags = flag
            activity.startActivity(intent)
        }
    }




}
