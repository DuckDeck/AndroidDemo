package stan.androiddemo.project.Mito.Model

import android.os.Parcel
import android.os.Parcelable
import okhttp3.Call
import okhttp3.Response
import org.jsoup.Jsoup
import stan.androiddemo.Model.ResultInfo
import stan.androiddemo.errcode_html_resolve_error
import stan.androiddemo.errcode_netword_error
import stan.androiddemo.tool.HttpTool
import java.io.IOException

/**
 * Created by stanhu on 03/01/2018.
 */
class ImageSubjectInfo() : Parcelable {
    var subjectName = ""
    var subjectUrl = ""
    var subjectImage = ""

    constructor(parcel: Parcel) : this() {
        subjectName = parcel.readString()
        subjectUrl = parcel.readString()
        subjectImage = parcel.readString()
    }

    companion object CREATOR : Parcelable.Creator<ImageSubjectInfo> {

        override fun createFromParcel(parcel: Parcel): ImageSubjectInfo {
            return ImageSubjectInfo(parcel)
        }

        override fun newArray(size: Int): Array<ImageSubjectInfo?> {
            return arrayOfNulls(size)
        }

        fun getSubjects(url:String,cb:(subjects: ResultInfo) -> Unit){
            HttpTool.get(url,object :okhttp3.Callback{
                var result = ResultInfo()
                override fun onFailure(call: Call?, e: IOException?) {
                    result.code = errcode_netword_error
                    result.message = "网络错误，请重新再试"
                    cb(result)
                    e?.printStackTrace()
                }
                override fun onResponse(call: Call?, response: Response?) {
                    try {
                        val responseText = response!!.body()!!.string()
                        val js = Jsoup.parse(responseText)
                        val tags = js.select("ul.clearfix").first().children()
                        val arrImageSets = ArrayList<ImageSubjectInfo>()
                        var count = js.select("a.a1").first().text()
                        if (!count.isNullOrEmpty()){
                            count = count.replace("条","")
                            result.count = count.toInt()
                        }
                        for (l in tags){
                            val sub = ImageSubjectInfo()
                            sub.subjectUrl = l.child(0).attr("href")
                            sub.subjectName = l.select("a>p").first().text()
                            sub.subjectImage = l.select("a>img").first().attr("src")
                            arrImageSets.add(sub)
                        }
                        result.data = arrImageSets
                        cb(result)
                    }
                    catch (e:Exception){
                        result.code = errcode_html_resolve_error
                        result.message = "HTML解析错误或者结果不存在"
                        cb(result)
                        e.printStackTrace()
                    }
                }
            })
        }

        fun getImageSubject(subUrl:String,cb:(subject:ResultInfo)-> Unit){
            HttpTool.get(subUrl, object : okhttp3.Callback {
                var result = ResultInfo()
                override fun onFailure(call: Call?, e: IOException?) {
                    result.code = errcode_netword_error
                    result.message = "网络错误，请重新再试"
                    cb(result)
                    e?.printStackTrace()
                }

                override fun onResponse(call: Call?, response: Response?) {
                    val responseText = response!!.body()!!.string()
                    val js = Jsoup.parse(responseText)
                    val tags = js.select("ul.clearfix").first().children()
                    var count = js.select("a.a1").first().text()
                    if (!count.isNullOrEmpty()){
                        count = count.replace("条","")
                        result.count = count.toInt()
                    }
                    val arrImageSets = ArrayList<ImageSetInfo>()
                    val reg = Regex("\\D+")
                    for (set in tags){
                        val imageSet = ImageSetInfo()
                        val img = set.select("div.listbox").first()
                        imageSet.mainImage = img.select("a>img").first().attr("src")
                        imageSet.title = img.select("a>span").first().text()
                        imageSet.url = img.select("a").first().attr("href")
                        val c =  img.select("em.page_num").first().text().replace(reg,"").toIntOrNull()
                        if (c != null){
                            imageSet.count = c!!
                        }
                        val imageInfo = set.select("div.listbott").first()
                        imageSet.category = imageInfo.select("em").first().text()
                        var res = imageInfo.select("span.fbl").first().text()
                        if (res.contains("(")){
                            res = res.split("(")[0]
                        }
                        if (res.contains("其他")){
                            res = "1920x1080"
                        }
                        if (res.isNullOrEmpty()){
                            res = "1000x1000"
                        }
                        imageSet.resolution = Resolution(res)
                        imageSet.resolutionStr = imageSet.resolution.toString()
                        imageSet.theme = imageInfo.select("span.color").first().text()
                        arrImageSets.add(imageSet)
                    }
                    result.data = arrImageSets
                    cb(result)
                }

            })
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(subjectName)
        parcel.writeString(subjectUrl)
        parcel.writeString(subjectImage)
    }

    override fun describeContents(): Int {
        return 0
    }




}