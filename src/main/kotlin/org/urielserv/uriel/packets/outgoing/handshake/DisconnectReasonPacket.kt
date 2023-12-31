package org.urielserv.uriel.packets.outgoing.handshake

import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class DisconnectReasonPacket(
    private val reason: DisconnectReason
) : Packet() {

    override val packetId = Outgoing.DisconnectReason

    override suspend fun construct() {
        appendInt(reason.code)
    }

    enum class DisconnectReason(val code: Int) {
        LOG_OUT(0),
        BANNED(1),
        STILL_BANNED(10),
        CONCURRENT_LOGIN(2),
        INCORRECT_PASSWORD(20),
    }

}