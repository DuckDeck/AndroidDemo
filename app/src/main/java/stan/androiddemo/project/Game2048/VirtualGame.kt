package stan.androiddemo.project.Game2048

import java.util.*

/**
 * Created by stanhu on 22/8/2017.
 */
//Grid 要 copy 一份才行
class VirtualGame(var grid: Grid) {

    internal val numSquaresX = 4
    internal val numSquaresY = 4

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

    private fun prepareTiles() {
        for (array in grid!!.field) {
            array.filter { grid!!.isCellOccupied(it as Cell?) }
                    .forEach { it?.mergedFrom = null }
        }
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

    fun positionsEqual(first:Cell,second:Cell):Boolean{
        return first.x == second.x && first.y == second.y
    }

    private fun moveTile(tile: Tile, cell: Cell) {
        grid!!.field[tile.x][tile.y] = null
        grid!!.field[cell.x][cell.y] = tile
        tile.updatePosition(cell)
    }

    fun isWin():Boolean{
        for (i in 0 until grid!!.field.size){
            for (j in 0 until grid!!.field[i].size){
                if (grid!!.isCellOccupied(grid!!.field[i][j])){ //如果这个格子不是空的
                    if (grid!!.field[i][j]!!.value >= 2048){
                        return  true
                    }
                }
            }
        }
        return false
    }

    fun move(direction: Int):Boolean {

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

                        val merged = Tile(positions[1],
                                tile.value * 2)
                        val temp = arrayOf(tile, next)
                        merged.mergedFrom = temp

                        grid?.insertTile(merged)
                        grid?.removeTile(tile)

                        tile.updatePosition(positions[1])
                    } else {
                        moveTile(tile, positions[0])
                    }

                    if (!positionsEqual(cell, tile)) {
                        moved = true
                    }
                }
            }
        }
        return moved
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