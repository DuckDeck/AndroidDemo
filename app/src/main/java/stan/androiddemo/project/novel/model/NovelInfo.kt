package stan.androiddemo.project.novel.model

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import okhttp3.Call
import okhttp3.Response
import org.jsoup.Jsoup
import org.litepal.crud.DataSupport
import stan.androiddemo.Model.ResultInfo
import stan.androiddemo.errcode_html_resolve_error
import stan.androiddemo.errcode_netword_error
import stan.androiddemo.tool.HttpTool
import java.io.IOException
import java.nio.charset.Charset
import javax.xml.transform.Source

@SuppressLint("ParcelCreator")
/**
 * Created by hugfo on 2017/7/20.
 */
class NovelInfo() :Parcelable,DataSupport(){
    var id = 0
    var url = ""
    var title = ""
    var img = ""
    var intro = ""
    var author = ""
    var category = ""
    var updateTime = ""
    var novelId = 0
    //litepal不能保存里面的array对象，再次取出就丢失了，这是个问题
    //因为litepal是用sqlite， 一个对应一张表，所以不能保存对象里面的array数组
    // 有一个issue点，就是使用litapal保存数据时会自动修改
    var arrBookmark = ArrayList<SectionInfo>()




    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        url = parcel.readString()
        title = parcel.readString()
        img = parcel.readString()
        intro = parcel.readString()
        author = parcel.readString()
        category = parcel.readString()
        updateTime = parcel.readString()
        novelId = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(url)
        parcel.writeString(title)
        parcel.writeString(img)
        parcel.writeString(intro)
        parcel.writeString(author)
        parcel.writeString(category)
        parcel.writeString(updateTime)
        parcel.writeInt(novelId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NovelInfo> {
        override fun createFromParcel(parcel: Parcel): NovelInfo {
            return NovelInfo(parcel)
        }

        override fun newArray(size: Int): Array<NovelInfo?> {
            return arrayOfNulls(size)
        }
        fun search(key:String,index:Int, cb: ((novels:ResultInfo)->Unit)){
            val url = "http://zhannei.baidu.com/cse/search?s=2041213923836881982&q=$key&p=$index&isNeedCheckDomain=1&jump=1"
            HttpTool.get(url,object :okhttp3.Callback{
                var result = ResultInfo()
                override fun onFailure(call: Call?, e: IOException?) {
                    result.code = errcode_netword_error
                    result.message = "网络错误，请重新再试"
                    cb(result)
                    e?.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    val arrNovels = ArrayList<NovelInfo>()
                    try {
                        val responseText = response.body()!!.string()
                        val js =Jsoup.parse(responseText)
                        val novelhtmls = js.select("div.result-item")
                        for(e in novelhtmls){
                            val novel = NovelInfo()
                            novel.img = e.select("img.result-game-item-pic-link-img").first().attr("src")
                            novel.url = e.select("a.result-game-item-title-link").first().attr("href")
                            novel.novelId = Math.abs(novel.url.hashCode())
                            novel.title = e.select("a.result-game-item-title-link").first().text()
                            novel.intro = e.select("p.result-game-item-desc").first().text()
                            val info = e.select("p.result-game-item-info-tag")
                            novel.author = info[0].child(1).text()
                            novel.category = info[1].child(1).text()
                            novel.updateTime = info[2].child(1).text()
                            arrNovels.add(novel)
                        }
                        result.data = arrNovels
                        cb(result)
                    }
                    catch (e:Exception){
                        result.code = errcode_html_resolve_error
                        result.message = "HTML解析错误"
                        cb(result)
                        e.printStackTrace()
                    }
                }
            })
        }
        fun getSections(url:String,cb: ((novels:ResultInfo)->Unit)){
            HttpTool.get(url,object :okhttp3.Callback{
                var result = ResultInfo()
                override fun onFailure(call: Call?, e: IOException?) {
                    result.code = errcode_netword_error
                    result.message = "网络错误，请重新再试"
                    cb(result)
                    e?.printStackTrace()
                }
                override fun onResponse(call: Call, response: Response) {
                    var arrSections = ArrayList<SectionInfo>()
                    try {
                        //issue3 when you convert something to string ,java use a default charset to convert, when the default charset do not match the stream, the chines to show random code and lost
                        //the origin charset, you can not convert it ti the correct charset, so the first thing is to convert to bytes ,bytes do not contain the charset ,then usr String(str,charset)
                        //to convert the right charset
                        val responseText = String(response.body()!!.bytes(), charset("GBK"))
                        val js =Jsoup.parse(responseText)
                        val sectionHtml = js.select("div#list").first().child(0).children()
                        for (s in sectionHtml){
                            if (s.tagName() == "dd"){
                                var section = SectionInfo()
                                section.title = s.child(0).text()
                                section.sectionUrl = s.child(0).attr("href")
                                arrSections.add(section)
                            }
                        }
                        result.data = arrSections
                        cb(result)
                    }
                    catch (e:Exception){
                        result.code = errcode_html_resolve_error
                        result.message = "HTML解析错误"
                        cb(result)
                        e.printStackTrace()
                    }
                }
            })
        }
    }
}



class SectionInfo() :DataSupport(),Parcelable{
    var id = 0
    var novelId = 0
    var sectionUrl = ""
    var title = ""
    var content = ""
    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        novelId = parcel.readInt()
        sectionUrl = parcel.readString()
        title = parcel.readString()
        content = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(novelId)
        parcel.writeString(sectionUrl)
        parcel.writeString(title)
        parcel.writeString(content)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SectionInfo> {
        override fun createFromParcel(parcel: Parcel): SectionInfo {
            return SectionInfo(parcel)
        }

        override fun newArray(size: Int): Array<SectionInfo?> {
            return arrayOfNulls(size)
        }

        fun getNovelSection(url: String,cb: (novels: ResultInfo) -> Unit){
            HttpTool.get(url,object :okhttp3.Callback{
                var result = ResultInfo()
                override fun onFailure(call: Call?, e: IOException?) {
                    result.code = errcode_netword_error
                    result.message = "网络错误，请重新再试"
                    cb(result)
                    e?.printStackTrace()
                }
                override fun onResponse(call: Call, response: Response) {
                    try {
                        val responseText = String(response.body()!!.bytes(), charset("GBK"))
                        val js =Jsoup.parse(responseText)
                        val sectionHtml = js.select("div#content").first().html().replace("&nbsp;","")
                        result.data = sectionHtml
                        cb(result)
                    }
                    catch (e:Exception){
                        result.code = errcode_html_resolve_error
                        result.message = "HTML解析错误"
                        cb(result)
                        e.printStackTrace()
                    }
                }
            })
        }
    }
}