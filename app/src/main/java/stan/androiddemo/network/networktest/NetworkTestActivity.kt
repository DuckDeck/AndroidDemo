package stan.androiddemo.network.networktest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_network_test.*
import okhttp3.Call
import okhttp3.Response
import stan.androiddemo.R
import stan.androiddemo.tool.HttpTool
import java.io.IOException
import java.util.*

class NetworkTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network_test)
        title = ""
        setSupportActionBar(toolbar)

        val para = hashMapOf("coinId" to "1","accessToken" to "","languageId" to "1","productId" to "96")
        txt_test.setOnClickListener {
            txt_start.text = Date().toString()
            HttpTool.post("https://www.skybaymall.com/api/gate/goods/detail",
                    para,object  :okhttp3.Callback{
                override fun onFailure(call: Call?, e: IOException?) {

                }

                override fun onResponse(call: Call?, response: Response?) {
                    runOnUiThread {
                        txt_finish.text = Date().toString()
                    }
                }

            })

        }
    }
}
