package stan.androiddemo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_layout.*
import stan.androiddemo.layout.constraintLayout.ConstraintLayoutDemoActivity
import stan.androiddemo.layout.keyboard.KeyboardActivity

class LayoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout)

        val adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,
                arrayListOf("键盘事件","ConstraintLayout布局"))
        list_view_layout.adapter = adapter

        list_view_layout.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val title = (view as TextView).text
            when(title){
                "键盘事件"->
                {
                    val intent = Intent(this, KeyboardActivity::class.java)
                    startActivity(intent)
                }
                "ConstraintLayout布局"->
                {
                    val intent = Intent(this, ConstraintLayoutDemoActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}
