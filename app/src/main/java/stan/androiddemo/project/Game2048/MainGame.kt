package stan.androiddemo.project.Game2048

import android.content.Context
import android.media.AudioManager
import android.media.SoundPool
import stan.androiddemo.R
import java.util.HashMap

/**
 * Created by hugfo on 2017/8/19.
 */
class MainGame{
    val SPAWN_ANIMATION = -1
    val MOVE_ANIMATION = 0
    val MERGE_ANIMATION = 1

    val FADE_GLOBAL_ANIMATION = 0

    val MOVE_ANIMATION_TIME = MainView.BASE_ANIMATION_TIME
    val SPAWN_ANIMATION_TIME = MainView.BASE_ANIMATION_TIME
    val NOTIFICATION_ANIMATION_TIME = MainView.BASE_ANIMATION_TIME * 5
    val NOTIFICATION_DELAY_TIME = MOVE_ANIMATION_TIME + SPAWN_ANIMATION_TIME

    private val HIGH_SCORE = "high score"

    val startingMaxValue = 2048
    val endingMaxValue = 32768

    // Odd state = game is not active
    // Even state = game is active
    // Win state = active state + 1
    val GAME_WIN = 1
    val GAME_LOST = -1
    val GAME_NORMAL = 0
    val GAME_NORMAL_WON = 1
    val GAME_ENDLESS = 2
    val GAME_ENDLESS_WON = 3

    var grid: Grid? = null
    var aGrid: AnimationGrid
    internal val numSquaresX = 4
    internal val numSquaresY = 4
    internal val startTiles = 2

    var gameState = 0
    var canUndo: Boolean = false

    var score: Long = 0
    var highScore: Long = 0

    var lastScore: Long = 0
    var lastGameState = 0

    private var bufferScore: Long = 0
    private var bufferGameState = 0

    private var soudPool: SoundPool? = null
    private var spMap: HashMap<Int, Int>? = null

    private val mContext: Context

    private val mView: MainView

    constructor(context: Context,view:MainView){
        mContext = context
        mView = view

    }

    fun initSoundPool(){
        soudPool = SoundPool(5, AudioManager.STREAM_MUSIC, 0)
        spMap = HashMap<Int, Int>()
        spMap.put(1, soudPool.load(mView.context, R.raw.sfx_wing, 1)) // slide
        spMap.put(2, soudPool.load(mView.context, R.raw.sfx_point, 1)) // get
        // point
        spMap.put(3, soudPool.load(mView.context, R.raw.sfx_swooshing, 1)) // swoosh
        spMap.put(4, soudPool.load(mView.context, R.raw.die, 1)) // die
        spMap.put(5, soudPool.load(mView.context, R.raw.win, 1)) // win

    }
}