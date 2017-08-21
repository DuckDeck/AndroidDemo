package stan.androiddemo.project.Game2048

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.KeyEvent
import android.view.Window
import stan.androiddemo.R

class GameActivity : AppCompatActivity() {

    lateinit var view:MainView

    companion object {
        val WIDTH = "width"
        val HEIGHT = "height"
        val SCORE = "score"
        val HIGH_SCORE = "high score temp"
        val UNDO_SCORE = "undo score"
        val CAN_UNDO = "can undo"
        val UNDO_GRID = "undo"
        val GAME_STATE = "game state"
        val UNDO_GAME_STATE = "undo game state"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        view = MainView(baseContext)
        val setting = PreferenceManager.getDefaultSharedPreferences(this)
        view.hasSaveState = setting.getBoolean("save_state",false)
        if (savedInstanceState != null){
            if (savedInstanceState.getBoolean("hasState")){
                load()
            }
        }
        setContentView(view)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when(keyCode){
            KeyEvent.KEYCODE_MENU->{
                return true
            }
            KeyEvent.KEYCODE_DPAD_DOWN->{
                view.game.move(2)
                return true
            }
            KeyEvent.KEYCODE_DPAD_UP->{
                view.game.move(0)
                return true
            }
            KeyEvent.KEYCODE_DPAD_LEFT->{
                view.game.move(3)
                return true
            }
            KeyEvent.KEYCODE_DPAD_RIGHT->{
                view.game.move(1)
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putBoolean("hasState",true)
        save()
    }



    override fun onPause() {
        super.onPause()
        save()
    }

    private fun save() {
        val settings = PreferenceManager
                .getDefaultSharedPreferences(this)
        val editor = settings.edit()
        val field = view.game.grid!!.field
        val undoField = view.game.grid!!.undoField
        editor.putInt(WIDTH, field.size)
        editor.putInt(HEIGHT, field.size)
        for (xx in field.indices) {
            for (yy in 0..field[0].size - 1) {
                if (field[xx][yy] != null) {
                    editor.putInt(xx.toString() + " " + yy, field[xx][yy]!!.value)
                } else {
                    editor.putInt(xx.toString() + " " + yy, 0)
                }

                if (undoField[xx][yy] != null) {
                    editor.putInt(UNDO_GRID + xx + " " + yy,
                            undoField[xx][yy]!!.value)
                } else {
                    editor.putInt(UNDO_GRID + xx + " " + yy, 0)
                }
            }
        }
        editor.putLong(SCORE, view.game.score)
        editor.putLong(HIGH_SCORE, view.game.highScore)
        editor.putLong(UNDO_SCORE, view.game.lastScore)
        editor.putBoolean(CAN_UNDO, view.game.canUndo)
        editor.putInt(GAME_STATE, view.game.gameState)
        editor.putInt(UNDO_GAME_STATE, view.game.lastGameState)
        editor.commit()
    }

    override fun onResume() {
        super.onResume()
        load()
    }

    private fun load() {
        // Stopping all animations
        view.game.aGrid?.cancelAnimations()

        val settings = PreferenceManager
                .getDefaultSharedPreferences(this)
        for (xx in 0..view.game.grid!!.field.size - 1) {
            for (yy in 0..view.game.grid!!.field[0].size - 1) {
                val value = settings.getInt(xx.toString() + " " + yy, -1)
                if (value > 0) {
                    view.game.grid!!.field[xx][yy] = Tile(xx, yy, value)
                } else if (value == 0) {
                    view.game.grid!!.field[xx][yy] = null
                }

                val undoValue = settings.getInt(UNDO_GRID + xx + " " + yy, -1)
                if (undoValue > 0) {
                    view.game.grid!!.undoField[xx][yy] = Tile(xx, yy,
                            undoValue)
                } else if (value == 0) {
                    view.game.grid!!.undoField[xx][yy] = null
                }
            }
        }

        view.game.score = settings.getLong(SCORE, view.game.score)
        view.game.highScore = settings.getLong(HIGH_SCORE, view.game.highScore)
        view.game.lastScore = settings.getLong(UNDO_SCORE, view.game.lastScore)
        view.game.canUndo = settings.getBoolean(CAN_UNDO, view.game.canUndo)
        view.game.gameState = settings.getInt(GAME_STATE, view.game.gameState)
        view.game.lastGameState = settings.getInt(UNDO_GAME_STATE,
                view.game.lastGameState)
    }
}
