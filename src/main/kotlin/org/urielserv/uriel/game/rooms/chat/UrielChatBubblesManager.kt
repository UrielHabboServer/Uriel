package org.urielserv.uriel.game.rooms.chat

import org.ktorm.entity.forEach
import org.urielserv.uriel.Database
import org.urielserv.uriel.core.database.schemas.rooms.ChatBubblesSchema

class UrielChatBubblesManager {

    private val chatBubbles = mutableMapOf<Int, ChatBubble>()

    init {
        Database.sequenceOf(ChatBubblesSchema)
            .forEach { chatBubbles[it.id] = it }
    }

    fun getChatBubbleById(id: Int): ChatBubble? = chatBubbles[id]

    fun getChatBubbleByName(name: String): ChatBubble? =
        chatBubbles.values.firstOrNull { it.name == name }

    fun getChatBubbleByNitroStyleId(nitroStyleId: Int): ChatBubble? =
        chatBubbles.values.firstOrNull { it.nitroStyleId == nitroStyleId }

    fun getChatBubbles(): List<ChatBubble> = chatBubbles.values.toList()

}