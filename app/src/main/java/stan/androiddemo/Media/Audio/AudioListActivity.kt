package stan.androiddemo.Media.Audio

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_audio_list.*
import stan.androiddemo.R

class AudioListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_list)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = ""

        btn_float.setOnClickListener {
            val intent = Intent(this, AudioRecordActivity::class.java)
            startActivity(intent)
        }
    }
}
