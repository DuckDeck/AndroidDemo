package stan.androiddemo.project.Game2048

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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
        setContentView(R.layout.activity_game)
    }
}
