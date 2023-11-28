package org.urielserv.uriel.packets.outgoing.users.nux

import org.urielserv.uriel.packets.outgoing.Packet
import org.urielserv.uriel.packets.outgoing.OutgoingPacketIDs

class NewUserExperienceStatusPacket(
    private val finishedNux: Boolean
) : Packet() {

    override val packetId = OutgoingPacketIDs.NewUserExperienceStatus

    override suspend fun construct() {
        appendInt(if (finishedNux) 1 else 0)
    }

}