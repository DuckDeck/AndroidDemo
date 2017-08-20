package stan.androiddemo.project.Game2048

/**
 * Created by hugfo on 2017/8/20.
 */
class AnimationCell:Cell{
    var animationType: Int = 0
    private var timeElapsed: Long = 0
    private var animationTime: Long = 0
    private var delayTime: Long = 0
     var extras: Array<Int>?

    constructor(x:Int,y:Int,animationType:Int,length:Long,delay:Long,extras:Array<Int>?):super(x,y){
        this.animationType = animationType
        this.animationTime = animationTime
        this.delayTime = delay
        this.extras = extras
    }

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