package stan.androiddemo.project.Game2048

/**
 * Created by hugfo on 2017/8/20.
 */
class AnimationCell(x: Int, y: Int, var animationType: Int, length: Long, delay: Long, var extras: Array<Int>?) : Cell(x, y) {
    private var timeElapsed: Long = 0
    private var animationTime: Long = length
    private var delayTime: Long = delay

    fun tick(timeElapsed: Long) {
        this.timeElapsed = this.timeElapsed + timeElapsed
    }

    fun animationDone(): Boolean {
        return animationTime + delayTime < timeElapsed
    }

    fun getPercentageDone(): Double {
        return Math.max(0.0, 1.0 * (timeElapsed - delayTime) / animationTime)
    }

    fun isActive(): Boolean {
        return timeElapsed >= delayTime
    }
}