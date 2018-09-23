package stan.androiddemo.UI

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.TextView
import stan.androiddemo.R
import android.annotation.SuppressLint
import org.litepal.util.LogUtil
import android.view.Gravity
import io.reactivex.schedulers.Schedulers
import io.reactivex.functions.Consumer
import io.reactivex.disposables.Disposable
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * TODO: document your custom view class.
 */
class DigitalTimer : TextView {

     var digitalTextSize: Float = 0f
     var digitalTextColor: Int = 0
    private var mCurrentSecond = 0 //当前秒
    private var hours: String? = null
    private var minutes: String? = null
    private var seconds: String? = null //展示的时分秒
    private var mDisposable: Disposable? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context,  attrs: AttributeSet) : super(context, attrs) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.DigitalTimer)
        digitalTextSize = a.getDimension(R.styleable.DigitalTimer_digitalTextSize, DEFAULT_TEXT_SIZE)
        digitalTextColor = a.getColor(R.styleable.DigitalTimer_digitalTextColor, DEFAULT_TEXT_COLOR)
        a.recycle()
        init()
    }

    //初始化
    private fun init() {
        textSize = digitalTextSize

        this.setTextColor(digitalTextColor)
        gravity = Gravity.CENTER
        setBackgroundColor(Color.TRANSPARENT)
    }

    //开始计时
    fun start() {

        mDisposable = Observable.interval(1, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mCurrentSecond++
                    val hour = mCurrentSecond / 3600
                    val minute = mCurrentSecond % 3600 / 60
                    val second = mCurrentSecond % 60

                    seconds = if (second < 10) { //处理秒
                        "0$second"
                    } else {
                        second.toString()
                    }

                    minutes = if (minute < 10) { //处理分
                        "0$minute"
                    } else {
                        minute.toString()
                    }

                    hours = if (hour < 10) { //处理小时
                        "0$hour"
                    } else {
                        hour.toString()
                    }

                    //设置数据
                    text = String.format("%s:%s:%s", hours, minutes, seconds)
                }) { throwable -> LogUtil.d("自定义计时器数据异常------" + throwable.message,throwable.localizedMessage) }

    }

    //停止计时
    fun stop() {
        LogUtil.d("当前计时时长----->",text.toString())
        if (mDisposable != null) {
            mDisposable!!.dispose()
        }
    }

    //重置数据
    @SuppressLint("SetTextI18n")
    fun reset() {
        mCurrentSecond = 0
        text = "00:00:00"
        stop()
    }

    companion object {
        private val DEFAULT_TEXT_SIZE = 12f
        private val DEFAULT_TEXT_COLOR = Color.WHITE
    }

}
