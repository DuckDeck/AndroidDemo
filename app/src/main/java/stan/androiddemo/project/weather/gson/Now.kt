package stan.androiddemo.project.weather.gson

import com.google.gson.annotations.SerializedName

/**
 * Created by hugfo on 2017/7/18.
 */
class Now {
    @SerializedName("tmp")
    var temperature: String? = null
    @SerializedName("cond")
    var more: More? = null

    inner class More {
        @SerializedName("txt")
        var info: String? = null
    }
}
