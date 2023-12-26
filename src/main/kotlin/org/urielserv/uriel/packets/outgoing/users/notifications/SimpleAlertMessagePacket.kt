package org.urielserv.uriel.packets.outgoing.users.notifications

import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class SimpleAlertMessagePacket(
    private val message: String,
    private val title: String? = null
) : Packet() {

    override val packetId = Outgoing.SimpleAlert

    override suspend fun construct() {
        appendString(message)

        if (title != null) appendString(title)
    }

}
