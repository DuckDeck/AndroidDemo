package stan.androiddemo.project.weather.gson

import com.google.gson.annotations.SerializedName

/**
 * Created by hugfo on 2017/7/18.
 */

class Forecast {
    var date: String? = null
    @SerializedName("tmp")
    var temperature: Temperature? = null

    @SerializedName("cond")
    var more: More? = null

    inner class Temperature {
        var max: String = ""
        var min: String = ""
    }

    inner class More {
        @SerializedName("txt_d")
        var info: String = ""
    }

}
