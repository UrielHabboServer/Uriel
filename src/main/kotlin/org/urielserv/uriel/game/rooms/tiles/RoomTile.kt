package org.urielserv.uriel.game.rooms.tiles

import org.urielserv.uriel.game.habbos.room_unit.HabboRoomUnit

data class RoomTile(
    val x: Int,
    val y: Int,
    val height: Int,
    var state: RoomTileState
) {

    val roomUnitsOnTile = mutableListOf<HabboRoomUnit>()

}