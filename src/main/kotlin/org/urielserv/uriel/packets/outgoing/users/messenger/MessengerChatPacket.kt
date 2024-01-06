package org.urielserv.uriel.packets.outgoing.users.messenger

import org.urielserv.uriel.extensions.currentUnixSeconds
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.habbos.messenger.interfaces.OfflineMessage
import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class MessengerChatPacket(
    private val senderId: Int,
    private val message: String,
    private val secondsSinceSent: Int = 0
) : Packet() {

    override val packetId = Outgoing.MessengerChat

    constructor(sender: Habbo, message: String) : this(
        senderId = sender.info.id,
        message = message
    )

    constructor(offlineMessage: OfflineMessage) : this(
        senderId = offlineMessage.senderHabboInfo.id,
        message = offlineMessage.message,
        secondsSinceSent = currentUnixSeconds - offlineMessage.timestamp
    )

    override suspend fun construct() {
        appendInt(senderId)
        appendString(message)
        appendInt(secondsSinceSent)
    }

}