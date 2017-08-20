package stan.androiddemo.project.Game2048

import android.view.MotionEvent
import android.view.View

/**
 * Created by hugfo on 2017/8/19.
 */
class InputListener: View.OnTouchListener{

    private val SWIPE_MIN_DISTANCE = 0
    private val SWIPE_THRESHOLD_VELOCITY = 25
    private val MOVE_THRESHOLD = 250
    private val RESET_STARTING = 10

    private var x: Float = 0F
    private var y: Float = 0F
    private var lastdx: Float = 0F
    private var lastdy: Float = 0F
    private var previousX: Float = 0F
    private var previousY: Float = 0F
    private var startingX: Float = 0F
    private var startingY: Float = 0F
    private var previousDirection = 1
    private var veryLastDirection = 1
    private var hasMoved = false

//    lateinit var mView:MainView
    override fun onTouch(v: View, m: MotionEvent): Boolean {
     return true
    }

}