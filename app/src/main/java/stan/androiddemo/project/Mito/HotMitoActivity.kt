package stan.androiddemo.project.Mito
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.activity_hot_mito.*
import stan.androiddemo.R
import stan.androiddemo.project.Mito.Model.ImageSetInfo

class HotMitoActivity : AppCompatActivity() {


    var arrImageSet = ArrayList<ImageSetInfo>()
    lateinit var mAdapter: BaseQuickAdapter<ImageSetInfo, BaseViewHolder>
    lateinit var failView: View
    lateinit var loadingView: View
    lateinit var progressLoading: Drawable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hot_mito)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }
}
