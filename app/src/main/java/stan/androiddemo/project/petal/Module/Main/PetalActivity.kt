package stan.androiddemo.project.petal.Module.Main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.activity_petal.*
import stan.androiddemo.R
import stan.androiddemo.project.petal.Base.BasePetalActivity
import stan.androiddemo.project.petal.Config.Config
import stan.androiddemo.project.petal.Event.OnPinsFragmentInteractionListener
import stan.androiddemo.project.petal.Model.PinsMainInfo
import stan.androiddemo.project.petal.Module.Follow.PetalFollowActivity
import stan.androiddemo.project.petal.Module.ImageDetail.PetalImageDetailActivity
import stan.androiddemo.project.petal.Module.Login.PetalLoginActivity
import stan.androiddemo.project.petal.Module.PetalList.PetalListFragment
import stan.androiddemo.project.petal.Module.Search.SearchPetalActivity
import stan.androiddemo.project.petal.Module.UserInfo.PetalUserInfoActivity
import stan.androiddemo.tool.CompatUtils
import stan.androiddemo.tool.ImageLoad.ImageLoadBuilder
import stan.androiddemo.tool.Logger
import stan.androiddemo.tool.SPUtils
import java.util.concurrent.TimeUnit


class PetalActivity : BasePetalActivity(),OnPinsFragmentInteractionListener, SharedPreferences.OnSharedPreferenceChangeListener {


    lateinit var fragmentManage: FragmentManager
    lateinit var types:Array<String>
    lateinit var titles:Array<String>

    private var mUserName = ""
    private var mUserId = ""

    lateinit var fragment:PetalListFragment
    lateinit var imgNavHead:SimpleDraweeView
    lateinit var txtNavUsername:TextView
    lateinit var txtNavUserEmail:TextView
    override fun getTag(): String {return this.toString()}

    override fun getLayoutId(): Int {
        return R.layout.activity_petal
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        fragmentManage = supportFragmentManager
        getData()

        getSharedPreferences("share_data", Context.MODE_PRIVATE).registerOnSharedPreferenceChangeListener(this)

        initDrawer(toolbar)

        initNavHeadView()

        initNavMenuView()

        selectFragment(0)
    }

    override fun onResume() {
        super.onResume()
        setNavUserInfo()
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
            if (it.groupId == R.id.menu_group_type){
                fragment.mKey = types[it.itemId]
                fragment.setRefresh()
                title = titles[it.itemId]
            }
            else{
                when(it.itemId){
                    R.id.nav_petal_setting->{
                        TODO("GOTO seting")
                    }
                    R.id.nav_petal_quit->{
                        SPUtils.clear(mContext)
                        finish()
                    }
                }
            }
            drawer_layout_petal.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }
    }
    //初始化HeadNav
    fun initNavHeadView(){
        val headView = navigation_view_petal.inflateHeaderView(R.layout.petal_nav_header)
        imgNavHead = headView.findViewById(R.id.img_petal_my_header)
        imgNavHead.setOnClickListener {
            if (isLogin){
                PetalUserInfoActivity.launch(this@PetalActivity,mUserId,mUserName)
            }
            else{
                PetalLoginActivity.launch(this@PetalActivity)
            }
        }
        txtNavUsername = headView.findViewById(R.id.txt_nav_username)
        txtNavUsername.setOnClickListener {
            Logger.e("click txtNavUsername")
        }
        txtNavUserEmail = headView.findViewById(R.id.txt_nav_email)
        val btnAttention = headView.findViewById<Button>(R.id.btn_nav_attention)
        btnAttention.setCompoundDrawablesRelativeWithIntrinsicBounds(null,
                CompatUtils.getTintDrawable(mContext,R.drawable.ic_loyalty_black_24dp
                        ,resources.getColor(R.color.tint_list_pink)),null,null)
        btnAttention.setOnClickListener {
            if (!isLogin){
                PetalLoginActivity.launch(this@PetalActivity)
            }
            else{
                PetalFollowActivity.launch(this@PetalActivity)
            }
        }
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

    fun setNavUserInfo(){
        val drawable = CompatUtils.getTintDrawable(mContext,R.drawable.ic_account_circle_gray_48dp,Color.GRAY)
        if (isLogin){
            var key = SPUtils.get(mContext,Config.USERHEADKEY,"") as String
            if (!key.isNullOrEmpty()){
               key = resources.getString(R.string.urlImageRoot) + key
                ImageLoadBuilder.Start(mContext,imgNavHead,key).setPlaceHolderImage(drawable).setIsCircle(true,true).build()
            }
            else{
                Logger.d("User Head Key is empty")
            }
            if (!mUserName.isNullOrEmpty()){
                txtNavUsername.text = mUserName
            }
            val email = SPUtils.get(mContext,Config.USEREMAIL,"") as String
            if (!email.isNullOrEmpty()){
                txtNavUserEmail.text = email
            }
        }
        else{
            ImageLoadBuilder.Start(mContext,imgNavHead,"").setPlaceHolderImage(drawable).setIsCircle(true,true).build()
        }
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
        transaction.replace(R.id.frame_layout_petal_with_refresh,fragment)
        transaction.commit()
        title = tt
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.petal_search_result,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        //do something
        return super.onOptionsItemSelected(item)
    }

    override fun initResAndListener() {
        float_button_search.setImageResource(R.drawable.ic_search_black_24dp)
        RxView.clicks(float_button_search).throttleFirst(Config.throttDuration.toLong(),TimeUnit.MILLISECONDS)
                .subscribe({
                    SearchPetalActivity.launch(this@PetalActivity)
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

    override fun onSharedPreferenceChanged(p0: SharedPreferences, p1: String) {
        if (Config.ISLOGIN == p1){
            isLogin = p0.getBoolean(Config.ISLOGIN, false)
        }
    }



    override fun onClickPinsItemImage(bean: PinsMainInfo, view: View) {
        PetalImageDetailActivity.launch(this@PetalActivity,PetalImageDetailActivity.ACTION_MAIN)
    }

    override fun onClickPinsItemText(bean: PinsMainInfo, view: View) {
        PetalImageDetailActivity.launch(this@PetalActivity,PetalImageDetailActivity.ACTION_MAIN)
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
