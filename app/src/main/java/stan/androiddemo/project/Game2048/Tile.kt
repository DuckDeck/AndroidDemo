package stan.androiddemo.project.Game2048

/**
 * Created by hugfo on 2017/8/19.
 */
class Tile:Cell{
     var value: Int = 0
     var mergedFrom: Array<Tile>? = null

    constructor(x:Int,y:Int,value:Int):super(x,y){
        this.value = value
    }

    constructor(cell:Cell,value:Int):super(cell.x,cell.y){
        this.value = value
    }

    fun updatePosition(cell:Cell){
        this.x = cell.x
        this.y = cell.y
    }
}