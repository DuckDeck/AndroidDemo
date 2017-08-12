package stan.androiddemo.project.petal.Module.Search

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.jakewharton.rxbinding.widget.RxTextView
import kotlinx.android.synthetic.main.activity_search_petal.*
import licola.demo.com.huabandemo.Module.Search.SearcHHintAdapter
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Func1
import rx.schedulers.Schedulers
import stan.androiddemo.R
import stan.androiddemo.UI.FlowLayout
import stan.androiddemo.project.petal.API.SearchAPI
import stan.androiddemo.project.petal.Base.BasePetalActivity
import stan.androiddemo.project.petal.Config.Config
import stan.androiddemo.project.petal.HttpUtiles.RetrofitClient
import stan.androiddemo.project.petal.Module.Type.PetalTypeActivity
import stan.androiddemo.tool.CompatUtils
import stan.androiddemo.tool.Logger
import stan.androiddemo.tool.SPUtils
import stan.androiddemo.tool.Utils
import java.util.concurrent.TimeUnit

class SearchPetalActivity : BasePetalActivity() {
    lateinit var mAdapter:ArrayAdapter<String>
    val arrListHttpHint = ArrayList<String>()
    var itemWidth = 0
    companion object {
        fun launch(activity:Activity){
            val intent = Intent(activity,SearchPetalActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun getTag(): String { return this.toString()    }

    override fun getLayoutId(): Int {
       return R.layout.activity_search_petal
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        image_btn_clear_history.setImageDrawable(CompatUtils.getTintListDrawable(mContext,R.drawable.ic_close_black_24dp,R.color.tint_list_grey))
        initFlowReference(flow_search_reference_petal)
        mAdapter = SearcHHintAdapter(mContext,android.R.layout.simple_spinner_dropdown_item,arrListHttpHint)
        auto_txt_search_petal.setAdapter(mAdapter)
        initHintHttp()

        auto_txt_search_petal.setOnItemClickListener { _, _, i, _ ->
            Logger.d(arrListHttpHint[i])
            SearchPetalResultActivity.launch(this,arrListHttpHint[i])
        }

        RxTextView.editorActions(auto_txt_search_petal, Func1 {
            it == EditorInfo.IME_ACTION_SEARCH
        }).throttleFirst(500,TimeUnit.MILLISECONDS)
                .subscribe {
                   initActionSearch()
                }
        initClearHistory()
    }

    override fun onResume() {
        super.onResume()
        initFLoaHistory(flow_search_history_petal)
    }

    fun initFLoaHistory(flowHistory:FlowLayout){
        flowHistory.removeAllViews()
        val txtList = SPUtils.get(mContext,Config.HISTORYTEXT,HashSet<String>()) as HashSet<String>
        if (!txtList.isEmpty()){
            for (txt in txtList){
                addHistoryChildText(flowHistory,txt)
            }
        }
        else{
            addChildNoHistoryTip(flowHistory,resources.getString(R.string.hint_not_history))
        }
    }

    fun addHistoryChildText(flowHistory: FlowLayout,str: String){
        val txt = LayoutInflater.from(mContext).inflate(R.layout.petal_txt_history_view_item,flowHistory,false) as TextView
        txt.text = str
        flowHistory.addView(txt)
        txt.setOnClickListener {
            SearchPetalResultActivity.launch(this,str)
        }
    }

    fun addChildNoHistoryTip(flowHistory: FlowLayout,str: String){
        val txtTip = TextView(mContext)
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.gravity = Gravity.CENTER
        layoutParams.setMargins(10, 10, 10, 10)
        txtTip.text = str
        txtTip.layoutParams = layoutParams
        txtTip.gravity = Gravity.CENTER_HORIZONTAL
        flowHistory.addView(txtTip)
    }

    fun initFlowReference(flowHistory: FlowLayout){
        val txtList = resources.getStringArray(R.array.title_array_all)
        val typeList = resources.getStringArray(R.array.type_array_all)
        itemWidth = Utils.getScreenWidth(mContext) / 3 - 2
        for (i in 0 until txtList.size){
            addReferenceButton(flow_search_reference_petal,txtList[i],typeList[i],R.drawable.ic_loyalty_black_24dp)
        }
    }

    fun addReferenceButton(flowHistory: FlowLayout,str: String,type:String,resId:Int){
        val btn = Button(mContext)
        val layoutParams = LinearLayout.LayoutParams(itemWidth,ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.gravity = Gravity.CENTER
        layoutParams.setMargins(1,1,1,1)
        btn.setCompoundDrawablesRelativeWithIntrinsicBounds(null,
                CompatUtils.getTintListDrawable(mContext,resId,R.color.tint_list_pink)
        ,null,null)
        btn.text = str
        btn.setBackgroundColor(Color.WHITE)
        btn.tag = type
        btn.layoutParams = layoutParams
        btn.gravity = Gravity.CENTER
        btn.setOnClickListener {
            PetalTypeActivity.launch(this,str,type)
        }
        flowHistory.addView(btn)
    }

    fun initHintHttp(){
        RxTextView.textChanges(auto_txt_search_petal).observeOn(Schedulers.io())
                .filter { it.length > 0 }
                .debounce(300,TimeUnit.MICROSECONDS)
                .switchMap {
                    RetrofitClient.createService(SearchAPI::class.java).httpsSearHintBean(mAuthorization,it.toString())
                }
                .map { it.result }
                .filter { it != null && it.size > 0 }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({it->
                    run {
                        arrListHttpHint.clear()
                        arrListHttpHint.addAll(it!!)
                        mAdapter.notifyDataSetChanged()
                    }
                },{
                    err->
                    run {
                        err.printStackTrace()
                    }
                })

    }

    fun initActionSearch(){
        if (auto_txt_search_petal.text.length > 0){
            SearchPetalResultActivity.launch(this,auto_txt_search_petal.text.toString())
        }
    }

    fun initClearHistory(){
        image_btn_clear_history.setOnClickListener {
            flow_search_history_petal.removeAllViews()
            SPUtils.remove(mContext,Config.HISTORYTEXT)
            addChildNoHistoryTip(flow_search_history_petal,resources.getString(R.string.hint_not_history))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.petal_search_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.action_search->{
                initActionSearch()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
