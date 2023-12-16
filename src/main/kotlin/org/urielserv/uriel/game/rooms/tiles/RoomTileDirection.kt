package org.urielserv.uriel.game.rooms.tiles

enum class RoomTileDirection {

    NORTH,
    NORTH_EAST,
    EAST,
    SOUTH_EAST,
    SOUTH,
    SOUTH_WEST,
    WEST,
    NORTH_WEST;

    val isDiagonal: Boolean
        get() = this == NORTH_EAST || this == SOUTH_EAST || this == SOUTH_WEST || this == NORTH_WEST

    fun toNonDiagonal(): RoomTileDirection {
        return when (this) {
            NORTH_EAST -> NORTH
            SOUTH_EAST -> EAST
            SOUTH_WEST -> SOUTH
            NORTH_WEST -> WEST
            else -> this
        }
    }

    companion object {

        fun fromInt(int: Int): RoomTileDirection {
            for (direction in entries) {
                if (direction.ordinal == int) {
                    return direction
                }
            }

            return NORTH
        }

        fun getDirectionFromTileToTile(from: RoomTile, to: RoomTile): RoomTileDirection {
            if (from.x == to.x && from.y > to.y) {
                return NORTH
            } else if (from.x < to.x && from.y > to.y) {
                return NORTH_EAST
            } else if (from.x < to.x && from.y == to.y) {
                return EAST
            } else if (from.x < to.x) {
                return SOUTH_EAST
            } else if (from.x == to.x && from.y < to.y) {
                return SOUTH
            } else if (from.x > to.x && from.y < to.y) {
                return SOUTH_WEST
            } else if (from.x > to.x && from.y == to.y) {
                return WEST
            } else if (from.x > to.x) {
                return NORTH_WEST
            }

            return NORTH
        }

    }

}