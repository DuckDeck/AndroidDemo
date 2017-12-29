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
                        val imageSets = js.select("dl.filter_item")[1].children().last().children()
                        imageSets.removeAt(0)
                        val imageCat  = ImageCatInfo()
                        for (item in imageSets){
                            var res = Resolution(item.text())
                            val href =  item.attr("href")
                            res.resolutionCode = href.substring(href.length - 13,href.length - 9)
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


    }
}