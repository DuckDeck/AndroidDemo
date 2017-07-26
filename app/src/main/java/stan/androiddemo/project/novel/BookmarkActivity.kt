package stan.androiddemo.project.novel

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gavin.com.library.StickyDecoration
import stan.androiddemo.R

import kotlinx.android.synthetic.main.activity_bookmark.*
import kotlinx.android.synthetic.main.activity_novel_search.view.*
import org.litepal.crud.DataSupport
import stan.androiddemo.project.novel.model.NovelInfo
import stan.androiddemo.project.novel.model.SectionInfo

class BookmarkActivity : AppCompatActivity() {

    lateinit var arrBookmark:ArrayList<NovelInfo>
    lateinit var arrSection:ArrayList<SectionInfo>
    lateinit var mAdapter:BaseQuickAdapter<SectionInfo,BaseViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmark)

        arrBookmark = ArrayList(DataSupport.findAll(NovelInfo::class.java))
        arrSection = ArrayList(DataSupport.findAll(SectionInfo::class.java))
        mAdapter = object:BaseQuickAdapter<SectionInfo,BaseViewHolder>(android.R.layout.simple_expandable_list_item_1,arrSection){
            override fun convert(helper: BaseViewHolder, item: SectionInfo) {
                helper.setText(android.R.id.text1,item.title)
            }
        }

        val dec = StickyDecoration.Builder.init {
            println(it)
            return@init "123"
        }.setGroupBackground(Color.GREEN).setTextSideMargin(20).build()

        recycler_bookmark.layoutManager = LinearLayoutManager(this)
        recycler_bookmark.addItemDecoration(dec)
        recycler_bookmark.adapter = mAdapter
        mAdapter.notifyDataSetChanged()

        btn_delete_bookmark.setOnClickListener {
            DataSupport.deleteAll(NovelInfo::class.java)
            DataSupport.deleteAll(SectionInfo::class.java)
            arrBookmark.clear()
            arrSection.clear()
            mAdapter.notifyDataSetChanged()
        }
    }

}
