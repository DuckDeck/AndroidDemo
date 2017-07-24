package stan.androiddemo.drawview.drawtext

import android.content.Context
import android.graphics.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import stan.androiddemo.R
import java.util.*

class DrawTextActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw_text)
    }
}

internal class DrawTextView : android.support.v7.widget.AppCompatImageView {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}


    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        val paint = Paint()
        paint.textSize = 50F
        paint.typeface = Typeface.MONOSPACE //字体
        paint.isFakeBoldText = true  //粗体
        paint.isStrikeThruText = true//中间划线
        paint.isUnderlineText = true//下面划线
        paint.textSkewX = 0.5F//文字倾斜
        paint.textScaleX = 1.4F//变大变小
        paint.letterSpacing = 0.2F//文字间隔
        paint.fontFeatureSettings = "smcp"
        paint.textLocale = Locale.CHINA
        canvas.drawText("今天要下雨了，还是小罗爽",100F,100F,paint)
        paint.textLocale = Locale.TAIWAN
        canvas.drawText("今天要下雨了，还是小罗爽",100F,200F,paint)
        paint.textLocale = Locale.JAPAN
        canvas.drawText("今天要下雨了，还是小罗爽",100F,300F,paint)
    }
}
