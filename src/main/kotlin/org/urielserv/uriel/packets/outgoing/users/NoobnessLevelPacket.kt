package org.urielserv.uriel.packets.outgoing.users

import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class NoobnessLevelPacket(
    private val noobnessLevel: Int
) : Packet() {

    override val packetId = Outgoing.NoobnessLevel

    override suspend fun construct() {
        appendInt(noobnessLevel)
    }

    companion object {

        const val OLD_IDENTITY = 0
        const val NEW_IDENTITY = 1
        const val REAL_NOOB = 2

    }

}