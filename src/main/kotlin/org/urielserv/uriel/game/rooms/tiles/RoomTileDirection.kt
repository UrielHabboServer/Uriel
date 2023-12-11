package org.urielserv.uriel.game.rooms.tiles

enum class RoomTileDirection {

    NORTH,
    EAST,
    SOUTH,
    WEST,
    NORTH_EAST,
    SOUTH_EAST,
    SOUTH_WEST,
    NORTH_WEST;

    companion object {

        fun fromInt(value: Int): RoomTileDirection {
            return when (value) {
                0 -> NORTH
                1 -> NORTH_EAST
                2 -> EAST
                3 -> SOUTH_EAST
                4 -> SOUTH
                5 -> SOUTH_WEST
                6 -> WEST
                7 -> NORTH_WEST
                else -> throw IllegalArgumentException("Invalid direction value: $value")
            }
        }

    }

}