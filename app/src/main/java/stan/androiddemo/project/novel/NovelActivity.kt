package stan.androiddemo.project.novel

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Parcel
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.view.View
import android.widget.Toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.activity_novel.*
import kotlinx.android.synthetic.main.novel_list_item.*
import okhttp3.internal.cache.DiskLruCache
import org.litepal.crud.DataSupport
import stan.androiddemo.Model.ResultInfo
import stan.androiddemo.R
import stan.androiddemo.project.novel.model.NovelInfo
import stan.androiddemo.project.novel.model.SectionInfo
import java.io.ObjectOutputStream
import java.lang.Exception

class NovelActivity : AppCompatActivity() {

    lateinit var novelInfo:NovelInfo
    lateinit var arrAllSections:ArrayList<SectionInfo>
    lateinit var currentSection:SectionInfo
    lateinit var currentDisplaySection:SectionInfo
    var index = 0
    var arrNovelSection = ArrayList<SectionInfo>()
    lateinit var mAdapter: BaseQuickAdapter<SectionInfo, BaseViewHolder>
    lateinit var failView: View
    lateinit var loadingView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novel)
        novelInfo = intent.getParcelableExtra("novel")
        arrAllSections = intent.getParcelableArrayListExtra("sections")
        currentSection = intent.getParcelableExtra("currentSection")

        mAdapter = object: BaseQuickAdapter<SectionInfo,BaseViewHolder>(android.R.layout.simple_list_item_1,arrNovelSection){
            override fun convert(helper: BaseViewHolder, item: SectionInfo) {
                currentDisplaySection = item
                txt_section_title.text = item.title //用是可以用 不怎么准确
                helper.setText(android.R.id.text1,Html.fromHtml(item.content))
            }
        }
        recycler_novel_section.layoutManager = LinearLayoutManager(this)
        recycler_novel_section.adapter = mAdapter
        mAdapter.setEnableLoadMore(true)
        mAdapter.setOnLoadMoreListener({
            getNovelSection()
        },recycler_novel_section)
        loadingView = View.inflate(this,R.layout.list_loading_hint,null)
        failView = View.inflate(this,R.layout.list_empty_hint,null)
        failView.setOnClickListener {
            mAdapter.emptyView = loadingView
            index = 0
            getNovelSection()
        }
        mAdapter.emptyView = loadingView
        if (arrAllSections.size <= 0){
            getSections()
        }
        else{
            index = arrAllSections.indexOfFirst {
                it.sectionUrl == currentSection.sectionUrl
            }
            getNovelSection()
        }


        btn_make_bookmark.setOnClickListener {
            if (arrNovelSection.size <= 0){
                Toast.makeText(this,"请等待加载完再添加书签",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val bookmark = DataSupport.where("url = ?",novelInfo.url).find(novelInfo::class.java)
            if (bookmark.size <= 0){
                novelInfo.save()
                currentDisplaySection.save()
                Toast.makeText(this,"添加书签" + currentDisplaySection.title + "成功",Toast.LENGTH_LONG).show()

                val b = DataSupport.where("url = ?",novelInfo.url).find(novelInfo::class.java)
                println(b)

                return@setOnClickListener
            }

            val sectionBookmark = DataSupport.where("sectionUrl = ?",currentDisplaySection.sectionUrl)
                    .find(SectionInfo::class.java)

            if (sectionBookmark.size <= 0){
                Toast.makeText(this,"添加书签" + currentDisplaySection.title + "成功",Toast.LENGTH_LONG).show()
                currentDisplaySection.save()
            }
            else{
                Toast.makeText(this,"你已经添加这个书签了",Toast.LENGTH_LONG).show()
            }


        }

    }

    fun getSections(){
        NovelInfo.getSections(novelInfo.url,{ v: ResultInfo ->
            runOnUiThread {
                if (v.code != 0) {
                    Toast.makeText(this,v.message, Toast.LENGTH_LONG).show()
                    mAdapter.emptyView = failView
                    return@runOnUiThread
                }
                arrAllSections.addAll((v.data as ArrayList<SectionInfo>).map {
                    it.sectionUrl =  novelInfo.url + it.sectionUrl
                    it.novelId = novelInfo.novelId
                    it
                })
                index = arrAllSections.indexOfFirst {
                    it.sectionUrl == currentSection.sectionUrl
                }
                getNovelSection()

            }
            return@getSections
        })
    }
    fun getNovelSection(){
        SectionInfo.getNovelSection(currentSection.sectionUrl,{ v: ResultInfo ->
            runOnUiThread {
                if (v.code != 0) {
                    if (arrNovelSection.size == 0){
                        mAdapter.emptyView = failView
                    }
                    else{
                        mAdapter.loadMoreFail()
                    }
                    Toast.makeText(this,v.message, Toast.LENGTH_LONG).show()
                    return@runOnUiThread
                }
                mAdapter.loadMoreComplete()
                var section =  SectionInfo()
                section.content = v.data as String
                section.novelId = novelInfo.novelId
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
