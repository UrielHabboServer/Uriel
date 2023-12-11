package org.urielserv.uriel.game.rooms.tiles

import org.urielserv.uriel.game.rooms.RoomModel
import kotlin.math.abs

class RoomTileMap(
    private val model: RoomModel
) {

    companion object {

        private const val STRAIGHT_COST = 10
        private const val DIAGONAL_COST = 14

    }

    val tiles: Array<Array<RoomTile>>
    val doorTile: RoomTile
    val doorDirection: RoomTileDirection

    val tileCount: Int
    val width: Int
    val length: Int

    init {
        val heightmap = model.heightmap.split("\n")

        width = heightmap[0].length
        length = heightmap.size

        var arrayTileCount = 0
        tiles = Array(width) { x ->
            Array(length) { y ->
                val heightmapChar = heightmap[y][x]
                val tileHeight = convertHeightCharToInt(heightmapChar)

                val state = when (length) {
                    x -> RoomTileState.VOID
                    else -> RoomTileState.OPEN
                }

                arrayTileCount++
                RoomTile(x, y, tileHeight, state)
            }
        }

        tileCount = arrayTileCount

        doorTile = getTile(model.doorX, model.doorY)!!
        doorDirection = RoomTileDirection.fromInt(model.doorDirection)
    }

    fun getTile(x: Int, y: Int): RoomTile? {
        try {
            return tiles[x][y]
        } catch (exc: ArrayIndexOutOfBoundsException) {
            return null
        }
    }

    fun getAdjacentTile(tile: RoomTile, direction: RoomTileDirection): RoomTile? {
        val x = tile.x
        val y = tile.y

        return when (direction) {
            RoomTileDirection.NORTH -> getTile(x, y - 1)
            RoomTileDirection.EAST -> getTile(x + 1, y)
            RoomTileDirection.SOUTH -> getTile(x, y + 1)
            RoomTileDirection.WEST -> getTile(x - 1, y)
            RoomTileDirection.NORTH_EAST -> getTile(x + 1, y - 1)
            RoomTileDirection.SOUTH_EAST -> getTile(x + 1, y + 1)
            RoomTileDirection.SOUTH_WEST -> getTile(x - 1, y + 1)
            RoomTileDirection.NORTH_WEST -> getTile(x - 1, y - 1)
        }
    }

    fun getPathTo(start: RoomTile, goal: RoomTile): List<RoomTile> {
        val openSet = mutableListOf(start)
        val cameFrom = mutableMapOf<RoomTile, RoomTile>()
        val gScore = mutableMapOf(start to 0)
        val fScore = mutableMapOf(start to getHeuristicCostEstimate(start, goal))

        while (openSet.isNotEmpty()) {
            val current = openSet.minByOrNull { fScore.getOrDefault(it, Int.MAX_VALUE) }!!

            if (current == goal) {
                return reconstructPath(cameFrom, current)
            }

            openSet.remove(current)

            for (neighbor in getNeighbors(current)) {
                val tentativeGScore = gScore.getOrDefault(current, Int.MAX_VALUE) + getMovementCost(current, neighbor)

                if (tentativeGScore < gScore.getOrDefault(neighbor, Int.MAX_VALUE)) {
                    cameFrom[neighbor] = current
                    gScore[neighbor] = tentativeGScore
                    fScore[neighbor] = tentativeGScore + getHeuristicCostEstimate(neighbor, goal)

                    if (neighbor !in openSet) {
                        openSet.add(neighbor)
                    }
                }
            }
        }

        return emptyList()
    }

    private fun getHeuristicCostEstimate(start: RoomTile, goal: RoomTile): Int {
        val dx = abs(start.x - goal.x)
        val dy = abs(start.y - goal.y)

        return 10 * (dx + dy) + 4 * minOf(dx, dy)
    }

    private fun getMovementCost(current: RoomTile, neighbor: RoomTile): Int {
        val dx = abs(current.x - neighbor.x)
        val dy = abs(current.y - neighbor.y)

        return if (dx == 1 && dy == 1) DIAGONAL_COST else STRAIGHT_COST
    }

    private fun getNeighbors(tile: RoomTile): List<RoomTile> {
        val neighbors = mutableListOf<RoomTile>()

        for (direction in RoomTileDirection.entries) {
            val adjacentTile = getAdjacentTile(tile, direction) ?: continue

            if (adjacentTile.state != RoomTileState.BLOCKED && adjacentTile.state != RoomTileState.VOID) {
                neighbors.add(adjacentTile)
            }
        }

        return neighbors
    }

    private fun reconstructPath(cameFrom: Map<RoomTile, RoomTile>, current: RoomTile): List<RoomTile> {
        val path = mutableListOf(current)
        var currentTile = current

        while (cameFrom.containsKey(currentTile)) {
            currentTile = cameFrom[currentTile]!!
            path.add(currentTile)
        }

        return path.reversed()
    }

    private fun convertHeightCharToInt(heightmapChar: Char): Int {
        if (heightmapChar.isDigit()) {
            return heightmapChar.digitToInt()
        }

        return 10 + ("abcdefghijklmnopqrstuvwxyz".indexOf(heightmapChar.lowercase()))
    }

}