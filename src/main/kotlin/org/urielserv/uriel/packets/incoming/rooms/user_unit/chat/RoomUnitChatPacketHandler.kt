package org.urielserv.uriel.packets.incoming.rooms.user_unit.chat

import org.urielserv.uriel.ChatBubblesManager
import org.urielserv.uriel.extensions.readInt
import org.urielserv.uriel.extensions.readString
import org.urielserv.uriel.game.rooms.chat.RoomChatMessage
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import java.io.ByteArrayInputStream

class RoomUnitChatPacketHandler(
    private val type: RoomChatMessage.ChatType
) : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteArrayInputStream) {
        if (client.habbo == null) return

        if (client.habbo!!.room == null) return

        val message = packet.readString()
        val bubbleId = packet.readInt()

        val bubble =
            ChatBubblesManager.getChatBubbleByNitroStyleId(bubbleId) ?: ChatBubblesManager.getChatBubbleById(1)!!

        client.habbo!!.roomUnit!!.talk(RoomChatMessage(client.habbo!!, message, bubble, type))
    }

}