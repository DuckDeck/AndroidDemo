package stan.androiddemo.project.weather.gson

import com.google.gson.annotations.SerializedName

/**
 * Created by hugfo on 2017/7/18.
 */
class Basic {
    @SerializedName("city")
    var cityName: String? = null

    @SerializedName("id")
    var weatherId: String? = null

    var update: Update? = null

    inner class Update {
        @SerializedName("loc")
        var updateTime: String? = null
    }

}
