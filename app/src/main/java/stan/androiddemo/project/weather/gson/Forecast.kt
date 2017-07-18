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
        var max: String? = null
        var min: String? = null
    }

    inner class More {
        @SerializedName("txt_d")
        var info: String? = null
    }

}
