package stan.androiddemo.project.Mito.Model

import android.os.Parcel
import android.os.Parcelable
import okhttp3.Call
import okhttp3.Response
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import stan.androiddemo.Model.ResultInfo
import stan.androiddemo.errcode_html_resolve_error
import stan.androiddemo.errcode_netword_error
import stan.androiddemo.tool.HttpTool
import java.io.IOException
import java.lang.Exception

/**
 * Created by stanhu on 28/12/2017.
 */
class ImageCatInfo() : Parcelable {
    var catName = ""
    var resulotions = mutableListOf<Resolution>()

    constructor(parcel: Parcel) : this() {
        catName = parcel.readString()
        //resulotions = parcel.readParcelableArray(ClassLoader.getSystemClassLoader()).toMutableList()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(catName)
        //parcel.writeParcelableArray(resulotions.toTypedArray(),flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ImageCatInfo> {
        override fun createFromParcel(parcel: Parcel): ImageCatInfo {
            return ImageCatInfo(parcel)
        }

        override fun newArray(size: Int): Array<ImageCatInfo?> {
            return arrayOfNulls(size)
        }

        fun imageCats(type:Int,cat:String,theme:String,  cb: ((ImageCatInfo: ResultInfo)->Unit)){

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
            val url = baseUrl + "-" + ImageSetInfo.themeToUrlPara(theme) + "-" + ImageSetInfo.catToUrlPara(cat) + "-0-" + "-0-1.html"
            HttpTool.get(url,object  :okhttp3.Callback{
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
                        val imageSets = ImageCatInfo.elementsFromHtml(type,js)
                        val imageCat  = ImageCatInfo()
                        imageCat.resulotions.add(Resolution.wholeResolution)
                        for (item in imageSets){
                            val resStr = item.text()
                            var res = Resolution()
                            if (resStr.contains('(')){
                                val index = resStr.indexOf('(')
                                res.setResolution(resStr.substring(0,index))
                                res.device =  resStr.substring(index + 1,resStr.length - 1)
                            }
                            else{
                                res.setResolution(resStr)
                            }

                            val href =  item.attr("href")
                            when(type){
                                0,3->{
                                    res.resolutionCode = href.substring(href.length - 13,href.length - 9)
                                }
                                1->{
                                    res.resolutionCode = href.substring(href.length - 14,href.length - 10)
                                }
                                2->{
                                    res.resolutionCode = href.substring(href.length - 11,href.length - 7)
                                }
                            }

                            imageCat.resulotions.add(res)
                        }

                        result.data = imageCat
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

        fun elementsFromHtml(type: Int,html:Document):Elements{
            when(type){
                0,1->{
                    val imageSets = html.select("dl.filter_item")[1].children().last().children()
                    imageSets.removeAt(0)
                    return imageSets
                }
                2->{
                    val verSets = html.select("dl.filter_item")[1].children()[2].children()
                    verSets.removeAt(0)
                    val horSets = html.select("dl.filter_item")[1].children().last().children()
                    horSets.removeAt(0)
                    verSets.addAll(horSets)
                    return verSets
                }
                3->{
                    val comSets = html.select("dl.filter_item")[1].children()[2].children()
                    comSets.removeAt(0)
                    val padSets = html.select("dl.filter_item")[1].children()[3].children()
                    padSets.removeAt(0)
                    val phoneSets = html.select("dl.filter_item")[1].children()[4].children()
                    phoneSets.removeAt(0)
                    comSets.addAll(padSets)
                    comSets.addAll(phoneSets)
                    return comSets
                }
                else->{
                    return Elements()
                }
            }
        }
    }


}