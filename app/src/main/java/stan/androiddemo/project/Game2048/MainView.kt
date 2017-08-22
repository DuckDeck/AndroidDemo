package stan.androiddemo.project.Game2048

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import stan.androiddemo.R

/**
 * Created by hugfo on 2017/8/19.
 */
class MainView: View {
    companion object {
        internal val BASE_ANIMATION_TIME:Long = 100000000

        internal val MERGING_ACCELERATION = (-0.5).toFloat()
        internal val INITIAL_VELOCITY = (1 - MERGING_ACCELERATION) / 4
    }
    // Internal variables
    internal var paint = Paint()
    var game: MainGame
    var hasSaveState = false
    private val numCellTypes = 16
    var continueButtonEnabled = false

    // Layout variables
    private var cellSize = 0
    private var textSize = 0f
    private var cellTextSize = 0f
    private var gridWidth = 0
    private var TEXT_BLACK: Int = 0
    private var TEXT_WHITE: Int = 0
    private var TEXT_BROWN: Int = 0
    var startingX: Int = 0
    var startingY: Int = 0
    var endingX: Int = 0
    var endingY: Int = 0
    private var textPaddingSize: Int = 0
    private var iconPaddingSize: Int = 0

    private var backgroundRectangle: Drawable? = null
    private val cellRectangle = arrayOfNulls<Drawable>(numCellTypes)
    private val bitmapCell = arrayOfNulls<BitmapDrawable>(numCellTypes)
    private var newGameIcon: Drawable? = null
    private var undoIcon: Drawable? = null
    private var cheatIcon: Drawable? = null
    private var lightUpRectangle: Drawable? = null
    private var fadeRectangle: Drawable? = null
    private var background: Bitmap? = null
    private var loseGameOverlay: BitmapDrawable? = null
    private var winGameContinueOverlay: BitmapDrawable? = null
    private var winGameFinalOverlay: BitmapDrawable? = null

    // Text variables
    private var sYAll: Int = 0
    private var titleStartYAll: Int = 0
    private var bodyStartYAll: Int = 0
    private var eYAll: Int = 0
    private var titleWidthHighScore: Int = 0
    private var titleWidthScore: Int = 0

    // Icon variables
    var sYIcons: Int = 0
    var sXNewGame: Int = 0
    var sXUndo: Int = 0
    var sXCheat: Int = 0
    var iconSize: Int = 0

    // Text values
    private var headerText: String? = null
    private var highScoreTitle: String? = null
    private var scoreTitle: String? = null
    private var instructionsText: String? = null
    private var winText: String? = null
    private var loseText: String? = null
    private var continueText: String? = null
    private var forNowText: String? = null
    private var endlessModeText: String? = null

    internal var lastFPSTime = System.nanoTime()
    internal var currentTime = System.nanoTime()

    internal var titleTextSize: Float = 0.toFloat()
    internal var bodyTextSize: Float = 0.toFloat()
    internal var headerTextSize: Float = 0.toFloat()
    internal var instructionsTextSize: Float = 0.toFloat()
    internal var gameOverTextSize: Float = 0.toFloat()

    internal var refreshLastTime = true

