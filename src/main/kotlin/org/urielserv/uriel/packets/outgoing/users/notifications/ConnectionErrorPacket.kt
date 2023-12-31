package org.urielserv.uriel.packets.outgoing.users.notifications

import org.urielserv.uriel.extensions.currentUnixSeconds
import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class ConnectionErrorPacket(
    private val errorCode: Int,
    private val messageId: Int
) : Packet() {

    override val packetId = Outgoing.ConnectionError

    override suspend fun construct() {
        appendInt(errorCode)
        appendInt(messageId)
        appendString(currentUnixSeconds.toString())
    }

}