package stan.androiddemo.project.novel

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.BaseAdapter
import android.widget.Toast
import com.ineat.quickadapter.QuickAdapter
import kotlinx.android.synthetic.main.activity_novel_search.*
import stan.androiddemo.R
import stan.androiddemo.project.novel.model.NovelInfo

class NovelSearchActivity : AppCompatActivity() {

    lateinit var mAdapter:QuickAdapter<NovelInfo>
    var arrNovels = ArrayList<NovelInfo>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novel_search)

        btn_search_start.setOnClickListener {
            val key = txt_search_input.text.toString().trim()
            if (key.length <= 0){
                Toast.makeText(this,"搜索条件不能为空",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            NovelInfo.search(key,{v:ArrayList<NovelInfo>->
                println(v)
               return@search 1
            })
            //it looks like  it's not QuickAdapter,it's basequickadapter, alter it tomorrow



        }

    }

}
