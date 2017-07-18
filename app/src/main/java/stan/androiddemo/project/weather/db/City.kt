package stan.androiddemo.project.weather.db

import org.litepal.crud.DataSupport

/**
 * Created by hugfo on 2017/7/18.
 */
class City:DataSupport(){
    internal var id: Int = 0
    internal var cityName: String = ""
    internal var cityCode: Int = 0
    internal var provinceId: Int = 0
}