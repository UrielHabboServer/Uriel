package org.urielserv.uriel.packets.outgoing.handshake

import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class AuthenticatedPacket : Packet() {

    override val packetId = Outgoing.Authenticated

    override suspend fun construct() = Unit

}