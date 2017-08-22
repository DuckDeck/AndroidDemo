package stan.androiddemo.project.Game2048

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioManager
import android.media.SoundPool
import android.preference.PreferenceManager
import stan.androiddemo.R
import java.util.*

/**
 * Created by hugfo on 2017/8/19.
 */
class MainGame(context: Context, view: MainView) {

    companion object {
        val SPAWN_ANIMATION = -1
        val MOVE_ANIMATION = 0
        val MERGE_ANIMATION = 1

        val FADE_GLOBAL_ANIMATION = 0

        val MOVE_ANIMATION_TIME:Long = MainView.BASE_ANIMATION_TIME
        val SPAWN_ANIMATION_TIME:Long = MainView.BASE_ANIMATION_TIME
        val NOTIFICATION_ANIMATION_TIME:Long = MainView.BASE_ANIMATION_TIME * 5
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
    }

    var grid: Grid? = null
    var aGrid: AnimationGrid? = null
    internal val numSquaresX = 4
    internal val numSquaresY = 4
    internal val startTiles = 2
    var playerTurn = true
    var gameState = 0
    var canUndo: Boolean = false

    var score: Long = 0
    var highScore: Long = 0

    var lastScore: Long = 0
    var lastGameState = 0

    private var bufferScore: Long = 0
    private var bufferGameState = 0

    lateinit var soudPool: SoundPool
    lateinit var spMap: HashMap<Int, Int>

    private val mContext: Context = context

    private val mView: MainView = view

    fun newGame() {
        playSound(3, 1)
        if (grid == null) {
            grid = Grid(numSquaresX, numSquaresY)
        } else {
            prepareUndoState()
            saveUndoState()
            grid?.clearGrid()
        }
        aGrid = AnimationGrid(numSquaresX, numSquaresY)
        highScore = getStoredHighScore()
        if (score >= highScore) {
            highScore = score
            recordHighScore()
        }
        score = 0
        gameState = GAME_NORMAL
        addStartTiles()
        mView.refreshLastTime = true
        mView.resyncTime()
        mView.invalidate()
    }

    //添加开始游戏的Tile
    private fun addStartTiles() {
        for (xx in 0..startTiles - 1) {
            this.addRandomTile()
        }
    }

    //随机添加Tile
    private fun addRandomTile() {
        if (grid!!.isCellsAvailable()) { //如果可以添加
            val value = if (Math.random() < 0.9) 2 else 4 //添加2或者4的Tile   2 出现的可能性为9成
            val tile = Tile(grid!!.randomAvailableCell()!!, value) //从随机可以出现Tile的地方实例化Tile
            spawnTile(tile)  //添加Tile
        }
    }

    private fun spawnTile(tile: Tile) {
        grid?.insertTile(tile)
        aGrid?.startAnimation(tile.x, tile.y, SPAWN_ANIMATION,
                SPAWN_ANIMATION_TIME, MOVE_ANIMATION_TIME, null) // Direction:
        // -1 =
        // EXPANDING
    }

    @SuppressLint("ApplySharedPref")
    private fun recordHighScore() {
        val settings = PreferenceManager
                .getDefaultSharedPreferences(mContext)
        val editor = settings.edit()
        editor.putLong(HIGH_SCORE, highScore)
        editor.commit()
    }

    private fun getStoredHighScore(): Long {
        val settings = PreferenceManager
                .getDefaultSharedPreferences(mContext)
        return settings.getLong(HIGH_SCORE, -1)
    }

    private fun prepareTiles() {
        for (array in grid!!.field) {
            array.filter { grid!!.isCellOccupied(it as Cell?) }
                    .forEach { it?.mergedFrom = null }
        }
    }

    private fun moveTile(tile: Tile, cell: Cell) {
        grid!!.field[tile.x][tile.y] = null
        grid!!.field[cell.x][cell.y] = tile
        tile.updatePosition(cell)
    }

    private fun saveUndoState() {
        grid?.saveTiles()
        canUndo = true
        lastScore = bufferScore
        lastGameState = bufferGameState
    }

    // cheat remove 2
    fun cheat() {
        playSound(3, 1)
        val notAvailableCell = grid?.getNotAvailableCells()
        var tile: Tile
        prepareUndoState()
        for (cell in notAvailableCell!!) {
            tile = grid!!.getCellContent(cell)!!
            if (2 == tile.value) {
                grid?.removeTile(tile)
            }
        }

        if (grid!!.getNotAvailableCells().size === 0) {
            addStartTiles()
        }
        saveUndoState()
        mView.resyncTime()
        mView.invalidate()

    }

    private fun prepareUndoState() {
        grid?.prepareSaveTiles()
        bufferScore = score
        bufferGameState = gameState
    }

