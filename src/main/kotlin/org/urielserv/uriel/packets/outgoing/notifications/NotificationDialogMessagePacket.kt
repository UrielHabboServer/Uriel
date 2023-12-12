package org.urielserv.uriel.packets.outgoing.notifications

import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class NotificationDialogMessagePacket(
    private val type: String,
    vararg keys: Pair<String, String>
) : Packet() {

    override val packetId = Outgoing.NotificationList

    private val keys = keys.toMutableList()

    override suspend fun construct() {
        appendString(type)

        appendInt(keys.size)
        for (key in keys) {
            appendString(key.first)
            appendString(key.second)
        }
    }

}