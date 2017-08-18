package stan.androiddemo.project.petal.Module.Search

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_search_petal_result.*
import stan.androiddemo.PageAdapter
import stan.androiddemo.R
import stan.androiddemo.project.petal.Base.BasePetalActivity
import stan.androiddemo.project.petal.Config.Config
import stan.androiddemo.project.petal.Event.OnBoardFragmentInteractionListener
import stan.androiddemo.project.petal.Event.OnPeopleFragmentInteraction
import stan.androiddemo.project.petal.Event.OnPinsFragmentInteractionListener
import stan.androiddemo.project.petal.Model.BoardPinsInfo
import stan.androiddemo.project.petal.Model.PinsMainInfo
import stan.androiddemo.project.petal.Module.BoardDetail.BoardDetailActivity
import stan.androiddemo.project.petal.Module.ImageDetail.PetalImageDetailActivity
import stan.androiddemo.project.petal.Module.UserInfo.PetalUserInfoActivity
import stan.androiddemo.tool.Logger
import stan.androiddemo.tool.SPUtils

class SearchPetalResultActivity : BasePetalActivity(), OnPinsFragmentInteractionListener, OnBoardFragmentInteractionListener<BoardPinsInfo>
, OnPeopleFragmentInteraction<SearchPeopleBean.UsersBean> {


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

    override fun onClickItemUser(bean: SearchPeopleBean.UsersBean, view: View) {
       PetalUserInfoActivity.launch(this@SearchPetalResultActivity,bean.user_id.toString(),bean.username!!)
    }

    override fun onClickBoardItemImage(bean: BoardPinsInfo, view: View) {
       BoardDetailActivity.launch(this@SearchPetalResultActivity,bean.board_id.toString(),bean.title!!)
    }

    override fun onClickPinsItemImage(bean: PinsMainInfo, view: View) {
        PetalImageDetailActivity.launch(this@SearchPetalResultActivity)
    }

    override fun onClickPinsItemText(bean: PinsMainInfo, view: View) {
        PetalImageDetailActivity.launch(this@SearchPetalResultActivity)
    }

}
