package org.urielserv.uriel.game.navigator.searches

import org.urielserv.uriel.game.navigator.searches.display.NavigatorDisplayMode
import org.urielserv.uriel.game.navigator.searches.display.NavigatorListMode
import org.urielserv.uriel.game.rooms.Room
import org.urielserv.uriel.packets.outgoing.Packet

class NavigatorSearchResultList(
    val code: String,
    val data: String,
    val action: NavigatorSearchAction,
    val displayMode: NavigatorDisplayMode,
    val listMode: NavigatorListMode,
    val rooms: MutableList<Room>
) {

    init {
        rooms.sortedByDescending { it.info.users }
    }

    fun appendToPacket(packet: Packet) {
        packet.appendString(code)
        packet.appendString(data)

        packet.appendInt(action.ordinal)
        packet.appendBoolean(displayMode.value)
        packet.appendInt(listMode.ordinal)

        packet.appendInt(rooms.size)
        for (room in rooms) {
            room.appendToPacket(packet)
        }
    }

}