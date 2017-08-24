package stan.androiddemo.project.Game2048

import java.util.*

/**
 * Created by hugfo on 2017/8/19.
 */
open class Grid(sizeX: Int, sizeY: Int) {
    var field: Array<Array<Tile?>> = Array(sizeX) { arrayOfNulls<Tile>(sizeY) }
    var undoField: Array<Array<Tile?>> = Array(sizeX) { arrayOfNulls<Tile>(sizeY) }
    private var bufferField: Array<Array<Tile?>> = Array(sizeX) { arrayOfNulls<Tile>(sizeY) }

    //从为空的格子随机中获取一个
    fun randomAvailableCell():Cell?{
        val availableCells = getAvailableCells()
        if (availableCells.size >=1 ){
            return availableCells[Math.floor(Math.random() * availableCells.size).toInt()]
        }
        return null
    }


    //获取为空的格子
    fun getAvailableCells():ArrayList<Cell>{
        val availableCells = ArrayList<Cell>()
        for (i in 0 until field.size){
            (0 until field[i].size)
                    .filter { field[i][it] == null }
                    .mapTo(availableCells) { Cell(i, it) }
        }
        return availableCells
    }

    fun getNotAvailableCells():ArrayList<Cell>{
        val notAvailableCells = ArrayList<Cell>()
        for (i in 0 until field.size){
            (0 until field[i].size)
                    .filter { field[i][it] != null }
                    .mapTo(notAvailableCells) { Cell(i, it) }
        }
        return notAvailableCells
    }

    fun isCellsAvailable():Boolean{
        return getAvailableCells().size >= 1
    }

    fun isCellAvailable(cell:Cell):Boolean{
        return !isCellOccupied(cell)
    }


    fun isCellOccupied(cell:Cell?):Boolean{
       return getCellContent(cell) != null
    }


    fun getCellContent(cell: Cell?):Tile?{
        if (cell != null && isCellWithinBounds(cell)){
            return field[cell.x][cell.y]
        }
        return  null
    }
    fun getCellContent(x:Int,y:Int):Tile?{
        if (isCellWithinBounds(x,y)){
            return field[x][y]
        }
        return  null
    }


    fun isCellWithinBounds(cell: Cell):Boolean{
        return 0 <= cell.x && cell.x < field.size && 0 <= cell.y && cell.y < field[0].size
    }

    fun isCellWithinBounds(x:Int,y:Int):Boolean{
        return x <= x && x < field.size   && 0 <= y && y < field[0].size
    }

    fun insertTile(tile:Tile){
        field[tile.x][tile.y] = tile
    }

    fun removeTile(tile:Tile){
        field[tile.x][tile.y] = null
    }

    fun saveTiles(){
        for (i in 0 until bufferField.size){
            for (j in 0 until bufferField[i].size){
                if(bufferField[i][j] == null){
                    undoField[i][j] = null
                }
                else{
                    undoField[i][j] = Tile(i,j,bufferField[i][j]!!.value)
                }
            }
        }
    }

    fun prepareSaveTiles() {
        for (xx in field.indices) {
            for (yy in 0..field[0].size - 1) {
                if (field[xx][yy] == null) {
                    bufferField[xx][yy] = null
                } else {
                    bufferField[xx][yy] = Tile(xx, yy,
                            field[xx][yy]!!.value)
                }
            }
        }
    }

    fun revertTiles() {
        for (xx in undoField.indices) {
            for (yy in 0..undoField[0].size - 1) {
                if (undoField[xx][yy] == null) {
                    field[xx][yy] = null
                } else {
                    field[xx][yy] = Tile(xx, yy,
                            undoField[xx][yy]!!.value)
                }
            }
        }
    }

    fun clearGrid(){
        for (i in 0 until field.size){
            for (j in 0 until field[i].size){
                field[i][j] = null
            }
        }
    }

    fun clearUndoGrid(){
        for (i in 0 until field.size){
            for (j in 0 until field[i].size){
                undoField[i][j] = null
            }
        }
    }

    init {
        clearGrid()
        clearUndoGrid()
    }

    fun clone():Grid{
        val grid = Grid(this.field.size,this.field[0].size)
        for (i in 0 until grid.field.size){
            for (j in 0 until grid.field[i].size){
                grid.field[i][j] = this.field[i][j]?.clone()
            }
        }
        return grid
    }





    var sizeWidth = sizeX
    var sizeHeight = sizeY
    var playerTurn = true
    private fun getVector(direction: Int): Cell {
        val map = arrayOf(Cell(0, -1), // up
                Cell(1, 0), // right
                Cell(0, 1), // down
                Cell(-1, 0) // left
        )
        return map[direction]
    }

    private fun buildTraversalsX(vector: Cell): List<Int> {
        val traversals = (0..sizeWidth - 1).toList()

        if (vector.x === 1) {
            Collections.reverse(traversals)
        }

        return traversals
    }

