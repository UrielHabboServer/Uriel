package org.urielserv.uriel.packets.outgoing.rooms.user_unit.chat

import org.urielserv.uriel.extensions.text
import org.urielserv.uriel.game.rooms.chat.RoomChatMessage
import org.urielserv.uriel.packets.outgoing.Outgoing
import org.urielserv.uriel.packets.outgoing.Packet

class RoomUnitChatWhisperPacket(
    private val roomChatMessage: RoomChatMessage,
    private val isSender: Boolean
) : Packet() {

    override val packetId = Outgoing.RoomUnitChatWhisper

    override suspend fun construct() {
        val message = if (isSender) {
            text(
                "uriel.chat.whisper_layout.sender",
                "target" to roomChatMessage.whisperTarget!!.info.username,
                "message" to roomChatMessage.message
            )
        } else {
            text(
                "uriel.chat.whisper_layout.receiver",
                "sender" to roomChatMessage.habbo.info.username,
                "message" to roomChatMessage.message
            )
        }

        appendInt(roomChatMessage.habbo.roomUnit!!.id)
        appendString(message)
        appendInt(roomChatMessage.emotionId)
        appendInt(roomChatMessage.bubble.nitroStyleId)
        appendInt(0)
        appendInt(roomChatMessage.message.length)
    }

}