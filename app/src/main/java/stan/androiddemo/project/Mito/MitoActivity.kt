package stan.androiddemo.project.Mito

import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_mito.*
import stan.androiddemo.PageAdapter
import stan.androiddemo.R

class MitoActivity : AppCompatActivity() {

    lateinit var mAdapter:PageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mito)
        title = ""
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher)
        }
       // 全部	美女	性感	明星	风光	卡通	创意	汽车	游戏	建筑	影视	植物	动物	节庆	可爱	静物	体育	日历	唯美	其它	系统	动漫	非主流	小清新
        val titles = arrayListOf("全部",	"美女","	性感","明星","风光","卡通","创意","汽车","游戏","建筑","影视","植物","动物",
                "节庆","可爱","静物","体育","日历","唯美","其它","系统","动漫","非主流","小清新")
        val fragments = arrayListOf<ImageFragment>()
        for (i in 0 until titles.size){
            val fra = ImageFragment.createFragment()
            val bundle = Bundle()
            bundle.putInt("cat",i)
            fra.arguments = bundle
            fragments.add(fra)
        }
        mAdapter = PageAdapter(supportFragmentManager,fragments,titles)
        viewPager.adapter = mAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            android.R.id.home->{
                drawer_layout_fixed.openDrawer(GravityCompat.START)
            }
        }
        return true

    }
}
