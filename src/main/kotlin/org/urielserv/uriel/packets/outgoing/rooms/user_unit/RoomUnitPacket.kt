package org.urielserv.uriel.packets.outgoing.rooms.user_unit

import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class RoomUnitPacket(
    private val habbos: List<Habbo>
) : Packet() {

    override val packetId = Outgoing.RoomUnit

    constructor(vararg habbos: Habbo) : this(habbos.toList())

    override suspend fun construct() {
        appendInt(habbos.size)

        for (habbo in habbos) {
            appendInt(habbo.info.id)
            appendString(habbo.info.username)
            appendString(habbo.info.motto)
            appendString(habbo.info.look)

            appendInt(habbo.roomUnit!!.id)
            appendInt(habbo.roomUnit!!.currentTile.x)
            appendInt(habbo.roomUnit!!.currentTile.y)
            appendString(habbo.roomUnit!!.currentTile.height.toString())
            appendInt(habbo.roomUnit!!.bodyRotation.ordinal)

            appendInt(1)

            appendString(habbo.info.gender.short())

            // TODO: Guilds
            appendInt(-1)
            appendInt(-1)
            appendString("")

            appendString("")

            // TODO: Achievements
            appendInt(0)

            appendBoolean(true)
        }
    }

}