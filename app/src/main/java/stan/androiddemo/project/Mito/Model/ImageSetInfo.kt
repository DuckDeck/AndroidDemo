package stan.androiddemo.project.Mito.Model

import android.os.Parcel
import android.os.Parcelable
import okhttp3.Call
import okhttp3.Response
import org.jsoup.Jsoup
import org.litepal.crud.DataSupport
import stan.androiddemo.Model.ResultInfo
import stan.androiddemo.errcode_html_resolve_error
import stan.androiddemo.errcode_netword_error
import stan.androiddemo.errcode_result_not_found
import stan.androiddemo.tool.HttpTool
import stan.androiddemo.tool.ToFixedInt
import java.io.IOException
import java.lang.Exception
import java.net.URLEncoder
import java.util.regex.Pattern

/**
 * Created by stanhu on 4/8/2017.
 */

const val PCImage = "http://www.5857.com/list-9"
const val PadImage = "http://www.5857.com/list-10"
const val PhoneImage = "http://www.5857.com/list-11"
const val EssentialImage = "http://www.5857.com/list-37"
class ImageSetInfo() :DataSupport(),Parcelable{
    var url = ""
    var category = ""
    var title = ""
    var mainTag = ""
    var tags = ArrayList<String>()
    var resolution = Resolution()
    var resolutionStr:String = ""
    var theme = ""
    var mainImage = ""
    var images = ArrayList<String>()
    var count = 0
    var imgBelongCat = 0
    var isCollected = false
    var hashId = 0
    constructor(parcel: Parcel) : this() {
        url = parcel.readString()
        category = parcel.readString()
        title = parcel.readString()
        mainTag = parcel.readString()
        resolution = parcel.readParcelable(Resolution::class.java.classLoader)
        theme = parcel.readString()
        mainImage = parcel.readString()
        count = parcel.readInt()
    }

