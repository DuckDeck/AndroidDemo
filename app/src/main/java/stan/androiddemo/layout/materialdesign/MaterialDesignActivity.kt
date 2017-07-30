package stan.androiddemo.layout.materialdesign

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_material_design.*
import stan.androiddemo.R

class MaterialDesignActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_material_design)
        title = ""
        setSupportActionBar(toolbar)

    }

    //图标不能用太大的，显示不出来，还会变形
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.menu_delete->{
                Toast.makeText(this,"You click delete",Toast.LENGTH_LONG).show()
            }
            R.id.menu_save->{
                Toast.makeText(this,"You click menu_save",Toast.LENGTH_LONG).show()
            }
            R.id.menu_setting->{
                Toast.makeText(this,"You click menu_setting",Toast.LENGTH_LONG).show()
            }
        }
        return true
    }
}
