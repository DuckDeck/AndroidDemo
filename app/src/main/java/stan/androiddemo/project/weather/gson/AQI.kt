package stan.androiddemo.project.weather.gson

import java.io.Serializable

/**
 * Created by hugfo on 2017/7/18.
 */

class AQI:Serializable{

    public var city:AQICity? = null

    class AQICity{
        public var aqi = ""
        public var pm25 = ""
    }
}