    fun revertUndoState() {
        playSound(3, 1)
        if (canUndo) {
            canUndo = false
            aGrid?.cancelAnimations()
            grid?.revertTiles()
            score = lastScore
            gameState = lastGameState
            mView.refreshLastTime = true
            mView.invalidate()
        }
    }

    fun gameWon(): Boolean {
        return gameState > 0 && gameState % 2 != 0
    }

    fun gameLost(): Boolean {
        return gameState == GAME_LOST
    }

    fun isActive(): Boolean {
        return !(gameWon() || gameLost())
    }

    fun move(direction: Int):Boolean {
        playSound(1, 1) // move sound
        aGrid?.cancelAnimations()
        // 0: up, 1: right, 2: down, 3: left
        if (!isActive()) {
            return false
        }
        prepareUndoState()
        val vector = getVector(direction)
        val traversalsX = buildTraversalsX(vector)
        val traversalsY = buildTraversalsY(vector)
        var moved = false

        prepareTiles()

        for (xx in traversalsX) {
            for (yy in traversalsY) {
                val cell = Cell(xx, yy)
                val tile = grid?.getCellContent(cell)

                if (tile != null) {
                    val positions = findFarthestPosition(cell, vector)
                    val next = grid?.getCellContent(positions[1])

                    if (next != null && next.value === tile!!.value
                            && next.mergedFrom == null) {
                        playSound(2, 1) // get ponit sound

                        val merged = Tile(positions[1],
                                tile.value * 2)
                        val temp = arrayOf(tile, next)
                        merged.mergedFrom = temp

                        grid?.insertTile(merged)
                        grid?.removeTile(tile)

                        // Converge the two tiles' positions
                        tile.updatePosition(positions[1])

                        val extras = arrayOf(xx,yy)
                        aGrid?.startAnimation(merged.x, merged.y,
                                MOVE_ANIMATION, MOVE_ANIMATION_TIME, 0, extras) // Direction:
                        // 0
                        // =
                        // MOVING
                        // MERGED
                        aGrid?.startAnimation(merged.x, merged.y,
                                MERGE_ANIMATION, SPAWN_ANIMATION_TIME,
                                MOVE_ANIMATION_TIME, null)

                        // Update the score
                        score += merged.value
                        highScore = Math.max(score, highScore)

                        // The mighty 2048 tile
                        if (merged.value >= winValue() && !gameWon()) {
                            gameState += GAME_WIN // Set win state
                            playSound(4, 1)
                            endGame()
                        }
                    } else {
                        moveTile(tile, positions[0])
                        val extras = arrayOf(xx, yy, 0)
                        aGrid?.startAnimation(positions[0].x,
                                positions[0].y, MOVE_ANIMATION,
                                MOVE_ANIMATION_TIME, 0, extras) // Direction: 1
                        // = MOVING
                        // NO MERGE
                    }

                    if (!positionsEqual(cell, tile)) {
                        moved = true
                        playerTurn = false
                    }
                }
            }
        }

        if (moved) {
            saveUndoState()
            addRandomTile()
            checkLose()
            playerTurn = true
        }
        mView.resyncTime()
        mView.invalidate()
        return moved
    }

    private fun checkLose() {
        if (!movesAvailable() && !gameWon()) {
            gameState = GAME_LOST
            endGame()
            playSound(5, 1)
        }
    }

    private fun endGame() {
        aGrid?.startAnimation(-1, -1, FADE_GLOBAL_ANIMATION,
                NOTIFICATION_ANIMATION_TIME, NOTIFICATION_DELAY_TIME, null)
        if (score >= highScore) {
            highScore = score
            recordHighScore()
        }
    }

    private fun getVector(direction: Int): Cell {
        val map = arrayOf(Cell(0, -1), // up
                Cell(1, 0), // right
                Cell(0, 1), // down
                Cell(-1, 0) // left
        )
        return map[direction]
    }

    private fun buildTraversalsX(vector: Cell): List<Int> {
        val traversals = (0..numSquaresX - 1).toList()

        if (vector.x === 1) {
            Collections.reverse(traversals)
        }

        return traversals
    }

    private fun buildTraversalsY(vector: Cell): List<Int> {
        val traversals = (0..numSquaresY - 1).toList()

        if (vector.y === 1) {
            Collections.reverse(traversals)
        }

        return traversals
    }

    private fun findFarthestPosition(cell: Cell, vector: Cell): Array<Cell> {
        var previous: Cell
        var nextCell = Cell(cell.x, cell.y)
        do {
            previous = nextCell
            nextCell = Cell(previous.x + vector.x,previous.y + vector.y)
        } while (grid!!.isCellWithinBounds(nextCell) && grid!!.isCellAvailable(nextCell))

        val answer = arrayOf(previous, nextCell)
        return answer
    }

    private fun movesAvailable(): Boolean {
        return grid!!.isCellsAvailable() || tileMatchesAvailable()
    }

