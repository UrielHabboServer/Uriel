package org.urielserv.uriel.game.habbos

import org.urielserv.uriel.game.rooms.tiles.RoomTile
import org.urielserv.uriel.game.rooms.tiles.RoomTileDirection

class HabboRoomState(
    private val habbo: Habbo,

    var currentTile: RoomTile,
    var direction: RoomTileDirection
) {

    var isWalking = false

}