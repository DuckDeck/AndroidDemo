package stan.androiddemo.project.weather

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_choose_area.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.litepal.crud.DataSupport
import stan.androiddemo.R
import stan.androiddemo.project.weather.db.City
import stan.androiddemo.project.weather.db.Country
import stan.androiddemo.project.weather.db.Province
import stan.androiddemo.project.weather.utility.Utility
import stan.androiddemo.tool.HttpTool
import java.io.IOException
import java.util.*


class ChooseAreaFragment : Fragment() {
    val LEVEL_PROVINCE = 0
    val LEVEL_CITY = 1
    val LEVEL_COUNTRY = 2

    private var progressBar: ProgressBar? = null
    lateinit var adapter: ArrayAdapter<String>
    internal var dataList: MutableList<String> = ArrayList()
    lateinit var provinceList: List<Province>
    lateinit var cityList: List<City>
    lateinit var countryList: List<Country>
    lateinit var selectedProvince: Province
    lateinit var list_view:ListView
    lateinit var selectedCity: City
    var currentLevel: Int = 0

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_choose_area, container, false)
        adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, dataList)
        list_view = view.findViewById<ListView>(R.id.list_view)
        list_view.adapter = adapter
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        list_view.setOnItemClickListener({ parent, view, position, id ->
            if (currentLevel == LEVEL_PROVINCE) {
                selectedProvince = provinceList[position]
                queryCities()
            } else if (currentLevel == LEVEL_CITY) {
                selectedCity = cityList[position]
                queryCountries()
            } else if (currentLevel == LEVEL_COUNTRY) {
                val weatherId = countryList[position].weatherId.toString()
                if (activity is  WeatherActivity) {
                    val intent = Intent(activity, WeatherInfoActivity::class.java)
                    intent.putExtra("weather_id", weatherId)
                    startActivity(intent)
                    activity.finish()
                } else if (activity is WeatherInfoActivity) {
                    val activity = activity as WeatherInfoActivity
                    activity.closeDrawer()
                    activity.refresh()
                    activity.requestWeather(weatherId)
                }

            }
        })
        back_button.setOnClickListener({
            if (currentLevel == LEVEL_COUNTRY) {
                queryCities()
            } else if (currentLevel == LEVEL_CITY) {
                queryProvince()
            }
        })
        txt_clear_data.setOnClickListener {
            DataSupport.deleteAll(Province::class.java)
            DataSupport.deleteAll(City::class.java)
            DataSupport.deleteAll(Country::class.java)
        }
        queryProvince()
    }


    private fun queryProvince() {
        title_text.text = "中国"
        back_button.visibility = View.GONE
        provinceList = DataSupport.findAll(Province::class.java)
        if (provinceList.isNotEmpty()) {
            dataList.clear()
            for (province in provinceList) {
                dataList.add(province.provinceName)
            }
            adapter.notifyDataSetChanged()
            list_view.setSelection(0)
            currentLevel = LEVEL_PROVINCE
        } else {
            val address = "http://guolin.tech/api/china"
            queryFromServer(address, "province")
        }
    }

    internal fun queryCities() {
        title_text.text = selectedProvince.provinceName
        back_button.visibility = View.VISIBLE
        cityList = DataSupport.where("provinceid = ?", selectedProvince.id.toString()).find(City::class.java)
        if (cityList.isNotEmpty()) {
            dataList.clear()
            for (city in cityList) {
                dataList.add(city.cityName)
            }
            adapter.notifyDataSetChanged()
            list_view.setSelection(0)
            currentLevel = LEVEL_CITY
        } else {
            val provinceCode = selectedProvince.provinceCode
            val address = "http://guolin.tech/api/china/" + provinceCode
            queryFromServer(address, "city")
        }
    }

    internal fun queryCountries() {
        title_text.text = selectedCity.cityName
        back_button.visibility = View.VISIBLE
        countryList = DataSupport.where("cityid = ?", selectedCity.id.toString()).find(Country::class.java)
        if (countryList.isNotEmpty()) {
            dataList.clear()
            for (country in countryList) {
                dataList.add(country.countryName)
            }
            adapter.notifyDataSetChanged()
            list_view.setSelection(0)
            currentLevel = LEVEL_COUNTRY
        } else {
            val provinceCode = selectedProvince.provinceCode
            val cityCode = selectedCity.cityCode
            val address = "http://guolin.tech/api/china/$provinceCode/$cityCode"
            queryFromServer(address, "country")
        }
    }

    internal fun queryFromServer(address: String, type: String) {
        showProgressDialog()
        HttpTool.get(address, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                activity.runOnUiThread {
                    closeProgressDialog()
                    Toast.makeText(context, "Load Fail", Toast.LENGTH_SHORT).show()
                }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val responseText = response.body()!!.string()
                var result = false
                if ("province" == type) {
                    result = Utility.handleProvinceResponse(responseText)
                } else if ("city" == type) {
                    result = Utility.handleCityResponse(responseText, selectedProvince.id)
                } else if ("country" == type) {
                    result = Utility.handleCountryResponse(responseText, selectedCity.id)
                }
                if (result) {
                    activity.runOnUiThread {
                        closeProgressDialog()
                        if ("province" == type) {
                            queryProvince()
                        } else if ("city" == type) {
                            queryCities()
                        } else if ("country" == type) {
                            queryCountries()
                        }
                    }

                }
            }
        })
    }

    internal fun showProgressDialog() {
        if (progressBar == null) {
            progressBar = ProgressBar(context)

        }

    }

    internal fun closeProgressDialog() {
//        if (progressDialog != null) {
//            progressDialog!!.dismiss()
//        }
    }
}
