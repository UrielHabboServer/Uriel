package org.urielserv.uriel.packets.outgoing.handshake

import org.urielserv.uriel.packets.outgoing.OutgoingPacketIDs
import org.urielserv.uriel.packets.outgoing.Packet

class AuthenticatedPacket : Packet() {

    override val packetId = OutgoingPacketIDs.Authenticated

    override suspend fun construct() = Unit

}