    constructor(context:Context):super(context){
        val resources = context.resources
        // Loading resources
        game = MainGame(context, this)
        try {
            // Getting text values
            headerText = resources.getString(R.string.header)
            highScoreTitle = resources.getString(R.string.high_score)
            scoreTitle = resources.getString(R.string.score)
            instructionsText = resources.getString(R.string.instructions)
            winText = resources.getString(R.string.you_win)
            loseText = resources.getString(R.string.game_over)
            continueText = resources.getString(R.string.go_on)
            forNowText = resources.getString(R.string.for_now)
            endlessModeText = resources.getString(R.string.endless)
            // Getting assets
            backgroundRectangle = resources.getDrawable(R.drawable.background_rectangle)

            cellRectangle[0] = resources.getDrawable(R.drawable.cell_rectangle)
            cellRectangle[1] = resources.getDrawable(R.drawable.cell_rectangle_2)
            cellRectangle[2] = resources.getDrawable(R.drawable.cell_rectangle_4)
            cellRectangle[3] = resources.getDrawable(R.drawable.cell_rectangle_8)
            cellRectangle[4] = resources.getDrawable(R.drawable.cell_rectangle_16)
            cellRectangle[5] = resources.getDrawable(R.drawable.cell_rectangle_32)
            cellRectangle[6] = resources.getDrawable(R.drawable.cell_rectangle_64)
            cellRectangle[7] = resources.getDrawable(R.drawable.cell_rectangle_128)
            cellRectangle[8] = resources.getDrawable(R.drawable.cell_rectangle_256)
            cellRectangle[9] = resources.getDrawable(R.drawable.cell_rectangle_512)
            cellRectangle[10] = resources.getDrawable(R.drawable.cell_rectangle_1024)
            cellRectangle[11] = resources.getDrawable(R.drawable.cell_rectangle_2048)
            cellRectangle[12] = resources.getDrawable(R.drawable.cell_rectangle_4096)
            cellRectangle[13] = resources.getDrawable(R.drawable.cell_rectangle_8192)
            cellRectangle[14] = resources.getDrawable(R.drawable.cell_rectangle_16384)
            cellRectangle[15] = resources.getDrawable(R.drawable.cell_rectangle_32768)

            newGameIcon = resources.getDrawable(R.drawable.ic_action_refresh)
            undoIcon = resources.getDrawable(R.drawable.ic_action_undo)
            cheatIcon = resources.getDrawable(R.drawable.ic_action_cheat)
            lightUpRectangle = resources
                    .getDrawable(R.drawable.light_up_rectangle)
            fadeRectangle = resources.getDrawable(R.drawable.fade_rectangle)
            TEXT_WHITE = resources.getColor(R.color.text_white)
            TEXT_BLACK = resources.getColor(R.color.text_black)
            TEXT_BROWN = resources.getColor(R.color.text_brown)
            this.setBackgroundColor(resources.getColor(R.color.background))
            val font = Typeface.createFromAsset(resources.assets,
                    "ClearSans-Bold.ttf")
            paint.typeface = font
            paint.isAntiAlias = true
        } catch (e: Exception) {
            println("Error getting assets?")
        }

        setOnTouchListener(InputListener(this))
        game.newGame()
    }

