package stan.androiddemo.project.novel

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gavin.com.library.StickyDecoration
import stan.androiddemo.R

import kotlinx.android.synthetic.main.activity_bookmark.*
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
            val section = arrSection[it]
            val novel = arrBookmark.indexOfFirst {
                it.novelId == section.novelId
            }
            if (novel >= 0 && novel < arrBookmark.size){
                return@init arrBookmark[novel].title
            }
            return@init null
        }.setGroupBackground(Color.parseColor("#555555")).setTextSideMargin(20).build()

        recycler_bookmark.layoutManager = LinearLayoutManager(this)
        recycler_bookmark.addItemDecoration(dec)
        recycler_bookmark.adapter = mAdapter
        mAdapter.notifyDataSetChanged()

        mAdapter.setOnItemClickListener { adapter, view, position ->
            val intent= Intent(this,NovelActivity::class.java)
            intent.putParcelableArrayListExtra("sections",ArrayList<SectionInfo>())
            val section = arrSection[position]
            val novelIndex = arrBookmark.indexOfFirst {
                it.novelId == section.novelId
            }
            intent.putExtra("novel",arrBookmark[novelIndex])
            intent.putExtra("currentSection",section)
            startActivity(intent)
        }

        btn_delete_bookmark.setOnClickListener {
            if (arrSection.size <= 0){
                return@setOnClickListener
            }
            MaterialDialog.Builder(this).title("删除书签").content("你确定要删除全部书签吗？").positiveText("确定")
                    .negativeText("取消").onPositive { dialog, which ->
                DataSupport.deleteAll(NovelInfo::class.java)
                DataSupport.deleteAll(SectionInfo::class.java)
                arrBookmark.clear()
                arrSection.clear()
                mAdapter.notifyDataSetChanged()
                Toast.makeText(this,"删除全部书签成功", Toast.LENGTH_LONG).show()
            }.show()
        }
    }

}
