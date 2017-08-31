package stan.androiddemo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.facebook.drawee.backends.pipeline.Fresco
import com.tencent.bugly.crashreport.CrashReport
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.lang.reflect.Field


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this)


        CrashReport.initCrashReport(applicationContext,"06c540ec45",true)

        setContentView(R.layout.activity_main)

        title = "" // it works
        setSupportActionBar(toolbar_main)

        val adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,
                arrayListOf("项目Demo","布局Layout","绘制View","线程Thread","通信IPC","存储Store","网络通信Network"))
        list_view_menu.adapter = adapter

        list_view_menu.onItemClickListener = AdapterView.OnItemClickListener { _, view, _, _ ->
            val title = (view as TextView).text
            when(title){
                "项目Demo"->
                {
                    val intent = Intent(this, ProjectActivity::class.java)
                    startActivity(intent)
                }
                "布局Layout"->
                {
                    val intent = Intent(this, LayoutActivity::class.java)
                    startActivity(intent)
                }
                "绘制View"->
                {
                    val intent = Intent(this, DrawViewActivity::class.java)
                    startActivity(intent)
                }
                "线程Thread"->
                {
                    val intent = Intent(this, ThreadActivity::class.java)
                    startActivity(intent)
                }
                "通信IPC"->
                {
                    val intent = Intent(this, CommunicationActivity::class.java)
                    startActivity(intent)
                }
                "存储Store"->
                {
                    val intent = Intent(this, StoreActivity::class.java)
                    startActivity(intent)
                }
                "网络通信Network"->{
                    val intent = Intent(this, NetworkActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        play()
    }

    fun play(){
        val strs = listOf("a", "bc", "def")
        println(strs.map(String::length))

        val j = JSONObject()
        j.put("testStr","this is a test")

        val s = A(j)
        println(s.sString)
        val reg = Regex("\\D+")
        val st = "123123wrer".replace(reg,"")

    }
}


open class BaseModel{
    constructor(){
        val t = this::class.java
        println(t.declaredFields)

        if (!TypeMap.selfMap.containsKey(t.name)){
            TypeMap.selfMap[t.name] = ReflectType()
        }
        if (TypeMap.selfMap[t.name]!!.className.length > 0){
            return
        }

        for (f in t.declaredFields){
            TypeMap.selfMap[t.name]!!.className = f.name
            TypeMap.selfMap[t.name]!!.properties[f.name] = f
        }

    }

     constructor(jsonObject: JSONObject):this(){
        val t = this::class.java
        val ty = TypeMap.selfMap[t.name]
        for (s in selfMapDesc!!){
            if (jsonObject.has(s.key))
            {
                val f = TypeMap.selfMap[t.name]!!.properties[s.value]
                f!!.isAccessible = true
                f!!.set(this,jsonObject.opt(s.key))
                println(this)
                // it looks like set the value success ,but the return constructor return the init instance which is not the set value instance
                // so the set value process fail
                // report error cannot access private  field java.lang.String stan.androiddemo.A.sString of class java.lang.Class<stan.androiddemo.A>
            }
        }
    }

    //对于selfMapDesc key是json的 value是 自己的
   open var selfMapDesc:HashMap<String,String>? = null

}

class A: BaseModel {
//    var iInt = 0
   open var sString = ""

    override var selfMapDesc: HashMap<String, String>?
        get() = hashMapOf("testStr" to "sString")
        set(value) {}

    constructor():super()

    constructor(jsonObject: JSONObject):super(jsonObject){}
}

class ReflectType{
    var className = ""
    var properties = HashMap<String,Field>()
}

class TypeMap{
    companion object {
        var selfMap = HashMap<String,ReflectType>()
    }
}