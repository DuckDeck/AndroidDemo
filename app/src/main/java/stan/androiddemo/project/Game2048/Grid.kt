package stan.androiddemo.project.Game2048

/**
 * Created by hugfo on 2017/8/19.
 */
class Grid(sizeX: Int, sizeY: Int) {
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

}