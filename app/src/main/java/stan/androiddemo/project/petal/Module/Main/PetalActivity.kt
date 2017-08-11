package stan.androiddemo.project.petal.Module.Main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.widget.Button
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.activity_petal.*
import kotlinx.android.synthetic.main.petal_swipe_frame.*
import rx.functions.Action1
import stan.androiddemo.R
import stan.androiddemo.project.petal.Base.BasePetalActivity
import stan.androiddemo.project.petal.Config.Config
import stan.androiddemo.project.petal.Event.OnFragmentRefreshListener
import stan.androiddemo.project.petal.Module.PetalList.PetalListFragment
import stan.androiddemo.tool.CompatUtils
import java.util.concurrent.TimeUnit


class PetalActivity : BasePetalActivity() {

    lateinit var fragmentManage: FragmentManager
    lateinit var types:Array<String>
    lateinit var titles:Array<String>
    override fun getTag(): String {return this.toString()}
    lateinit var fragment:PetalListFragment
    protected lateinit var mListenerRefresh:OnFragmentRefreshListener
    override fun getLayoutId(): Int {
        return R.layout.activity_petal
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        fragmentManage = supportFragmentManager
        getData()
        initDrawer(toolbar)

        initNavHeadView()

        initNavMenuView()

        selectFragment(0)
    }

    //取出各种需要用的全局变量
    fun getData(){
        types = resources.getStringArray(R.array.type_array)
        titles = resources.getStringArray(R.array.title_array)

    }

    //初始化DrawLayout
    fun initDrawer(tb: android.support.v7.widget.Toolbar){
        val toggle = ActionBarDrawerToggle(this,drawer_layout_petal,tb,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close)
        drawer_layout_petal.addDrawerListener(toggle)
        toggle.syncState()
        navigation_view_petal.setNavigationItemSelectedListener {

            return@setNavigationItemSelectedListener true
        }
    }
    //初始化HeadNav
    fun initNavHeadView(){
        val headView = navigation_view_petal.inflateHeaderView(R.layout.petal_nav_header)
        val btnAttention = headView.findViewById<Button>(R.id.btn_nav_attention)
        btnAttention.setCompoundDrawablesRelativeWithIntrinsicBounds(null,
                CompatUtils.getTintDrawable(mContext,R.drawable.ic_loyalty_black_24dp
                        ,resources.getColor(R.color.tint_list_pink)),null,null)
        val btnGather = headView.findViewById<Button>(R.id.btn_nav_gather)
        btnGather.setCompoundDrawablesRelativeWithIntrinsicBounds(null,
                CompatUtils.getTintDrawable(mContext,R.drawable.ic_camera_black_24dp
                        ,resources.getColor(R.color.tint_list_pink)),null,null)
        val btnMessage = headView.findViewById<Button>(R.id.btn_nav_message)
        btnMessage.setCompoundDrawablesRelativeWithIntrinsicBounds(null,
                CompatUtils.getTintDrawable(mContext,R.drawable.ic_message_black_24dp
                        ,resources.getColor(R.color.tint_list_pink)),null,null)
        val btnFriend = headView.findViewById<Button>(R.id.btn_nav_friends)
        btnFriend.setCompoundDrawablesRelativeWithIntrinsicBounds(null,
                CompatUtils.getTintDrawable(mContext,R.drawable.ic_people_black_24dp
                        ,resources.getColor(R.color.tint_list_pink)),null,null)

    }
    //初始化NavMenu
    fun initNavMenuView(){
        val menu = navigation_view_petal.menu
        val titleList = resources.getStringArray(R.array.title_array)
        val icons = arrayListOf(R.drawable.ic_toys_black_24dp,R.drawable.ic_camera_black_24dp,R.drawable.ic_camera_alt_black_24dp,R.drawable.ic_local_dining_black_24dp,R.drawable.ic_loyalty_black_24dp)
        for(i in 0 until titleList.size){
            menu.add(R.id.menu_group_type,i, Menu.NONE,titleList[i]).setIcon(icons[i]).setCheckable(true)
        }
        menu.getItem(0).isChecked = true
    }

    fun selectFragment(position:Int){
        val transaction = fragmentManage.beginTransaction()
        val type = types[position]
        val tt = titles[position]
        fragment = PetalListFragment.createListFragment(type,tt)
        mListenerRefresh = fragment
        transaction.replace(R.id.swipe_petal_refresh,fragment)
        transaction.commit()
        title = tt
    }

    override fun initResAndListener() {
        float_button_search.setImageResource(R.drawable.ic_search_black_24dp)
        RxView.clicks(float_button_search).throttleFirst(Config.throttDuration.toLong(),TimeUnit.MILLISECONDS)
                .subscribe(Action1 {
                    println("11111111111")
                })

        swipe_petal_refresh.setOnRefreshListener {
            mListenerRefresh.refresh()
        }
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
