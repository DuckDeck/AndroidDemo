package stan.androiddemo.project.Game2048

import java.util.*

/**
 * Created by stanhu on 22/8/2017.
 */
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
}