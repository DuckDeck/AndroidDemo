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

    fun search(depth:Int,alpha:Double,beta:Double,positions:Int,cutoffs:Int):MoveResult{
        var bestScore:Double = 0.0
        var bestMove = -1
        var result:MoveResult? = null
        var position = positions
        var cutoff = cutoffs
        if (game.playerTurn){
            bestScore = alpha
            (0 until 4).map {
                val virtualGame = VirtualGame(game.grid!!) //这里里面的东西可能要copy一份
                if (virtualGame.move(it)){
                    position ++
                }
                if (virtualGame.isWin()){
                    return MoveResult(it,10000.0,position,cutoffs)
                }
                if (depth == 0){
                    result = MoveResult(it,eval(),0,0)
                }
                else{
                    result = search(depth - 1,bestScore,beta,position,cutoffs)
                    if (result!!.score > 9900){
                        result!!.score --
                    }
                    position = result!!.positions
                    cutoff = result!!.cutoffs
                }
                if (result!!.score > bestScore){
                    bestScore = result!!.score
                    bestMove = it
                }
                if (bestScore > beta){
                    cutoff++
                    return MoveResult(bestMove,beta,position,cutoff)
                }
            }
        }
        return  return MoveResult(bestMove,bestScore,position,cutoff)
    }

    fun getBest():Int{
        return iterativeDeep()
    }

    fun iterativeDeep():Int{
        val start = Date()
        var depth = 0
        var best:MoveResult? = null
        do {
            val newBest = search(depth,-10000.0,10000.0,0,0)
            if (newBest.move == -1){
                break
            }
            else{
                best = newBest
            }
            depth++
        }while (Date() - start < minSearchTime)
        return 0
    }
}

class MoveResult{
    constructor(move:Int,score:Double,positions:Int,cutoffs: Int){
        this.move = move
        this.score = score
        this.positions = positions
        this.cutoffs = cutoffs
    }
    var move:Int? = 0
    var score:Double = 0.0
    var positions = 0
    var cutoffs = 0
}