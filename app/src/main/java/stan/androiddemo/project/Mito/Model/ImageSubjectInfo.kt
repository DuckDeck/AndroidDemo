package stan.androiddemo.project.Mito.Model

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
class ImageSubjectInfo {
    var subjectName = ""
    var subjectUrl = ""
    var subjectImage = ""
    companion object {
        fun getSubjects(cb:(subjects: ResultInfo) -> Unit){
            val url = "http://www.5857.com/zhuanti.html"
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
    }
}