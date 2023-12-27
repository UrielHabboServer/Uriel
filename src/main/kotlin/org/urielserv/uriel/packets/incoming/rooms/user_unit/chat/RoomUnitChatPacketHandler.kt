package org.urielserv.uriel.packets.incoming.rooms.user_unit.chat

import org.urielserv.uriel.ChatBubblesManager
import org.urielserv.uriel.extensions.getString
import org.urielserv.uriel.game.rooms.chat.RoomChatMessage
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import java.nio.ByteBuffer

class RoomUnitChatPacketHandler(
    private val type: RoomChatMessage.ChatType
) : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        if (client.habbo == null) return

        if (client.habbo!!.room == null) return

        val message = packet.getString()
        val bubbleId = packet.getInt()

        val bubble =
            ChatBubblesManager.getChatBubbleByNitroStyleId(bubbleId) ?: ChatBubblesManager.getChatBubbleById(1)!!

        client.habbo!!.roomUnit!!.talk(RoomChatMessage(client.habbo!!, message, bubble, type))
    }

}