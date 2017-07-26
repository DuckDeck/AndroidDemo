package stan.androiddemo.project.novel

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.activity_novel_search.*
import org.litepal.crud.DataSupport
import stan.androiddemo.Model.ResultInfo
import stan.androiddemo.R
import stan.androiddemo.UI.Separate
import stan.androiddemo.project.novel.model.NovelInfo
import stan.androiddemo.project.novel.model.SectionInfo

class NovelSearchActivity : AppCompatActivity() {

    lateinit var mAdapter:BaseQuickAdapter<NovelInfo,BaseViewHolder>
    var arrNovels = ArrayList<NovelInfo>()
    var index = 0
    var key = ""
    lateinit var failView: View
    lateinit var loadingView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novel_search)
        mAdapter = object:BaseQuickAdapter<NovelInfo,BaseViewHolder>(R.layout.novel_list_item,arrNovels){
            override fun convert(helper: BaseViewHolder, item: NovelInfo) {
                Glide.with(this@NovelSearchActivity).load(item.img)
                        .dontAnimate().centerCrop()
                        .into(helper.getView<ImageView>(R.id.img_novel_cover))
                helper.setText(R.id.txt_novel_title,item.title)
                helper.setText(R.id.txt_novel_intro,item.intro)
                helper.setText(R.id.txt_novel_author,item.author)
                helper.setText(R.id.txt_novel_category,item.category)
                helper.setText(R.id.txt_novel_update_time,item.updateTime)
            }

        }
        recycler_novels.layoutManager = LinearLayoutManager(this)
        recycler_novels.addItemDecoration(Separate(this,Separate.VERTICAL_LIST))
        recycler_novels.adapter = mAdapter
        mAdapter.setEnableLoadMore(true)
        mAdapter.setOnLoadMoreListener({
            searchNovel(key)
        },recycler_novels)
        loadingView = View.inflate(this,R.layout.list_loading_hint,null)
        failView = View.inflate(this,R.layout.list_empty_hint,null)
        failView.setOnClickListener {
            mAdapter.emptyView = loadingView
            index = 0
            searchNovel(key)
        }

        txt_search_start.setOnClickListener {
            val key = txt_search_input.text.toString().trim()
            if (key.isEmpty()){
                Toast.makeText(this,"搜索条件不能为空",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            index = 0
            this.key = key
            searchNovel(key)
        }
        mAdapter.setOnItemClickListener { _, _, position ->
            val novel = arrNovels[position]
            val bundle = Bundle()
            bundle.putParcelable("novel",novel)
            val intent = Intent(this,SectionListActivity::class.java)
            intent.putExtra("novel",novel)
            startActivity(intent,bundle)
        }

        btn_check_bookmark.setOnClickListener {
          val intent = Intent(this,BookmarkActivity::class.java)
            startActivity(intent)
        }
    }

    fun searchNovel(key:String){
        mAdapter.emptyView = loadingView
        NovelInfo.search(key,index) { v:ResultInfo->
            runOnUiThread {
                if (v.code != 0)                 {
                    Toast.makeText(this,v.message,Toast.LENGTH_LONG).show()
                    if (index == 0){
                        mAdapter.emptyView = failView
                    }
                    else{
                        mAdapter.loadMoreFail()
                    }
                    return@runOnUiThread
                }
                if (index == 0){
                    arrNovels.clear()
                }
                val resultNovels = v.data!! as ArrayList<NovelInfo>
                if (resultNovels.size <= 0){
                    mAdapter.loadMoreEnd()
                }
                else{
                    mAdapter.loadMoreComplete()
                    arrNovels.addAll(resultNovels)
                    index ++
                    mAdapter.notifyDataSetChanged()
                }

            }
            return@search
        }

    }

}
