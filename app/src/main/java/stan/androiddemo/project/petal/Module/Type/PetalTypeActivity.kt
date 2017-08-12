package stan.androiddemo.project.petal.Module.Type

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_petal_type.*
import stan.androiddemo.R
import stan.androiddemo.project.petal.Base.BasePetalActivity
import stan.androiddemo.project.petal.Module.PetalList.PetalListFragment

class PetalTypeActivity : BasePetalActivity() {

    lateinit var petalTitle:String
    lateinit var petaltype:String

    override fun getTag(): String { return this.toString() }
    override fun getLayoutId(): Int { return R.layout.activity_petal_type  }


    companion object {
        fun launch(activity:Activity,title:String,type:String){
            val intent = Intent(activity,PetalTypeActivity::class.java)
            intent.putExtra("title",title)
            intent.putExtra("type",type)
            activity.startActivity(intent)
        }
        fun launch(activity:Activity){
            val intent = Intent(activity,PetalTypeActivity::class.java)
            activity.startActivity(intent)
        }
        fun launch(activity:Activity,flag:Int){
            val intent = Intent(activity,PetalTypeActivity::class.java)
            intent.flags = flag
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        petalTitle = intent.getStringExtra("title")
        petaltype = intent.getStringExtra("type")
        this.title = petalTitle

        val fragment = PetalListFragment.createListFragment(petaltype,petalTitle)

        supportFragmentManager.beginTransaction().replace(R.id.frame_layout_petal_with_refresh,fragment).commit()
    }

}
