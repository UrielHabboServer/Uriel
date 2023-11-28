package org.urielserv.uriel.packets.outgoing.users

import org.urielserv.uriel.packets.outgoing.Packet
import org.urielserv.uriel.packets.outgoing.OutgoingPacketIDs

class UserHomeRoomPacket(
    private val homeRoomId: Int,
    private val roomToEnterId: Int
) : Packet() {

    override val packetId = OutgoingPacketIDs.UserHomeRoom

    override suspend fun construct() {
        appendInt(homeRoomId)
        appendInt(roomToEnterId)
    }

}