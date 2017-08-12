package stan.androiddemo.project.petal.Module.Search

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_search_petal_result.*
import stan.androiddemo.R
import stan.androiddemo.project.petal.Base.BasePetalActivity

class SearchPetalResultActivity : BasePetalActivity() {


    lateinit var searchKey :String

    override fun getTag(): String {
        return  this.toString()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_search_petal_result
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
    }

}
