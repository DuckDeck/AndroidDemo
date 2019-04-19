package stan.androiddemo.project.Mito

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_mito.*
import stan.androiddemo.PageAdapter
import stan.androiddemo.R

class DynamicMitoActivity : AppCompatActivity() {

    lateinit var mAdapter: PageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dynamic_mito)

        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val titles = arrayListOf("全部",	"娱乐明星","网络红人","歌曲舞蹈","影视大全","动漫卡通","游戏天地","动物萌宠","风景名胜","天生尤物","其他视频")
        val fragments = arrayListOf<DynamicMitoFragment>()
        for (i in 0 until titles.size){
            val fra = DynamicMitoFragment.createFragment()
            val bundle = Bundle()
            bundle.putString("cat",titles[i])
            fra.arguments = bundle
            fragments.add(fra)
        }
        mAdapter = PageAdapter(supportFragmentManager,fragments,titles)
        viewPager.adapter = mAdapter
        tabLayout.setupWithViewPager(viewPager)



    }
}
