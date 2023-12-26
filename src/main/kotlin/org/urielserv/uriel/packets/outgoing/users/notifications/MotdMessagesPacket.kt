package org.urielserv.uriel.packets.outgoing.users.notifications

import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class MotdMessagesPacket(
    private val messages: List<String>
) : Packet() {

    override val packetId = Outgoing.MotdMessages

    constructor(vararg messages: String) : this(messages.toList())

    override suspend fun construct() {
        appendInt(messages.size)

        for (message in messages) {
            appendString(message)
        }
    }

}