    private fun buildTraversalsY(vector: Cell): List<Int> {
        val traversals = (0..sizeHeight - 1).toList()

        if (vector.y === 1) {
            Collections.reverse(traversals)
        }

        return traversals
    }

    private fun prepareTiles() {
        for (array in field) {
            array.filter { isCellOccupied(it as Cell?) }
                    .forEach { it?.mergedFrom = null }
        }
    }

    private fun findFarthestPosition(cell: Cell, vector: Cell): Array<Cell> {
        var previous: Cell
        var nextCell = Cell(cell.x, cell.y)
        do {
            previous = nextCell
            nextCell = Cell(previous.x + vector.x,previous.y + vector.y)
        } while (isCellWithinBounds(nextCell) && isCellAvailable(nextCell))

        val answer = arrayOf(previous, nextCell)
        return answer
    }

    fun positionsEqual(first:Cell,second:Cell):Boolean{
        return first.x == second.x && first.y == second.y
    }

    private fun moveTile(tile: Tile, cell: Cell) {
        field[tile.x][tile.y] = null
        field[cell.x][cell.y] = tile
        tile.updatePosition(cell)
    }

    fun isWin():Boolean{
        for (i in 0 until field.size){
            for (j in 0 until field[i].size){
                if (isCellOccupied(field[i][j])){ //如果这个格子不是空的
                    if (field[i][j]!!.value >= 2048){
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
                val tile = getCellContent(cell)
                if (tile != null) {
                    val positions = findFarthestPosition(cell, vector)
                    val next = getCellContent(positions[1])
                    if (next != null && next.value === tile!!.value && next.mergedFrom == null) {
                        val merged = Tile(positions[1],tile.value * 2)
                        val temp = arrayOf(tile, next)
                        merged.mergedFrom = temp
                        insertTile(merged)
                        removeTile(tile)
                        tile.updatePosition(positions[1])
                    } else {
                        moveTile(tile, positions[0])
                    }
                    if (!positionsEqual(cell, tile)) {
                        moved = true
                        playerTurn = false
                    }
                }
            }
        }
        return moved
    }
    //获取平滑度
    fun smoothness():Double{
        var smoothness = 0.0
        for (i in 0 until field.size){
            for (j in 0 until field[i].size){
                if (isCellOccupied(field[i][j])){ //如果这个格子不是空的
                    val value = Math.log(field[i][j]!!.value.toDouble()) / Math.log(2.toDouble())
                    (1 until 3)
                            .map { getVector(it) }
                            .map { findFarthestPosition(field[i][j]!!, it)[1] }
                            .filter {isCellOccupied(it) }
                            .map { getCellContent(it) }
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
        (0 until sizeWidth).map {
            var current = 0
            var next = current + 1
            while (next < sizeWidth){
                while (next < sizeWidth && !isCellOccupied(field[it][next])){
                    next ++
                }
                if (next >=4){ next--   }
                val currentValue:Double = if (isCellOccupied(field[it][current]))
                    Math.log(getCellContent(field[it][current])!!.value.toDouble()) / Math.log(2.0) else 0.0
                val nextValue:Double = if (isCellOccupied(field[it][next]))
                    Math.log(getCellContent(field[it][next])!!.value.toDouble()) / Math.log(2.0) else 0.0
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
        (0 until sizeHeight).map {
            var current = 0
            var next = current + 1
            while (next < sizeWidth){
                while (next < sizeWidth && !isCellOccupied(field[next][it])){
                    next ++
                }
                if (next >=4){ next--   }
                val currentValue:Double = if (isCellOccupied(field[current][it]))
                    Math.log(getCellContent(field[current][it])!!.value.toDouble()) / Math.log(2.0) else 0.0
                val nextValue:Double = if (isCellOccupied(field[next][it]))
                    Math.log(getCellContent(field[next][it])!!.value.toDouble()) / Math.log(2.0) else 0.0
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
        for (i in 0 until field.size){
            for (j in 0 until field[i].size){
                if (isCellOccupied(field[i][j])){ //如果这个格子不是空的
                    val value = getCellContent(i,j)!!.value
                    if (value > max){
                        max = value
                    }
                }
            }
        }
        return max
    }

    fun isLands():Double {
        //这玩意真麻烦
        var isLands = 0.0
        for (i in 0 until field.size){
            for (j in 0 until field[i].size){
                 field[i][j]?.isMarked = false

            }
        }

        for (i in 0 until field.size){
            for (j in 0 until field[i].size){
                if (field[i][j] != null &&  !field[i][j]!!.isMarked){
                    isLands ++
                    mark(i,j,field[i][j]!!.value)
                }
            }
        }

        return isLands
    }

    fun mark(x:Int,y:Int,value:Int){
        if (x >= 0 && x < field.size && y >= 0 && y < field[0].size && field[x][y] != null && field[x][y]!!.value == value && !field[x][y]!!.isMarked ){
            field[x][y]?.isMarked = true
            (0 until 4).map {
                val v = getVector(it)
                mark(x + v.x,y+v.y,value)
            }
        }
    }


}