package org.urielserv.uriel.packets.outgoing.handshake

import org.urielserv.uriel.packets.outgoing.Packet
import org.urielserv.uriel.packets.outgoing.OutgoingPacketIDs

class AuthenticationOKPacket : Packet() {

    override val packetId = OutgoingPacketIDs.AuthenticationOK

}