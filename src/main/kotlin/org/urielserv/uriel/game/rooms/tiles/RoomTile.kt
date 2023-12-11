package org.urielserv.uriel.game.rooms.tiles

data class RoomTile(
    val x: Int,
    val y: Int,
    val height: Int,
    var state: RoomTileState
)