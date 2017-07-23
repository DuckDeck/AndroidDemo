package stan.androiddemo.project.novel

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.widget.Toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.activity_novel.*
import stan.androiddemo.Model.ResultInfo
import stan.androiddemo.R
import stan.androiddemo.project.novel.model.NovelInfo
import stan.androiddemo.project.novel.model.SectionInfo

class NovelActivity : AppCompatActivity() {

    lateinit var novelInfo:NovelInfo
    lateinit var arrAllSections:ArrayList<SectionInfo>
    lateinit var currentSection:SectionInfo
    var index = 0
    var arrNovelSection = ArrayList<SectionInfo>()
    lateinit var mAdapter: BaseQuickAdapter<SectionInfo, BaseViewHolder>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novel)
        novelInfo = intent.getParcelableExtra("novel")
        arrAllSections = intent.getParcelableArrayListExtra("sections")
        currentSection = intent.getParcelableExtra("currentSection")
        index = arrAllSections.indexOfFirst {
            it.sectionUrl == currentSection.sectionUrl
        }
        mAdapter = object: BaseQuickAdapter<SectionInfo,BaseViewHolder>(android.R.layout.simple_list_item_1,arrNovelSection){
            override fun convert(helper: BaseViewHolder, item: SectionInfo) {
                helper.setText(android.R.id.text1,Html.fromHtml(item.content))
            }
        }
        recycler_novel_section.layoutManager = LinearLayoutManager(this)
        recycler_novel_section.adapter = mAdapter
        mAdapter.setEnableLoadMore(true)
        mAdapter.setOnLoadMoreListener({
            getNovelSection()
        },recycler_novel_section)
        getNovelSection()

    }


    fun getNovelSection(){
        SectionInfo.getNovelSection(currentSection.sectionUrl,{ v: ResultInfo ->
            runOnUiThread {
                if (v.code != 0) {
                    mAdapter.loadMoreFail()
                    Toast.makeText(this,v.message, Toast.LENGTH_LONG).show()
                    return@runOnUiThread
                }
                mAdapter.loadMoreComplete()
                var section =  SectionInfo()
                section.content = v.data as String
                section.novelId = novelInfo.id
                section.sectionUrl = currentSection.sectionUrl
                section.title = currentSection.title
                arrNovelSection.add(section)
                index ++
                currentSection = arrAllSections[index]
                mAdapter.notifyDataSetChanged()

            }
            return@getNovelSection
        })
    }


}
