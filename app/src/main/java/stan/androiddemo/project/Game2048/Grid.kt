package stan.androiddemo.project.Game2048

/**
 * Created by hugfo on 2017/8/19.
 */
class Grid{
    var field: Array<Array<Tile?>>
    var undoField: Array<Array<Tile?>>
    private var bufferField: Array<Array<Tile?>>

    constructor(sizeX:Int,sizeY:Int){
        field = Array<Array<Tile?>>(sizeX) { arrayOfNulls<Tile>(sizeY) }
        undoField = Array<Array<Tile?>>(sizeX) { arrayOfNulls<Tile>(sizeY) }
        bufferField = Array<Array<Tile?>>(sizeX) { arrayOfNulls<Tile>(sizeY) }
        clearGrid()
        clearUndoGrid()
    }

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
        var availableCells = ArrayList<Cell>()
        for (i in 0 until field.size){
            for (j in 0 until field[i].size){
               if (field[i][j] == null){
                   availableCells.add(Cell(i,j))
               }
            }
        }
        return availableCells
    }

    fun getNotAvailableCells():ArrayList<Cell>{
        var notAvailableCells = ArrayList<Cell>()
        for (i in 0 until field.size){
            for (j in 0 until field[i].size){
                if (field[i][j] != null){
                    notAvailableCells.add(Cell(i,j))
                }
            }
        }
        return notAvailableCells
    }

    fun isCellsAvailable():Boolean{
        return getAvailableCells().size >= 1
    }

    fun isCellsAvailable(cell:Cell):Boolean{
        return getAvailableCells().size >= 1
    }



    fun isCellOccupied(cell:Cell):Tile?{
        if (cell != null && isCellWithinBounds(cell)){
            return field[cell.x][cell.y]
        }
        return null
    }


    fun getCellContent(cell: Cell?):Tile?{
        if (cell != null && isCellWithinBounds(cell)){
            return field[cell!!.x][cell!!.y]
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

    fun removeTitle(tile:Tile){
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
}