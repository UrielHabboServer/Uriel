package org.urielserv.uriel.packets.outgoing.users

import org.urielserv.uriel.packets.outgoing.OutgoingPacketIDs
import org.urielserv.uriel.packets.outgoing.Packet

class UserHomeRoomPacket(
    private val homeRoomId: Int,
    private val roomIdToEnter: Int
) : Packet() {

    override val packetId = OutgoingPacketIDs.UserHomeRoom

    override suspend fun construct() {
        appendInt(homeRoomId)
        appendInt(roomIdToEnter)
    }

}