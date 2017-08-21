package stan.androiddemo.project.Game2048


import kotlin.collections.ArrayList

/**
 * Created by hugfo on 2017/8/19.
 */
class AnimationGrid{
    var field: Array<Array<ArrayList<AnimationCell>?>>
    internal var activeAnimations = 0
    internal var oneMoreFrame = false
    var globalAnimation = ArrayList<AnimationCell>()

    constructor(x:Int,y:Int){
        field = Array(x) { arrayOfNulls<ArrayList<AnimationCell>>(y) }
        for (i in 0 until x){
            for (j in 0 until y){
                field[i][j] = ArrayList<AnimationCell>()
            }
        }
    }

    fun startAnimation(x: Int, y: Int, animationType: Int, length: Long,
                       delay: Long, extras: Array<Int>?) {
        val animationToAdd = AnimationCell(x, y, animationType,
                length, delay, extras)
        if (x == -1 && y == -1) {
            globalAnimation.add(animationToAdd)
        } else {
            field[x][y]?.add(animationToAdd)
        }
        activeAnimations += 1
    }

    fun tickAll(timeElapsed: Long) {
        val cancelledAnimations = java.util.ArrayList<AnimationCell>()
        for (animation in globalAnimation) {
            animation.tick(timeElapsed)
            if (animation.animationDone()) {
                cancelledAnimations.add(animation)
                activeAnimations = activeAnimations - 1
            }
        }

        for (array in field) {
            for (list in array) {
                for (animation in list!!) {
                    animation.tick(timeElapsed)
                    if (animation.animationDone()) {
                        cancelledAnimations.add(animation)
                        activeAnimations = activeAnimations - 1
                    }
                }
            }
        }

        for (animation in cancelledAnimations) {
            cancelAnimation(animation)
        }
    }

    fun isAnimationActive(): Boolean {
        if (activeAnimations != 0) {
            oneMoreFrame = true
            return true
        } else if (oneMoreFrame) {
            oneMoreFrame = false
            return true
        } else {
            return false
        }
    }

    fun getAnimationCell(x: Int, y: Int): java.util.ArrayList<AnimationCell> {
        return field[x][y]!!
    }

    fun cancelAnimations() {
        for (array in field) {
            for (list in array) {
                list?.clear()
            }
        }
        globalAnimation.clear()
        activeAnimations = 0
    }

    fun cancelAnimation(animation: AnimationCell) {
        if (animation.x === -1 && animation.y === -1) {
            globalAnimation.remove(animation)
        } else {
            field[animation.x][animation.y]?.remove(animation)
        }
    }
}