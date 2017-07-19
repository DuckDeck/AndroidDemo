package stan.androiddemo.project.weather.utility

import android.text.TextUtils
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.litepal.crud.DataSupport
import stan.androiddemo.project.weather.db.City
import stan.androiddemo.project.weather.db.Country
import stan.androiddemo.project.weather.db.Province
import stan.androiddemo.project.weather.gson.Weather

/**
 * Created by hugfo on 2017/7/18.
 */
class Utility {

    companion object {
        fun handleProvinceResponse(response: String): Boolean {
            if (!TextUtils.isEmpty(response)) {
                try {
                    val allProvince = JSONArray(response)
                    for (i in 0..allProvince.length() - 1) {
                        val provinceObject = allProvince.getJSONObject(i)
                        val province = Province()
                        province.provinceName = provinceObject.getString("name")
                        province.provinceCode = provinceObject.getInt("id")
                        province.save()
                    }
                    return true

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
            return false
        }

        fun handleCityResponse(response: String, provinceId: Int): Boolean {
            if (!TextUtils.isEmpty(response)) {
                try {
                    val allCities = JSONArray(response)
                    for (i in 0..allCities.length() - 1) {
                        val cityObject = allCities.getJSONObject(i)
                        val city = City()
                        city.cityName =  cityObject.getString("name")
                        city.cityCode = cityObject.getInt("id")
                        city.provinceId = provinceId
                        city.save()
                    }
                    return true

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
            return false
        }


        fun handleCountryResponse(response: String, cityId: Int): Boolean {
            if (!TextUtils.isEmpty(response)) {
                try {
                    val allCounties = JSONArray(response)
                    for (i in 0..allCounties.length() - 1) {
                        val countryObject = allCounties.getJSONObject(i)
                        val country = Country()
                        country.countryName = (countryObject.getString("name"))
                        country.weatherId = (countryObject.getString("weather_id"))
                        country.cityId = (cityId)
                        country.save()
                    }
                    return true

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
            return false
        }


        fun handleWeatherResponse(response: String): Weather? {
            try {
                val jsonObject = JSONObject(response)
                val jsonArray = jsonObject.getJSONArray("HeWeather")
                val weatherContent = jsonArray.getJSONObject(0).toString()
                return Gson().fromJson<Weather>(weatherContent, Weather::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null
        }
    }


}