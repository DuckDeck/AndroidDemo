package stan.androiddemo.project.weather.db

import org.litepal.crud.DataSupport

/**
 * Created by hugfo on 2017/7/18.
 */
class Province :DataSupport(){
    internal var id: Int = 0
    internal var provinceName: String = ""
    internal var provinceCode: Int = 0
}