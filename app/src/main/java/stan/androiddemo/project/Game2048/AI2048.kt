package stan.androiddemo.project.Game2048

import java.util.*

/**
 * Created by stanhu on 22/8/2017.
 */
class AI2048(var game: MainGame) {

    val minSearchTime = 100

    fun eval():Double{
        val emptyCellsNum = game.grid!!.getAvailableCells().size
        val smoothWeight = 0.1
        val monoWeight = 1.0
        val emptyWeight = 2.7
        val maxWeigh = 1.0
        return game.smoothness() * smoothWeight + game.monotonly() * monoWeight + Math.log(emptyCellsNum.toDouble()) * emptyWeight  + game.maxTileValue() * maxWeigh
    }

    fun search(depth:Int,alpha:Int,beta:Int,positions:Int,cutooffs:Boolean):MoveResult{
        var bestScore = 0
        var bestMove = -1
        var result = 0
        if (game.playerTurn){
            bestScore = alpha
            (0 until 4).map {
                val virtualGame = VirtualGame(game.grid!!) //这里里面的东西可能要copy一份
                if (virtualGame.move(it)){

                }
            }
        }

    }

    fun getBest():Int{
        return iterativeDeep()
    }

    fun iterativeDeep():Int{
        val start = Date()
        var depth = 0
        var best = 0
        do {

        }
        return 0
    }
}

class MoveResult{
    var move:Int? = 0
    var score = 0
    var positions = 0
    var cutoffs = 0
}