package stan.androiddemo.project.FiveStroke.Model

import okhttp3.Call
import okhttp3.Response
import org.jsoup.Jsoup
import org.litepal.crud.DataSupport
import stan.androiddemo.Model.ResultInfo
import stan.androiddemo.errcode_html_resolve_error
import stan.androiddemo.errcode_netword_error
import stan.androiddemo.tool.HttpTool
import java.io.IOException
import java.lang.Exception

/**
 * Created by hugfo on 2017/8/5.
 */
class FiveStrokeInfo:DataSupport(){
    var letter = ""
    var spell = ""
    var code = ""
    var decodeUrl = ""

    companion object {
        fun letterCode(letter:String, cb: ((imageSets: ResultInfo)->Unit)){
            val url =  "http://www.52wubi.com/wbbmcx/search.php"
            val header = hashMapOf("Accept" to "text/html, application/xhtml+xml, image/jxr, */*",
                    "Referer" to "http://www.52wubi.com/wbbmcx/search.php",
                    "Accept-Language" to "zh-Hans-CN,zh-Hans;q=0.8,en-US;q=0.5,en;q=0.3",
                    "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36 Edge/15.15063",
                    "Content-Type" to "application/x-www-form-urlencoded",
                    "Content-Length" to letter.toByteArray().size.toString(),
                    "Proxy-Connection" to " Keep-Alive",
                    "Pragma" to "no-cache")

            HttpTool.post(url,header, hashMapOf("hzname" to letter), object  :okhttp3.Callback{
                var result = ResultInfo()
                override fun onFailure(call: Call?, e: IOException?) {
                    result.code = errcode_netword_error
                    result.message = "网络错误，请重新再试"
                    cb(result)
                    e?.printStackTrace()
                }
                override fun onResponse(call: Call, response: Response) {
                    val arr = ArrayList<FiveStrokeInfo>()
                    try {
                        val responseText = String(response.body().bytes(), charset("GBK"))
                        val js = Jsoup.parse(responseText)
                        val letters = js.select("#zgbg").first().select("tr.tbhover")
                        for (l in letters){
                            val f = FiveStrokeInfo()
                            f.letter = l.select("td>a").first().text()
                            f.spell = l.child(1).text()
                            f.code = l.child(2).text()
                            f.decodeUrl ="http://www.52wubi.com/wbbmcx/" +  l.select("td>img").first().attr("src")
                            arr.add(f)
                        }
                        result.data = arr
                        cb(result)
                    }
                    catch (e: Exception){
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