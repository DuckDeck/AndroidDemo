package stan.androiddemo.project.petal.Module.Follow

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.View
import kotlinx.android.synthetic.main.activity_petal_follow.*
import stan.androiddemo.PageAdapter
import stan.androiddemo.R
import stan.androiddemo.project.petal.Base.BasePetalActivity
import stan.androiddemo.project.petal.Event.OnBoardFragmentInteractionListener
import stan.androiddemo.project.petal.Event.OnPinsFragmentInteractionListener
import stan.androiddemo.project.petal.Model.BoardPinsInfo
import stan.androiddemo.project.petal.Model.PinsMainInfo
import stan.androiddemo.project.petal.Module.BoardDetail.BoardDetailActivity
import stan.androiddemo.project.petal.Module.ImageDetail.PetalImageDetailActivity

class PetalFollowActivity : BasePetalActivity(),OnBoardFragmentInteractionListener<BoardPinsInfo>,OnPinsFragmentInteractionListener {


    lateinit var mAdapter: PageAdapter

    companion object {
        fun launch(activity:Activity,flag:Int){
            val intent = Intent(activity,PetalFollowActivity::class.java)
            intent.flags = flag
            activity.startActivity(intent)
        }
        fun launch(activity:Activity){
            val intent = Intent(activity,PetalFollowActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun getTag(): String {
        return this.toString()
    }


    override fun isTranslucentStatusBar(): Boolean {
        return false
    }

    override fun getLayoutId(): Int {
       return R.layout.activity_petal_follow
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        val tt =  resources.getStringArray(R.array.title_attention)
        val titles = ArrayList<String>()
        titles.addAll(tt)
        val fragments = arrayListOf(PetalFollowBoardFragment.newInstance(),PetalFollowPinsFragment.newInstance())

        mAdapter = PageAdapter(supportFragmentManager,fragments,titles)
        viewPager.adapter = mAdapter
        tablayout_attention.setupWithViewPager(viewPager)
        tablayout_attention.setSelectedTabIndicatorColor(Color.WHITE)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.petal_menu_my_follow,menu)
        return true
    }

    override fun onClickBoardItemImage(bean: BoardPinsInfo, view: View) {
        val boardId = bean.board_id
        BoardDetailActivity.launch(this,boardId.toString(),bean.title!!)
    }

    override fun onClickPinsItemImage(bean: PinsMainInfo, view: View) {
        PetalImageDetailActivity.launch(this,PetalImageDetailActivity.ACTION_ATTENTION)
    }

    override fun onClickPinsItemText(bean: PinsMainInfo, view: View) {
        PetalImageDetailActivity.launch(this,PetalImageDetailActivity.ACTION_ATTENTION)
    }


}
