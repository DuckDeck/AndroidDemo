package stan.androiddemo.project.novel.model

import okhttp3.Call
import okhttp3.Response
import org.jsoup.Jsoup
import org.litepal.crud.DataSupport
import stan.androiddemo.tool.HttpTool
import java.io.IOException

/**
 * Created by hugfo on 2017/7/20.
 */
class NovelInfo{
    var id = 0
    var url = ""
    var title = ""
    var img = ""
    var intro = ""
    var author = ""
    var category = ""
    var updateTime = ""

    companion object {
        fun search(key:String, cb: ((novels:ArrayList<NovelInfo>)->Int)){
            val url = "http://zhannei.baidu.com/cse/search?s=2041213923836881982&q=$key&isNeedCheckDomain=1&jump=1&qq-pf-to=pcqq.c2c"
            HttpTool.sendOKHttpRequest(url,object :okhttp3.Callback{
                override fun onFailure(call: Call?, e: IOException?) {
                    e?.printStackTrace()

                }

                override fun onResponse(call: Call, response: Response) {
                    val arrNovels = ArrayList<NovelInfo>()
                    try {
                        val responseText = response.body().string()
                        val js =Jsoup.parse(responseText)
                        val novelhtmls = js.select("div.result-item")
                        for(e in novelhtmls){
                            val novel = NovelInfo()
                            novel.img = e.select("img.result-game-item-pic-link-img").first().attr("src")
                            novel.url = e.select("a.result-game-item-title-link").first().attr("href")
                            novel.id = novel.url.hashCode()
                            novel.title = e.select("a.result-game-item-title-link").first().child(0).text()
                            novel.intro = e.select("p.result-game-item-desc").first().text()
                            val info = e.select("p.result-game-item-info-tag")
                            novel.author = info[0].child(1).text()
                            novel.category = info[1].child(1).text()
                            novel.updateTime = info[2].child(1).text()
                            arrNovels.add(novel)
                        }

                    }
                    catch (e:Exception){
                        e.printStackTrace()
                    }
                    finally {
                        cb(arrNovels)
                    }
                }
            })
        }
    }
}

class SectionInfo:DataSupport(){
    var id = 0
    var novelId = 0
    var sectionUrl = ""
    var title = ""
}