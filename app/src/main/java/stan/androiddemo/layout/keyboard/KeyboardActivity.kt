package stan.androiddemo.layout.keyboard

import KeyBoardListener
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_keyboard.*
import stan.androiddemo.R



class KeyboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keyboard)
        KeyBoardListener.setListener(this, object:KeyBoardListener.OnSoftKeyBoardChangeListener{
            override fun keyBoardHide(height: Int) {
                txt_event_status.text="键盘收起"
            }

            override fun keyBoardShow(height: Int) {
                txt_event_status.text="键盘收弹出"
            }

        })


    }
    // view the http://blog.csdn.net/yxhuang2008/article/details/53822072 ,
    // it suggest that you can custom a editview
}

//now work
class TextEditTextView : EditText {
    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}


    override fun onKeyPreIme(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.action === 1) {
            //it looks like not work for me, this event can not active
            Log.i("main_activity", "键盘向下 ")
            super.onKeyPreIme(keyCode, event)
            return false
        }
        return super.onKeyPreIme(keyCode, event)
    }
}