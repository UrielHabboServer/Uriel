package org.urielserv.uriel.game.rooms

import org.urielserv.uriel.packets.outgoing.Packet

class Room internal constructor(
    val info: RoomInfo,
) {

    fun appendToPacket(packet: Packet) {
        packet.appendInt(info.id)
        packet.appendString(info.name)

        if (info.isPublic) {
            packet.appendInt(0)
            packet.appendString("")
        } else {
            packet.appendInt(info.ownerHabboInfo.id)
            packet.appendString(info.ownerHabboInfo.username)
        }

        packet.appendInt(info.accessType.ordinal)

        packet.appendInt(info.users)
        packet.appendInt(info.maximumUsers)

        packet.appendString(info.description)

        packet.appendInt(0)
        packet.appendInt(info.score)

        packet.appendInt(0)
        packet.appendInt(info.flatCategory.id)

        val tags = info.tags.split(";")
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

        if (!info.isPublic) {
            base = base or 8
        }

        packet.appendInt(base)

        // TODO: Add Guild to packet

        // TODO: Add Promotion Info to packet
    }

}