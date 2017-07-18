package stan.androiddemo.project.weather.gson

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by hugfo on 2017/7/18.
 */
class Weather protected constructor(`in`: Parcel) : Parcelable {
    var status: String
    var basic: Basic? = null
    var aqi: AQI? = null
    var now: Now? = null
    var suggestion: Suggestion? = null

    @SerializedName("daily_forecast")
    var forecastList: List<Forecast>? = null

    init {
        status = `in`.readString()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {

        val CREATOR: Parcelable.Creator<Weather> = object : Parcelable.Creator<Weather> {
            override fun createFromParcel(`in`: Parcel): Weather {
                return Weather(`in`)
            }

            override fun newArray(size: Int): Array<Weather> {
                return newArray(size)
            }
        }
    }
}
