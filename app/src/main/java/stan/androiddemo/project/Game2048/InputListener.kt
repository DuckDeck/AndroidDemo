package stan.androiddemo.project.Game2048

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View

/**
 * Created by hugfo on 2017/8/19.
 */
class InputListener(view: MainView) : View.OnTouchListener{

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
    internal var mView: MainView = view

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                x = event.x
                y = event.y
                startingX = x
                startingY = y
                previousX = x
                previousY = y
                lastdx = 0f
                lastdy = 0f
                hasMoved = false
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                x = event.x
                y = event.y
                if (mView.game.isActive()) {
                    val dx = x - previousX
                    if (Math.abs(lastdx + dx) < Math.abs(lastdx) + Math.abs(dx)
                            && Math.abs(dx) > RESET_STARTING
                            && Math.abs(x - startingX) > SWIPE_MIN_DISTANCE) {
                        startingX = x
                        startingY = y
                        lastdx = dx
                        previousDirection = veryLastDirection
                    }
                    if (lastdx == 0f) {
                        lastdx = dx
                    }
                    val dy = y - previousY
                    if (Math.abs(lastdy + dy) < Math.abs(lastdy) + Math.abs(dy)
                            && Math.abs(dy) > RESET_STARTING
                            && Math.abs(y - startingY) > SWIPE_MIN_DISTANCE) {
                        startingX = x
                        startingY = y
                        lastdy = dy
                        previousDirection = veryLastDirection
                    }
                    if (lastdy == 0f) {
                        lastdy = dy
                    }
                    if (pathMoved() > SWIPE_MIN_DISTANCE * SWIPE_MIN_DISTANCE) {
                        var moved = false
                        if ((dy >= SWIPE_THRESHOLD_VELOCITY && previousDirection == 1 || y - startingY >= MOVE_THRESHOLD) && previousDirection % 2 != 0) {
                            moved = true
                            previousDirection *= 2
                            veryLastDirection = 2
                            mView.game.move(2)
                        } else if ((dy <= -SWIPE_THRESHOLD_VELOCITY && previousDirection == 1 || y - startingY <= -MOVE_THRESHOLD) && previousDirection % 3 != 0) {
                            moved = true
                            previousDirection *= 3
                            veryLastDirection = 3
                            mView.game.move(0)
                        } else if ((dx >= SWIPE_THRESHOLD_VELOCITY && previousDirection == 1 || x - startingX >= MOVE_THRESHOLD) && previousDirection % 5 != 0) {
                            moved = true
                            previousDirection *= 5
                            veryLastDirection = 5
                            mView.game.move(1)
                        } else if ((dx <= -SWIPE_THRESHOLD_VELOCITY && previousDirection == 1 || x - startingX <= -MOVE_THRESHOLD) && previousDirection % 7 != 0) {
                            moved = true
                            previousDirection *= 7
                            veryLastDirection = 7
                            mView.game.move(3)
                        }
                        if (moved) {
                            hasMoved = true
                            startingX = x
                            startingY = y
                        }
                    }
                }
                previousX = x
                previousY = y
                return true
            }
            MotionEvent.ACTION_UP -> {
                x = event.x
                y = event.y
                previousDirection = 1
                veryLastDirection = 1
                // "Menu" inputs
                if (!hasMoved) {
                    if (iconPressed(mView.sXNewGame, mView.sYIcons)) {
                        mView.game.newGame()
                    } else if (iconPressed(mView.sXUndo, mView.sYIcons)) {
                        mView.game.revertUndoState()
                    } else if (iconPressed(mView.sXCheat, mView.sYIcons)) {
                        mView.game.cheat()
                    } else if (isTap(2)
                            && inRange(mView.startingX.toFloat(), x, mView.endingX.toFloat())
                            && inRange(mView.startingY.toFloat(), x, mView.endingY.toFloat())
                            && mView.continueButtonEnabled) {
                        mView.game.setEndlessMode()
                    }
                }
            }
        }
        return true
    }

    private fun pathMoved(): Float {
        return (x - startingX) * (x - startingX) + (y - startingY) * (y - startingY)
    }

    private fun iconPressed(sx: Int, sy: Int): Boolean {
        return isTap(1) && inRange(sx.toFloat(), x, sx + mView.iconSize.toFloat())
                && inRange(sy.toFloat(), y, sy + mView.iconSize.toFloat())
    }

    private fun inRange(starting: Float, check: Float, ending: Float): Boolean {
        return check in starting..ending
    }

    private fun isTap(factor: Int): Boolean {
        return pathMoved() <= mView.iconSize * factor
    }

}