package org.urielserv.uriel.packets.incoming.rooms.user_unit.chat

import org.urielserv.uriel.ChatBubblesManager
import org.urielserv.uriel.HotelSettings
import org.urielserv.uriel.extensions.getString
import org.urielserv.uriel.game.rooms.chat.RoomChatMessage
import org.urielserv.uriel.networking.UrielServerClient
import org.urielserv.uriel.packets.incoming.PacketHandler
import java.nio.ByteBuffer

class RoomUnitChatPacketHandler(
    private val type: RoomChatMessage.ChatType
) : PacketHandler {

    override suspend fun handle(client: UrielServerClient, packet: ByteBuffer) {
        val habbo = client.habbo ?: return

        if (habbo.room == null) return

        val message = packet.getString()
        val bubbleId = packet.getInt()

        val bubble =
            ChatBubblesManager.getChatBubbleByNitroStyleId(bubbleId)
                ?: ChatBubblesManager.getChatBubbleById(HotelSettings.habbos.defaultChatBubbleId)!!

        habbo.roomUnit!!.talk(RoomChatMessage(habbo, message, bubble, type))
    }

}