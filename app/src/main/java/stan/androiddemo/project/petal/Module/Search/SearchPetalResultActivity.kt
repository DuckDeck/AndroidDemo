package stan.androiddemo.project.petal.Module.Search

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_search_petal_result.*
import stan.androiddemo.PageAdapter
import stan.androiddemo.R
import stan.androiddemo.project.petal.Base.BasePetalActivity
import stan.androiddemo.project.petal.Config.Config
import stan.androiddemo.tool.Logger
import stan.androiddemo.tool.SPUtils

class SearchPetalResultActivity : BasePetalActivity() {


    lateinit var searchKey :String

    lateinit var mAdapter: PageAdapter
    override fun getTag(): String {
        return  this.toString()
    }

    companion object {
        fun launch(activity:Activity,key:String){
            val intent = Intent(activity,SearchPetalResultActivity::class.java)
            intent.putExtra("key",key)
            activity.startActivity(intent)
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_search_petal_result
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        searchKey = intent.getStringExtra("key")
        saveSearchHistory(searchKey)

        val titles = arrayListOf<String>(resources.getString(R.string.title_fragment_gather),
                resources.getString(R.string.title_fragment_board),
                resources.getString(R.string.title_fragment_user))
        val framgments = arrayListOf<Fragment>(SearchPetalResultPinsFragment.newInstance(searchKey),
                SearchPetalResultBoardFragment.newInstance(searchKey),
                SearchPetalResultPeopleFragment.newInstance(searchKey))
        mAdapter = PageAdapter(supportFragmentManager,framgments,titles )
        viewPager_search_petal_result.adapter = mAdapter
        tabLayout_search_petal_result.setupWithViewPager(viewPager_search_petal_result)
        title = searchKey
    }

    fun saveSearchHistory(key:String){
        val map = SPUtils.get(mContext,Config.HISTORYTEXT,HashSet<String>()) as HashSet<String>
        val searchedData = HashSet<String>(map)
        searchedData.add(key)
        val saveSuccess = SPUtils.putCommit(mContext,Config.HISTORYTEXT,searchedData)
        Logger.d("is save search success"+saveSuccess)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.petal_search_result,menu)
        return true
    }

    override fun isTranslucentStatusBar(): Boolean {
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){

        }
        return super.onOptionsItemSelected(item)
    }


}
