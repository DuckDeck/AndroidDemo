package stan.androiddemo.project.Game2048

import stan.androiddemo.tool.Logger
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by stanhu on 22/8/2017.
 */
class AI2048(var grid: Grid) {

    val minSearchTime = 150

    fun eval():Double{
        val emptyCellsNum = grid.getAvailableCells().size
        val smoothWeight = 0.1
        val monoWeight = 1.0
        val emptyWeight = 2.7
        val maxWeigh = 1.0
        val maxValue = Math.log(grid.maxTileValue().toDouble()) / Math.log(2.0)
        val a = grid.smoothness() * smoothWeight
        val b = grid.monotonly() * monoWeight
        val c = Math.log(emptyCellsNum.toDouble()) * emptyWeight
        val d = maxValue * maxWeigh
        Logger.e("A:$a---B:$b----C:$c----D:$d")
        Logger.e("result:"+(a + b + c + d))
        return  a + b + c + d
    }

    fun search(depth:Int,alpha:Double,beta:Double,positions:Int,cutoffs:Int):MoveResult{
        var bestScore:Double = 0.0
        var bestMove = -1
        var result:MoveResult? = null
        var position = positions
        var cutoff = cutoffs
        if (grid.playerTurn) {
            bestScore = alpha
            (0 until 4).map {
                val virtualGrid = grid.clone() //这里里面的东西可能要copy一份
                if (virtualGrid.move(it)) {
                    position++
                    if (virtualGrid.isWin()) {
                        return MoveResult(it, 10000.0, position, cutoffs)
                    }
                    val ai = AI2048(virtualGrid)
                    if (depth == 0) {
                        result = MoveResult(it, ai.eval(), 0, 0)
                    } else {
                        result = ai.search(depth - 1, bestScore, beta, position, cutoffs)
                        if (result!!.score > 9900) {
                            result!!.score--
                        }
                        position = result!!.positions
                        cutoff = result!!.cutoffs
                    }
                    if (result!!.score > bestScore) {
                        bestScore = result!!.score
                        bestMove = it
                    }
                    if (bestScore > beta) {
                        cutoff++
                        return MoveResult(bestMove, beta, position, cutoff)
                    }
                }
            }
        }
        else{
            bestScore = beta
            var candidates = arrayListOf<Pair<Cell,Int>>()
            val cells = grid.getAvailableCells()
            var scores = hashMapOf(2 to ArrayList<Double?>(),4 to ArrayList<Double?>())
            for (v in scores){
                for (c in 0 until cells.size){
                    v.value.add(null)
                    val tile = Tile(cells[c],v.key)
                    grid.insertTile(tile)
                    v.value[c] = grid.isLands() - grid.smoothness()
                    grid.removeTile(tile)
                }
            }
            val maxScore = Math.max(scores[2]!!.maxBy { it!! }!!,scores[4]!!.maxBy { it!! }!!)
            for (v in scores){
                for (i in 0 until scores[v.key]!!.size ){
                    if (scores[v.key]!![i] == maxScore){
                        candidates.add(Pair(cells[i],v.key))
                    }
                }
            }



            for (i in 0 until candidates.size){
                val pos =  candidates[i].first
                val value = candidates[i].second
                val newGrid = grid.clone()
                val tile = Tile(pos,value)
                newGrid.insertTile(tile)
                newGrid.playerTurn = true
                position ++
                val newAI = AI2048(newGrid)
                result = newAI.search(depth,alpha,bestScore,position,cutoffs)
                position = result!!.positions
                cutoff = result!!.cutoffs
                if (result!!.score < bestScore){
                    bestScore = result!!.score
                }
                if (bestScore < alpha){
                    cutoff++
                    return MoveResult(null, beta, position, cutoff)
                }
            }
        }
        return MoveResult(bestMove,bestScore,position,cutoff)
    }

    fun getBest():MoveResult?{
        return iterativeDeep()
    }

    fun iterativeDeep():MoveResult?{
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
        Logger.e("结果是"+best.toString())
        return best
    }
}

class MoveResult{
    constructor(move:Int?,score:Double,positions:Int,cutoffs: Int){
        this.move = move
        this.score = score
        this.positions = positions
        this.cutoffs = cutoffs
    }
    var move:Int? = 0
    var score:Double = 0.0
    var positions = 0
    var cutoffs = 0

    override fun toString(): String {
        return "MoveResult(move=$move, score=$score, positions=$positions, cutoffs=$cutoffs)"
    }


}