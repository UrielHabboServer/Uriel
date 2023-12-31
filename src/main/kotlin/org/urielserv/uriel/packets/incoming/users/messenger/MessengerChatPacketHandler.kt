package org.urielserv.uriel.packets.incoming.users.messenger

import org.urielserv.uriel.extensions.getString
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import java.nio.ByteBuffer

class MessengerChatPacketHandler : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        val habbo = client.habbo ?: return

        val targetId = packet.getInt()
        var message = packet.getString()

        if (message.length > MAX_MESSAGE_LENGTH) {
            message = message.substring(0, MAX_MESSAGE_LENGTH - 1)
        }

        val friendship = habbo.messenger.getFriendship(targetId) ?: return

        friendship.sendMessage(habbo, message)
    }

    companion object {

        const val MAX_MESSAGE_LENGTH = 256

    }

}