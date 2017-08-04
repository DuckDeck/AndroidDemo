package stan.androiddemo.project.Mito.Model

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
 * Created by stanhu on 4/8/2017.
 */

const val PCImage = "http://www.5857.com/list-9"
class ImageSetInfo:DataSupport(){
    var url = ""
    var category = ImageCategory.All
    var title = ""
    var mainTag = ""
    //var tags
    var resolution = Resolution()
    var resolutionStr:String = ""
        get() {return  resolution.toString()}
    var theme = ImageTheme.All
    var mainImage = ""
    var images = ArrayList<String>()

    companion object {
        fun imageSets(cat:ImageCategory,resolution: Resolution,theme:ImageTheme, index:Int, cb: ((imageSets: ResultInfo)->Unit)){
            val fixedIndex = index + 1
            val url = PCImage + "-" + theme.ordinal + "-" + cat.ordinal + "-0-" + resolution.toUrlPara() + "-0-"+fixedIndex+".html"
            HttpTool.sendOKHttpRequest(url,object  :okhttp3.Callback{
                var result = ResultInfo()
                override fun onFailure(call: Call?, e: IOException?) {
                    result.code = errcode_netword_error
                    result.message = "网络错误，请重新再试"
                    cb(result)
                    e?.printStackTrace()
                }
                override fun onResponse(call: Call, response: Response) {
                    val arrImageSets = ArrayList<ImageSetInfo>()
                    try {
                        val responseText = response.body().string()
                        val js = Jsoup.parse(responseText)
                        val imageSets = js.select("ul.clearfix").first().children()
                        for (set in imageSets){
                            val imageSet = ImageSetInfo()
                            val img = set.select("div.listbox").first()
                            imageSet.mainImage = img.select("a>img").first().attr("src")
                            imageSet.title = img.select("a>span").first().text()
                            imageSet.url = img.select("a").first().attr("href")
                            val imageInfo = set.select("div.listbott").first()
//                            imageSet.category = ImageCategory.valueOf(imageInfo.select("rm").first().text())
//                            imageSet.resolution = Resolution(imageInfo.select("span.fbl").first().text())
//                            imageSet.theme = ImageTheme.valueOf(imageInfo.select("span.color").first().text())
                            arrImageSets.add(imageSet)
                        }
                        result.data = arrImageSets
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
// 全部	美女	性感	明星	风光	卡通	创意	汽车	游戏	建筑	影视	植物	动物	节庆	可爱	静物	体育	日历	唯美	其它	系统	动漫	非主流	小清新
enum class ImageCategory(value:Int){
    All(0),Beauty(3365),Sexy(3437),Star(3366),Scene(4)
    ,Cartoon(4),Original(4),Car(4),Game(4),Build(4),Plant(4),Animal(4)
    ,Feast(4),Love(4),Cute(4),Still(4),Sports(4),Calendar(4),Gorgeous(4)
    ,Other(4),System(4),Comic(4),Nonmainstream(4),Fresh(4);

    companion object {
        fun convert(str:String):ImageCategory{
            when(str){
                "全部"->return All
                "美女"->return Beauty
                "性感"->return Sexy
                "明星"->return Star
            }
            return ImageCategory.All
        }
    }

    override fun toString(): String {
        when(this){
            ImageCategory.All->"全部"
            ImageCategory.Beauty->"美女"
            ImageCategory.Sexy->"性感"
            ImageCategory.Star->"明星"
        }
        return "全部"
    }
}
// 橙色 黄色 绿色 紫色 粉色 青色 蓝色 棕色 白色 黑色 银色 灰色
enum class ImageTheme(value:Int){
    All(0),Red(3383),Orange(3384),Yellow(3385),Green(3386),Purple(3387),Pink(3388)
    ,Cyan(3389),Blue(3390),Brown(3391),White(3392),Black(3393),Silver(3394),Gray(3395);

   companion object {
       fun convert(str:String):ImageTheme{
           when(str){
              "全部"->return  ImageTheme.All
               "红色"->return  ImageTheme.Red
               "橙色"->return  ImageTheme.Orange
               "黄色"->return  ImageTheme.Yellow
               "绿色"->return  ImageTheme.Green
               "紫色"->return  ImageTheme.Purple
               "粉色"->return  ImageTheme.Pink
               "青色"->return  ImageTheme.Cyan
               "蓝色"->return  ImageTheme.Black
               "棕色"->return  ImageTheme.Brown
               "白色"->return  ImageTheme.White
               "黑色"->return  ImageTheme.Black
               "银色"->return  ImageTheme.Silver
               "灰色"->return  ImageTheme.Gray
           }
           return  ImageTheme.All
       }
   }

    override fun toString(): String {
        when(this){
            All->return "全部"
            Red->return "红色"
            Orange->return "橙色"
            Yellow->return "黄色"
            Green->return "绿色"
            Purple->return "紫色"
            Pink->return "粉色"
            Cyan->return "青色"
            Blue->return "蓝色"
            Brown->return "棕色"
            White->return "白色"
            Black->return "黑色"
            Silver->return "银色"
            Gray->return "灰色"
        }
        return ""
    }
}

class Resolution{
    constructor()
    constructor(resolution: String){
        val res = resolution.split("x")
        if (res.size <= 1)
        {
            pixelX = res[0].toInt()
        }
        else {
            pixelX = res[0].toInt()
            pixelY = res[1].toInt()
        }
    }

    var pixelX = 0
    var pixelY = 0
    override fun toString(): String {
        return pixelX.toString() + "x" + pixelY.toString()
    }

    fun toUrlPara():String{
        if (pixelX == 0 && pixelY == 0){
            return "0"
        }
        else if(pixelX == 3840 && pixelY == 1200){
            return  "3440"
        }
        return "0"

    }
}

