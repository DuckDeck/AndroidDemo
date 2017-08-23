package stan.androiddemo.project.Game2048

import stan.androiddemo.tool.Logger
import java.util.*

/**
 * Created by stanhu on 22/8/2017.
 */
class AI2048(var game: VirtualGame) {

    val minSearchTime = 100

    fun getupGrid(grid:Grid){
        this.game.grid = grid.clone()
    }

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
            bestScore = alpha
            (0 until 4).map {
                val virtualGame = VirtualGame(this.game.grid!!.clone()) //这里里面的东西可能要copy一份
                if (virtualGame.move(it)){
                    position ++
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
                    when (it){
                        0->{
                            Logger.e("向上的分是" + result?.score)
                        }
                        1->{
                            Logger.e("向右的分是" + result?.score)
                        }
                        2->{
                            Logger.e("向下的分是" + result?.score)
                        }
                        3->{
                            Logger.e("向左的分是" + result?.score)
                        }

                    }
                }
            }
        return  return MoveResult(bestMove,bestScore,position,cutoff)
    }

    fun getBest():MoveResult{
        return iterativeDeep()
    }

    fun iterativeDeep():MoveResult{
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
        }while (Date().time - start.time < minSearchTime)
        return best!!
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