package stan.androiddemo.project.Mito

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import stan.androiddemo.PageAdapter
import stan.androiddemo.R
import stan.androiddemo.project.Mito.Model.ImageCatInfo

class DynamicMitoActivity : AppCompatActivity() {

    lateinit var mAdapter: PageAdapter
    var currentSelectCat = 0
    var imageCat: ImageCatInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dynamic_mito)
    }
}
