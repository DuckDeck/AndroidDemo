package stan.androiddemo.project.weather.db

import org.litepal.crud.DataSupport

/**
 * Created by hugfo on 2017/7/18.
 */
class Country:DataSupport(){
    internal var id: Int = 0
    internal var countryName: String = ""
    internal var weatherId: String = ""
    internal var cityId: Int = 0

}