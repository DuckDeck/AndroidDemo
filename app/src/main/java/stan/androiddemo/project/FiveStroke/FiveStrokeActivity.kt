package stan.androiddemo.project.FiveStroke

import android.content.Context
import android.media.Image
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.activity_five_stroke.*
import org.litepal.crud.DataSupport
import stan.androiddemo.Model.ResultInfo
import stan.androiddemo.R
import stan.androiddemo.project.FiveStroke.Model.FiveStrokeInfo
import java.net.URLEncoder
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager
import stan.androiddemo.tool.KeyboardTool


class FiveStrokeActivity : AppCompatActivity() {


    lateinit var mAdapter :BaseQuickAdapter<FiveStrokeInfo,BaseViewHolder>
    var arrLetters = ArrayList<FiveStrokeInfo>()
    lateinit var failView: View
    lateinit var loadingView: View
    var key = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_five_stroke)

        title = ""
        toolbar.setNavigationOnClickListener { onBackPressed() }

        arrLetters.addAll(ArrayList<FiveStrokeInfo>(DataSupport.findAll(FiveStrokeInfo::class.java)))
        mAdapter = object:BaseQuickAdapter<FiveStrokeInfo,BaseViewHolder>(R.layout.five_stroke_letter_item,arrLetters){
            override fun convert(helper: BaseViewHolder, item: FiveStrokeInfo) {
                helper.setText(R.id.txt_letter,item.letter)
                helper.setText(R.id.txt_letter_spell,item.spell)
                helper.setText(R.id.txt_letter_code,item.code)
                Glide.with(this@FiveStrokeActivity).load(item.decodeUrl).crossFade().into(helper.getView(R.id.img_letter_code))
                helper.getView<ImageView>(R.id.img_delete_letter).setOnClickListener {
                    DataSupport.deleteAll(FiveStrokeInfo::class.java,"letter = '" + item.letter+"'")
                    arrLetters.remove(item)
                    mAdapter.notifyDataSetChanged()
                }
            }
        }

        recycler_letters.layoutManager = LinearLayoutManager(this)
        recycler_letters.adapter = mAdapter
        loadingView = View.inflate(this,R.layout.list_loading_hint,null)
        failView = View.inflate(this,R.layout.list_empty_hint,null)
        failView.setOnClickListener {
            mAdapter.emptyView = loadingView
            searchLetter(key)
        }

        img_search.setOnClickListener {
            val keySearch = txt_search_lettter.text.toString().trim()
            if (keySearch.length <= 0){
                Toast.makeText(this,"搜索不能为空", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            for (s in keySearch){
                if(!s.toString().matches(Regex("[\u4e00-\u9fa5]"))){
                    Toast.makeText(this,"搜索的字符只能为中文", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
            }
            key = keySearch.replace(" ","")
            KeyboardTool.hideKeyboard(this)
            searchLetter(key)
        }

        txt_search_lettter.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH){
                KeyboardTool.hideKeyboard(this)
                val imm = textView.context.getSystemService(
                        Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (imm.isActive) {
                    imm.hideSoftInputFromWindow(textView.applicationWindowToken, 0)
                }
                val keySearch = txt_search_lettter.text.toString().trim()
                if (keySearch.length <= 0){
                    Toast.makeText(this,"搜索不能为空", Toast.LENGTH_LONG).show()
                    return@setOnEditorActionListener true
                }
                for (s in keySearch){
                    if(!s.toString().matches(Regex("[\u4e00-\u9fa5]"))){
                        Toast.makeText(this,"搜索的字符只能为中文", Toast.LENGTH_LONG).show()
                        return@setOnEditorActionListener true
                    }
                }
                key = keySearch.replace(" ","")
                searchLetter(key)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

    }
    //终于出来了，不容易啊，这个网络太TMD不爽了
    //我用抓包工具分析的是什么玩意，发现好像是UrlEncoded的字符串，然后再用网站看，发现是用GKB  Encoded的，不是UTF-8，然后还要设置好header才行，需要和网站设置成一样的
    //最后要删除Accept-Encoding这个头，不然HTML获取后不能解码，会变成乱码。
    //使用OK HTTP3 POST数据时 使用FormBody.Builder().add(body)这个时侯会再把body encode一次，而且是用UTF-8的，不全要求，所以需要自己来Encoded的，然后再用
    //RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),sb.toString()) 的就去来添加body
    // 真是坑爹啊，非常坑爹
    fun searchLetter(key:String){
        val encodeKey = URLEncoder.encode(key,"GBK")
        FiveStrokeInfo.letterCode(encodeKey,{result:ResultInfo ->
            runOnUiThread {
                if (result.code != 0) {
                    Toast.makeText(this,result.message, Toast.LENGTH_LONG).show()
                    mAdapter.emptyView = failView
                    return@runOnUiThread
                }
                val letters = result.data!! as ArrayList<FiveStrokeInfo>
                if (letters.size <= 0){
                    mAdapter.emptyView = failView
                    return@runOnUiThread
                }
                for (l in letters){
                    l.save()
                    arrLetters.add(0,l)
                }
                mAdapter.notifyDataSetChanged()
            }

        })
    }
}
