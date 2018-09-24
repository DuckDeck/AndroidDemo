package stan.androiddemo.UI

import android.content.Context
import android.graphics.Canvas
import android.widget.ImageButton

class BaseImageButton:ImageButton{


    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)

    }


    constructor(context: Context) : super(context) {

    }

    override fun onDraw(canvas: Canvas?) {
        
        super.onDraw(canvas)

    }
}