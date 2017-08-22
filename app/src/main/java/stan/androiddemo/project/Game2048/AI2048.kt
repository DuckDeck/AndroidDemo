package stan.androiddemo.project.Game2048

/**
 * Created by stanhu on 22/8/2017.
 */
class AI2048(var game: MainGame) {
    fun eval():Double{
        val emptyCellsNum = game.grid!!.getAvailableCells().size
        val smoothWeight = 0.1
        val monoWeight = 1.0
        val emptyWeight = 2.7
        val maxWeigh = 1.0
        return game.smoothness() * smoothWeight + game.monotonly() * monoWeight + Math.log(emptyCellsNum.toDouble()) * emptyWeight  + game.maxTileValue() * maxWeigh
    }

    fun search(depth:Int,alpha:Int,beta:Int,positions:Int,cutooffs:Boolean){
        var bestScore = 0
        var bestMove = -1
        var result = 0
        
    }
}