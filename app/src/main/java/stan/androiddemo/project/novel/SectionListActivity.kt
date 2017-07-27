package stan.androiddemo.project.novel

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.activity_section_list.*
import stan.androiddemo.Model.ResultInfo
import stan.androiddemo.R
import stan.androiddemo.project.novel.model.NovelInfo
import stan.androiddemo.project.novel.model.SectionInfo

class SectionListActivity : AppCompatActivity() {

    lateinit var novelInfo:NovelInfo
    var arrSections = ArrayList<SectionInfo>()
    lateinit var mAdapter:BaseQuickAdapter<SectionInfo, BaseViewHolder>
    lateinit var failView:View
    lateinit var loadingView:View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_section_list)

        novelInfo = intent.getParcelableExtra("novel")

        mAdapter = object: BaseQuickAdapter<SectionInfo,BaseViewHolder>(android.R.layout.simple_list_item_1,arrSections){
            override fun convert(helper: BaseViewHolder, item: SectionInfo) {
                helper.setText(android.R.id.text1,item.title)
            }
        }
        recyclerView_sections.layoutManager = LinearLayoutManager(this)
        recyclerView_sections.adapter = mAdapter
        loadingView = View.inflate(this,R.layout.list_loading_hint,null)
        failView = View.inflate(this,R.layout.list_empty_hint,null)
        failView.setOnClickListener {
            mAdapter.emptyView = loadingView
            getSections()
        }
        mAdapter.emptyView = loadingView

        mAdapter.setOnItemClickListener { _, _, position ->
            val intent= Intent(this,NovelActivity::class.java)
            intent.putParcelableArrayListExtra("sections",arrSections)
            intent.putExtra("novel",novelInfo)
            intent.putExtra("currentSection",arrSections[position])
            startActivity(intent)
        }

        getSections()
    }

    fun getSections(){
        NovelInfo.getSections(novelInfo.url,{ v: ResultInfo ->
            runOnUiThread {
                if (v.code != 0) {
                    Toast.makeText(this,v.message, Toast.LENGTH_LONG).show()
                    mAdapter.emptyView = failView
                    return@runOnUiThread
                }
                arrSections.addAll((v.data as ArrayList<SectionInfo>).map {
                    it.sectionUrl =  novelInfo.url + it.sectionUrl
                    it.novelId = novelInfo.novelId
                    it
                })
               mAdapter.notifyDataSetChanged()

            }
            return@getSections
        })
    }
}