    override fun onDraw(canvas: Canvas) {
        // Reset the transparency of the screen

        canvas.drawBitmap(background, 0f, 0f, paint)

        drawScoreText(canvas)

        if (!game.isActive() && !game.aGrid!!.isAnimationActive()) {
            drawNewGameButton(canvas, true)
        }

        drawCells(canvas)

        if (!game.isActive()) {
            drawEndGameState(canvas)
        }

        if (!game.canContinue()) {
            drawEndlessText(canvas)
        }

        // Refresh the screen if there is still an animation running
        if (game.aGrid!!.isAnimationActive()) {
            invalidate(startingX, startingY, endingX, endingY)
            tick()
            // Refresh one last time on game end.
        } else if (!game.isActive() && refreshLastTime) {
            invalidate()
            refreshLastTime = false
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        getLayout(width, height)
        createBackgroundBitmap(width, height)
        createBitmapCells()
        createOverlays()
    }

    private fun drawDrawable(canvas: Canvas, draw: Drawable, startingX: Int,
                             startingY: Int, endingX: Int, endingY: Int) {
        draw.setBounds(startingX, startingY, endingX, endingY)
        draw.draw(canvas)
    }

    private fun drawCellText(canvas: Canvas, value: Int, sX: Int, sY: Int) {
        val textShiftY = centerText()
        if (value >= 8) {
            paint.color = TEXT_WHITE
        } else {
            paint.color = TEXT_BLACK
        }
        canvas.drawText("" + value, (sX + cellSize / 2).toFloat(), (sY + cellSize / 2 - textShiftY).toFloat(), paint)
    }

    private fun drawScoreText(canvas: Canvas) {
        // Drawing the score text: Ver 2
        paint.textSize = bodyTextSize
        paint.textAlign = Paint.Align.CENTER

        val bodyWidthHighScore = paint.measureText("" + game.highScore).toInt()
        val bodyWidthScore = paint.measureText("" + game.score).toInt()

        val textWidthHighScore = Math.max(titleWidthHighScore,
                bodyWidthHighScore) + textPaddingSize * 2
        val textWidthScore = Math.max(titleWidthScore, bodyWidthScore) + textPaddingSize * 2

        val textMiddleHighScore = textWidthHighScore / 2
        val textMiddleScore = textWidthScore / 2

        val eXHighScore = endingX
        val sXHighScore = eXHighScore - textWidthHighScore

        val eXScore = sXHighScore - textPaddingSize
        val sXScore = eXScore - textWidthScore

        // Outputting high-scores box
        backgroundRectangle?.setBounds(sXHighScore, sYAll, eXHighScore, eYAll)
        backgroundRectangle?.draw(canvas)
        paint.textSize = titleTextSize
        paint.color = TEXT_BROWN
        canvas.drawText(highScoreTitle, (sXHighScore + textMiddleHighScore).toFloat(),
                titleStartYAll.toFloat(), paint)
        paint.textSize = bodyTextSize
        paint.color = TEXT_WHITE
        canvas.drawText(game.highScore.toString(), (sXHighScore + textMiddleHighScore).toFloat(), bodyStartYAll.toFloat(), paint)

        // Outputting scores box
        backgroundRectangle?.setBounds(sXScore, sYAll, eXScore, eYAll)
        backgroundRectangle?.draw(canvas)
        paint.textSize = titleTextSize
        paint.color = TEXT_BROWN
        canvas.drawText(scoreTitle, (sXScore + textMiddleScore).toFloat(), titleStartYAll.toFloat(),
                paint)
        paint.textSize = bodyTextSize
        paint.color = TEXT_WHITE
        canvas.drawText(game.score.toString(), (sXScore + textMiddleScore).toFloat(),
                bodyStartYAll.toFloat(), paint)
    }

    private fun drawNewGameButton(canvas: Canvas, lightUp: Boolean) {
        if (lightUp) {
            drawDrawable(canvas, lightUpRectangle!!, sXNewGame, sYIcons,
                    sXNewGame + iconSize, sYIcons + iconSize)
        } else {
            drawDrawable(canvas, backgroundRectangle!!, sXNewGame, sYIcons,
                    sXNewGame + iconSize, sYIcons + iconSize)
        }
        drawDrawable(canvas, newGameIcon!!, sXNewGame + iconPaddingSize, sYIcons + iconPaddingSize, sXNewGame + iconSize - iconPaddingSize,
                sYIcons + iconSize - iconPaddingSize)
    }

    fun drawCheatButton(canvas: Canvas) {
        drawDrawable(canvas, backgroundRectangle!!, sXCheat, sYIcons, sXCheat + iconSize, sYIcons + iconSize)
        drawDrawable(canvas, cheatIcon!!, sXCheat + iconPaddingSize, sYIcons + iconPaddingSize, sXCheat + iconSize - iconPaddingSize,
                sYIcons + iconSize - iconPaddingSize)
    }

    private fun drawUndoButton(canvas: Canvas) {
        drawDrawable(canvas, backgroundRectangle!!, sXUndo, sYIcons, sXUndo + iconSize, sYIcons + iconSize)
        drawDrawable(canvas, undoIcon!!, sXUndo + iconPaddingSize, sYIcons + iconPaddingSize, sXUndo + iconSize - iconPaddingSize, sYIcons + iconSize - iconPaddingSize)
    }

    private fun drawHeader(canvas: Canvas) {
        // Drawing the header
        paint.textSize = headerTextSize
        paint.color = TEXT_BLACK
        paint.textAlign = Paint.Align.LEFT
        val textShiftY = centerText() * 2
        val headerStartY = sYAll - textShiftY
        canvas.drawText(headerText, startingX.toFloat(), headerStartY.toFloat(), paint)
    }

    fun drawInstructions(canvas: Canvas) {
        // Drawing the instructions
        paint.textSize = instructionsTextSize
        paint.textAlign = Paint.Align.LEFT
        val textShiftY = centerText() * 5
        canvas.drawText(instructionsText, startingX.toFloat(), (endingY - textShiftY + textPaddingSize).toFloat(), paint)
    }

    private fun drawBackground(canvas: Canvas) {
        drawDrawable(canvas, backgroundRectangle!!, startingX, startingY,
                endingX, endingY)
    }

    private fun drawBackgroundGrid(canvas: Canvas) {
        // Outputting the game grid
        for (xx in 0..game.numSquaresX - 1) {
            for (yy in 0..game.numSquaresY - 1) {
                val sX = startingX + gridWidth + (cellSize + gridWidth) * xx
                val eX = sX + cellSize
                val sY = startingY + gridWidth + (cellSize + gridWidth) * yy
                val eY = sY + cellSize

                drawDrawable(canvas, cellRectangle[0]!!, sX, sY, eX, eY)
            }
        }
    }

    private fun drawCells(canvas: Canvas) {
        paint.textSize = textSize
        paint.textAlign = Paint.Align.CENTER
        // Outputting the individual cells
        for (xx in 0..game.numSquaresX - 1) {
            for (yy in 0..game.numSquaresY - 1) {
                val sX = startingX + gridWidth + (cellSize + gridWidth) * xx
                val eX = sX + cellSize
                val sY = startingY + gridWidth + (cellSize + gridWidth) * yy
                val eY = sY + cellSize

                val currentTile = game.grid?.getCellContent(xx, yy)
                if (currentTile != null) {
                    // Get and represent the value of the tile
                    val value = currentTile.value
                    val index = log2(value)

                    // Check for any active animations
                    val aArray = game.aGrid?.getAnimationCell(xx, yy)
                    var animated = false
                    for (i in aArray!!.indices.reversed()) {
                        val aCell = aArray[i]
                        // If this animation is not active, skip it
                        if (aCell.animationType === MainGame.SPAWN_ANIMATION) {
                            animated = true
                        }
                        if (!aCell.isActive()) {
                            continue
                        }

                        if (aCell.animationType === MainGame.SPAWN_ANIMATION) { // Spawning
                            // animation
                            val percentDone = aCell.getPercentageDone()
                            val textScaleSize = percentDone.toFloat()
                            paint.textSize = textSize * textScaleSize

                            val cellScaleSize = cellSize / 2 * (1 - textScaleSize)
                            bitmapCell[index]?.setBounds(
                                    (sX + cellScaleSize).toInt(),
                                    (sY + cellScaleSize).toInt(),
                                    (eX - cellScaleSize).toInt(),
                                    (eY - cellScaleSize).toInt())
                            bitmapCell[index]?.draw(canvas)
                        } else if (aCell.animationType === MainGame.MERGE_ANIMATION) { // Merging
                            // Animation
                            val percentDone = aCell.getPercentageDone()
                            val textScaleSize = (1.0 + INITIAL_VELOCITY * percentDone + MERGING_ACCELERATION.toDouble() * percentDone * percentDone / 2).toFloat()
                            paint.textSize = textSize * textScaleSize

                            val cellScaleSize = cellSize / 2 * (1 - textScaleSize)
                            bitmapCell[index]?.setBounds(
                                    (sX + cellScaleSize).toInt(),
                                    (sY + cellScaleSize).toInt(),
                                    (eX - cellScaleSize).toInt(),
                                    (eY - cellScaleSize).toInt())
                            bitmapCell[index]?.draw(canvas)
                        } else if (aCell.animationType === MainGame.MOVE_ANIMATION) { // Moving
                            // animation
                            val percentDone = aCell.getPercentageDone()
                            var tempIndex = index
                            if (aArray.size >= 2) {
                                tempIndex -= 1
                            }
                            val previousX = aCell.extras!![0]
                            val previousY = aCell.extras!![1]
                            val currentX = currentTile.x
                            val currentY = currentTile.y
                            val dX = ((currentX - previousX).toDouble() * (cellSize + gridWidth).toDouble() * (percentDone - 1) * 1.0).toInt()
                            val dY = ((currentY - previousY).toDouble() * (cellSize + gridWidth).toDouble() * (percentDone - 1) * 1.0).toInt()
                            bitmapCell[tempIndex]?.setBounds(sX + dX, sY + dY,
                                    eX + dX, eY + dY)
                            bitmapCell[tempIndex]?.draw(canvas)
                        }
                        animated = true
                    }

                    // No active animations? Just draw the cell
                    if (!animated) {
                        bitmapCell[index]?.setBounds(sX, sY, eX, eY)
                        bitmapCell[index]?.draw(canvas)
                    }
                }
            }
        }
    }

    private fun drawEndGameState(canvas: Canvas) {
        var alphaChange = 1.0
        continueButtonEnabled = false
        game.aGrid!!.globalAnimation
                .filter { it.animationType === MainGame.FADE_GLOBAL_ANIMATION }
                .forEach { alphaChange = it.getPercentageDone() }
        var displayOverlay: BitmapDrawable? = null
        if (game.gameWon()) {
            if (game.canContinue()) {
                continueButtonEnabled = true
                displayOverlay = winGameContinueOverlay
            } else {
                displayOverlay = winGameFinalOverlay
            }
        } else if (game.gameLost()) {
            displayOverlay = loseGameOverlay
        }

        if (displayOverlay != null) {
            displayOverlay.setBounds(startingX, startingY, endingX, endingY)
            displayOverlay.alpha = (255 * alphaChange).toInt()
            displayOverlay.draw(canvas)
        }
    }

    private fun drawEndlessText(canvas: Canvas) {
        paint.textAlign = Paint.Align.LEFT
        paint.textSize = bodyTextSize
        paint.color = TEXT_BLACK
        canvas.drawText(endlessModeText, startingX.toFloat(), (sYIcons - centerText() * 2).toFloat(),
                paint)
    }

    private fun createEndGameStates(canvas: Canvas, win: Boolean,
                                    showButton: Boolean) {
        val width = endingX - startingX
        val length = endingY - startingY
        val middleX = width / 2
        val middleY = length / 2
        if (win) {
            lightUpRectangle?.alpha = 127
            drawDrawable(canvas, lightUpRectangle!!, 0, 0, width, length)
            lightUpRectangle?.alpha = 255
            paint.color = TEXT_WHITE
            paint.alpha = 255
            paint.textSize = gameOverTextSize
            paint.textAlign = Paint.Align.CENTER
            val textBottom = middleY - centerText()
            canvas.drawText(winText, middleX.toFloat(), textBottom.toFloat(), paint)
            paint.textSize = bodyTextSize
            val text = if (showButton) continueText else forNowText
            canvas.drawText(text, middleX.toFloat(), (textBottom + textPaddingSize * 2 - centerText() * 2).toFloat(), paint)
        } else {
            fadeRectangle?.alpha = 127
            drawDrawable(canvas, fadeRectangle!!, 0, 0, width, length)
            fadeRectangle?.alpha = 255
            paint.color = TEXT_BLACK
            paint.alpha = 255
            paint.textSize = gameOverTextSize
            paint.textAlign = Paint.Align.CENTER
            canvas.drawText(loseText, middleX.toFloat(), (middleY - centerText()).toFloat(), paint)
        }
    }


    private fun createBackgroundBitmap(width: Int, height: Int) {
        background = Bitmap
                .createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(background)
        drawHeader(canvas)
        drawCheatButton(canvas)
        drawNewGameButton(canvas, false)
        drawUndoButton(canvas)
        drawBackground(canvas)
        drawBackgroundGrid(canvas)
        drawInstructions(canvas)

    }

    private fun createBitmapCells() {
        paint.textSize = cellTextSize
        paint.textAlign = Paint.Align.CENTER
        val resources = resources
        for (xx in bitmapCell.indices) {
            val bitmap = Bitmap.createBitmap(cellSize, cellSize,
                    Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawDrawable(canvas, cellRectangle[xx]!!, 0, 0, cellSize, cellSize)
            drawCellText(canvas, Math.pow(2.0, xx.toDouble()).toInt(), 0, 0)
            bitmapCell[xx] = BitmapDrawable(resources, bitmap)
        }
    }

    private fun createOverlays() {
        val resources = resources
        // Initalize overlays
        var bitmap = Bitmap.createBitmap(endingX - startingX, endingY - startingY, Bitmap.Config.ARGB_8888)
        var canvas = Canvas(bitmap)
        createEndGameStates(canvas, true, true)
        winGameContinueOverlay = BitmapDrawable(resources, bitmap)
        bitmap = Bitmap.createBitmap(endingX - startingX, endingY - startingY,
                Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap)
        createEndGameStates(canvas, true, false)
        winGameFinalOverlay = BitmapDrawable(resources, bitmap)
        bitmap = Bitmap.createBitmap(endingX - startingX, endingY - startingY,
                Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap)
        createEndGameStates(canvas, false, false)
        loseGameOverlay = BitmapDrawable(resources, bitmap)
    }

    private fun tick() {
        currentTime = System.nanoTime()
        game.aGrid?.tickAll(currentTime - lastFPSTime)
        lastFPSTime = currentTime
    }

    fun resyncTime() {
        lastFPSTime = System.nanoTime()
    }

    private fun log2(n: Int): Int {
        if (n <= 0)
            throw IllegalArgumentException()
        return 31 - Integer.numberOfLeadingZeros(n)
    }

    private fun getLayout(width: Int, height: Int) {
        cellSize = Math.min(width / (game.numSquaresX + 1), height / (game.numSquaresY + 3))
        gridWidth = cellSize / 7
        val screenMiddleX = width / 2
        val screenMiddleY = height / 2
        val boardMiddleX = screenMiddleX
        val boardMiddleY = screenMiddleY + cellSize / 2
        iconSize = cellSize / 2

        paint.textAlign = Paint.Align.CENTER
        paint.textSize = cellSize.toFloat()
        textSize = cellSize * cellSize / Math.max(cellSize.toFloat(), paint.measureText("0000"))
        cellTextSize = textSize * 0.9f
        titleTextSize = textSize / 3
        bodyTextSize = (textSize / 1.5).toInt().toFloat()
        instructionsTextSize = (textSize / 1.8).toInt().toFloat()
        headerTextSize = textSize * 2
        gameOverTextSize = textSize * 2
        textPaddingSize = (textSize / 3).toInt()
        iconPaddingSize = (textSize / 5).toInt()

        // Grid Dimensions
        val halfNumSquaresX = game.numSquaresX / 2.0
        val halfNumSquaresY = game.numSquaresY / 2.0

        startingX = (boardMiddleX.toDouble() - (cellSize + gridWidth) * halfNumSquaresX - (gridWidth / 2).toDouble()).toInt()
        endingX = (boardMiddleX.toDouble() + (cellSize + gridWidth) * halfNumSquaresX + (gridWidth / 2).toDouble()).toInt()
        startingY = (boardMiddleY.toDouble() - (cellSize + gridWidth) * halfNumSquaresY - (gridWidth / 2).toDouble()).toInt()
        endingY = (boardMiddleY.toDouble() + (cellSize + gridWidth) * halfNumSquaresY + (gridWidth / 2).toDouble()).toInt()

        paint.textSize = titleTextSize

        var textShiftYAll = centerText()
        // static variables
        sYAll = (startingY - cellSize * 1.5).toInt()
        titleStartYAll = (sYAll.toFloat() + textPaddingSize.toFloat() + titleTextSize / 2 - textShiftYAll).toInt()
        bodyStartYAll = (titleStartYAll.toFloat() + textPaddingSize.toFloat() + titleTextSize / 2 + bodyTextSize / 2).toInt()

        titleWidthHighScore = paint.measureText(highScoreTitle).toInt()
        titleWidthScore = paint.measureText(scoreTitle).toInt()
        paint.textSize = bodyTextSize
        textShiftYAll = centerText()
        eYAll = (bodyStartYAll.toFloat() + textShiftYAll.toFloat() + bodyTextSize / 2 + textPaddingSize.toFloat()).toInt()

        sYIcons = (startingY + eYAll) / 2 - iconSize / 2
        sXNewGame = endingX - iconSize
        sXUndo = sXNewGame - iconSize * 3 / 2 - iconPaddingSize
        sXCheat = sXUndo - iconSize * 3 / 2 - iconPaddingSize
        resyncTime()
    }

    private fun centerText(): Int {
        return ((paint.descent() + paint.ascent()) / 2).toInt()
    }
}