    fun tileMatchesAvailable():Boolean{
        var tile:Tile? = null
        for (i in 0 until numSquaresX){
            for (j in 0 until numSquaresY){
               tile = grid?.getCellContent(i,j)
                if (tile != null) {
                    for (direction in 0..3) {
                        val vector = getVector(direction)
                        val cell = Cell(i + vector.x, j + vector.y)

                        val other = grid?.getCellContent(cell)

                        if (other != null && other.value === tile.value) {
                            return true
                        }
                    }
                }
            }
        }
        return false
    }
    fun positionsEqual(first:Cell,second:Cell):Boolean{
        return first.x == second.x && first.y == second.y
    }

    fun winValue():Int{
        if (canContinue()){
            return endingMaxValue
        }
        else{
            return startingMaxValue
        }
    }

    fun setEndlessMode(){
        gameState = GAME_ENDLESS
        mView.invalidate()
        mView.refreshLastTime = true
    }


    fun canContinue():Boolean{
        return !(gameState == GAME_ENDLESS || gameState == GAME_ENDLESS_WON)
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

    fun playSound(sound:Int,number:Int){
        val am = mView.context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC).toFloat()
        val audioCurrentVolumn = am.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()
        val volumnRatio = audioCurrentVolumn / audioMaxVolumn
        soudPool.play(spMap[sound]!!,volumnRatio,volumnRatio,1,number,1F)
    }

    init {
        initSoundPool()
    }

    //获取平滑度
    fun smoothness():Double{
        var smoothness = 0.0
        for (i in 0 until grid!!.field.size){
            for (j in 0 until grid!!.field[i].size){
                if (grid!!.isCellOccupied(grid!!.field[i][j])){ //如果这个格子不是空的
                    val value = Math.log(grid!!.field[i][j]!!.value.toDouble()) / Math.log(2.toDouble())
                    (1 until 3)
                            .map { getVector(it) }
                            .map { findFarthestPosition(grid!!.field[i][j]!!, it)[1] }
                            .filter { grid!!.isCellOccupied(it) }
                            .map { grid!!.getCellContent(it) }
                            .map { Math.log(it!!.value.toDouble()) / Math.log(2.toDouble()) }
                            .forEach { smoothness -= Math.abs(value - it) }
                }
            }
        }
        return smoothness
    }

    fun monotonly():Double{
        var totals = arrayListOf(0.0,0.0,0.0,0.0)
        //上下方向
        (0 until numSquaresX).map {
            var current = 0
            var next = current + 1
            while (next < numSquaresX){
                while (next < numSquaresX && !grid!!.isCellOccupied(grid!!.field[it][next])){
                    next ++
                }
                if (next >=4){ next--   }
                val currentValue:Double = if (grid!!.isCellOccupied(grid!!.field[it][current]))
                    Math.log(grid!!.getCellContent(grid!!.field[it][current])!!.value.toDouble()) / Math.log(2.0) else 0.0
                val nextValue:Double = if (grid!!.isCellOccupied(grid!!.field[it][next]))
                    Math.log(grid!!.getCellContent(grid!!.field[it][next])!!.value.toDouble()) / Math.log(2.0) else 0.0
                if (currentValue > nextValue){
                    totals[0] += nextValue - currentValue
                }
                else if(nextValue > currentValue){
                    totals[1] += currentValue - nextValue
                }
                current = next
                next ++
            }
        }
        //左右方向
        (0 until numSquaresY).map {
            var current = 0
            var next = current + 1
            while (next < numSquaresX){
                while (next < numSquaresX && !grid!!.isCellOccupied(grid!!.field[next][it])){
                    next ++
                }
                if (next >=4){ next--   }
                val currentValue:Double = if (grid!!.isCellOccupied(grid!!.field[current][it]))
                    Math.log(grid!!.getCellContent(grid!!.field[current][it])!!.value.toDouble()) / Math.log(2.0) else 0.0
                val nextValue:Double = if (grid!!.isCellOccupied(grid!!.field[next][it]))
                    Math.log(grid!!.getCellContent(grid!!.field[next][it])!!.value.toDouble()) / Math.log(2.0) else 0.0
                if (currentValue > nextValue){
                    totals[2] += nextValue - currentValue
                }
                else if(nextValue > currentValue){
                    totals[3] += currentValue - nextValue
                }
                current = next
                next ++
            }
        }
        return Math.max(totals[0],totals[1]) + Math.max(totals[2],totals[3])
    }

    fun maxTileValue():Int{
        var max = 0
        for (i in 0 until grid!!.field.size){
            for (j in 0 until grid!!.field[i].size){
                if (grid!!.isCellOccupied(grid!!.field[i][j])){ //如果这个格子不是空的
                    val value = grid!!.field[i][j]!!.value
                   if (value > max){
                       max = value
                   }
                }
            }
        }
        return max
    }
}