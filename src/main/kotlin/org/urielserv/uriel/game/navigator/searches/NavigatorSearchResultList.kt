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
            packet.appendInt(room.info.id)
            packet.appendString(room.info.name)

            if (room.info.isPublic) {
                packet.appendInt(0)
                packet.appendString("")
            } else {
                packet.appendInt(room.info.ownerHabboInfo.id)
                packet.appendString(room.info.ownerHabboInfo.username)
            }

            packet.appendInt(room.info.accessType.ordinal)

            packet.appendInt(room.info.users)
            packet.appendInt(room.info.maximumUsers)

            packet.appendString(room.info.description)

            packet.appendInt(0)
            packet.appendInt(room.info.score)

            packet.appendInt(0)
            packet.appendInt(room.info.flatCategory.id)

            val tags = room.info.tags.split(";")
            packet.appendInt(tags.size)
            for (tag in tags) {
                packet.appendString(tag)
            }

            var base = 0

            /*
            TODO: Add Guild to base
            if (info.guildId > 0) {
                base = base or 2
            }
             */

            /*
            TODO: Add support for Promotions
            if (info.isPromoted) {
                base = base or 4
            }
             */

            if (!room.info.isPublic) {
                base = base or 8
            }

            packet.appendInt(base)

            // TODO: Add Guild to packet

            // TODO: Add Promotion Info to packet
        }
    }

}