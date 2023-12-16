package org.urielserv.uriel.packets.outgoing.rooms.user_unit.chat

import org.urielserv.uriel.game.rooms.chat.RoomChatMessage
import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class RoomUnitChatShoutPacket(
    private val roomChatMessage: RoomChatMessage
) : Packet() {

    override val packetId = Outgoing.RoomUnitChatShout

    override suspend fun construct() {
        appendInt(roomChatMessage.habbo.roomUnit!!.id)
        appendString(roomChatMessage.message)
        appendInt(roomChatMessage.emotionId)
        appendInt(roomChatMessage.bubble.nitroStyleId)
        appendInt(0)
        appendInt(roomChatMessage.message.length)
    }

}