    companion object CREATOR : Parcelable.Creator<ImageSetInfo> {
        override fun createFromParcel(parcel: Parcel): ImageSetInfo {
            return ImageSetInfo(parcel)
        }

        override fun newArray(size: Int): Array<ImageSetInfo?> {
            return arrayOfNulls(size)
        }

        fun imageSets(type:Int,cat:String,resolution: Resolution,theme:String, index:Int, cb: ((imageSets: ResultInfo)->Unit)){
            val fixedIndex = index + 1
            var baseUrl  = PCImage
            if (type == 1){
                baseUrl = PadImage
            }
            else if (type == 2){
                baseUrl = PhoneImage
            }
            else if (type == 3){
                baseUrl = EssentialImage
            }
            var url = baseUrl + "-" + ImageSetInfo.themeToUrlPara(theme) + "-" + ImageSetInfo.catToUrlPara(cat) + "-0-" + resolution.resolutionCode + "-0-"+fixedIndex+".html"
            when(type){
                1->{
                     url = baseUrl + "-" + ImageSetInfo.themeToUrlPara(theme) + "-" + ImageSetInfo.catToUrlPara(cat) + "-" + resolution.resolutionCode + "-0-0-"+fixedIndex+".html"
                }
                2->{
                    url = baseUrl + "-" + ImageSetInfo.themeToUrlPara(theme) + "-" + ImageSetInfo.catToUrlPara(cat) + "-0-0-"+  resolution.resolutionCode + "-" +fixedIndex+".html"
                }
            }
            print(url)
            HttpTool.get(url,object  :okhttp3.Callback{
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
                        val responseText = response.body()!!.string()
                        val js = Jsoup.parse(responseText)
                        val imageSets = js.select("ul.clearfix").first().children()
                        if (imageSets.count() == 1 && imageSets.first().children().count() == 0){
                            result.code = errcode_result_not_found
                            result.message = "无有找到图片"
                            cb(result)
                            return
                        }
                        val reg = Regex("\\D+")
                        for (set in imageSets){
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

                            imageSet.resolution = Resolution(res)
                            if (imageSet.resolution.pixelX == 0){
                                when(type){
                                    0-> imageSet.resolution = Resolution.standardComputerResolution
                                    1-> imageSet.resolution = Resolution.standardPadResolution
                                    2-> imageSet.resolution = Resolution.standardPhoneResolution
                                    3-> imageSet.resolution = Resolution.standardEssentialResolution
                                }
                            }
                            imageSet.hashId = imageSet.url.hashCode()
                            imageSet.resolutionStr = imageSet.resolution.toString()
                            imageSet.theme = imageInfo.select("span.color").first().text()
                            arrImageSets.add(imageSet)
                        }
                        result.data = arrImageSets
                        cb(result)
                    }
                    catch (e:Exception){
                        result.code = errcode_html_resolve_error
                        result.message = "HTML解析错误或者没有图片"
                        cb(result)
                        e.printStackTrace()
                    }
                }

            })
        }

        fun searchImages(keyword:String,index:Int,cb:(imageSets: ResultInfo) -> Unit){
            val url = "http://www.5857.com/index.php?m=search&c=index&a=init&typeid=3&q=" + URLEncoder.encode(keyword,"UTF-8") + "&page=" + index
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
                        //    val count = js.select("div.position").text()
                        //    val s = Pattern.compile("[^0-9]").matcher(count)
                        // do not need all image count now
                        val reg = Regex("\\D+")
                        val arrImageSets = ArrayList<ImageSetInfo>()
                        for (l in tags){
                            val imageSet = ImageSetInfo()
                            val img = l.select("div.listbox").first() ?: continue
                            imageSet.mainImage = img.select("a>img").first().attr("src")
                            imageSet.title = img.select("a>span").first().text()
                            imageSet.url = img.select("a").first().attr("href")
                            val c =  img.select("em.page_num").first().text().replace(reg,"").toIntOrNull()
                            if (c != null){
                                imageSet.count = c!!
                            }
                            val imageInfo = l.select("div.listbott").first() ?: continue
                            imageSet.category = imageInfo.select("em>a").first().text()
                            var res = imageInfo.select("span.fbl>a").first().text()
                            if (res.contains("(")){
                                res = res.split("(")[0]
                            }
                            if (res.contains("其他")){
                                res = "1920x1080"
                            }
                            imageSet.hashId = imageSet.url.hashCode()
                            imageSet.resolution = Resolution(res)
                            imageSet.resolutionStr = imageSet.resolution.toString()
                            imageSet.theme = imageInfo.select("span.color>a").first().text()
                            arrImageSets.add(imageSet)
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
        //获取同类图片集合
        fun imageSet(imgSet: ImageSetInfo,cb: (imageSets: ResultInfo) -> Unit){
            HttpTool.get(imgSet.url,object  :okhttp3.Callback{
                var result = ResultInfo()
                override fun onFailure(call: Call?, e: IOException?) {
                    result.code = errcode_netword_error
                    result.message = "网络错误，请重新再试"
                    cb(result)
                    e?.printStackTrace()
                }
                override fun onResponse(call: Call, response: Response) {
                    try {
                        val responseText = response.body()!!.string()
                        val js = Jsoup.parse(responseText)

                        if(imgSet.count == 1){
                            val tags = js.select("div.con-tags").first().select("a")
                            for (tag in tags){
                                imgSet.tags.add(tag.text())
                            }
                            val img = js.select("a.photo-a").first().child(0).attr("src")
                            imgSet.images.add(img)
                        }
                        else{
                            val tags = js.select("p.main_center_bianqin_fl").first().select("a")
                            for (tag in tags){
                                imgSet.tags.add(tag.select("span").text())
                            }
                            val imgs = js.select("div.img-box").first().select("a")
                            for (img in imgs){
                                val i = img.select("img").first().attr("src")
                                imgSet.images.add(i)
                            }
                        }

                        result.data = imgSet
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

        fun catToUrlPara(str:String):Int{
            when(str){
                "全部"->return 0
                "美女"->return 3365
                "性感"->return 3437
                "明星"->return 3366
                "风光"->return 3367
                "卡通"->return 3368
                "创意"->return 3370
                "汽车"->return 3371
                "游戏"->return 3372
                "建筑"->return 3373
                "影视"->return 3374
                "植物"->return 3376
                "动物"->return 3377
                "节庆"->return 3378
                "可爱"->return 3379
                "静物"->return 3369
                "体育"->return 3380
                "日历"->return 3424
                "唯美"->return 3430
                "其它"->return 3431
                "系统"->return 3434
                "动漫"->return 3444
                "非主流"->return 3375
                "小清新"->return 3381

            }
            return 0
        }

        fun themeToUrlPara(str:String):Int{
            when(str){
                "全部"->return 0
                "红色"->return 3383
                "橙色"->return 3384
                "黄色"->return 3385
                "绿色"->return 3386
                "紫色"->return 3387
                "粉色"->return 3388
                "青色"->return 3389
                "蓝色"->return 3390
                "棕色"->return 3391
                "白色"->return 3392
                "黑色"->return 3393
                "银色"->return 3394
                "灰色"->return 3395
            }
            return 0
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(url)
        parcel.writeString(category)
        parcel.writeString(title)
        parcel.writeString(mainTag)
        parcel.writeParcelable(resolution, flags)
        parcel.writeString(theme)
        parcel.writeString(mainImage)
        parcel.writeInt(count)
    }

    override fun describeContents(): Int {
        return 0
    }
}






