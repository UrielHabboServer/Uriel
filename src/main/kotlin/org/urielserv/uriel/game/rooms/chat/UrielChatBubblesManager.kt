package org.urielserv.uriel.game.rooms.chat

import org.ktorm.entity.forEach
import org.urielserv.uriel.Database
import org.urielserv.uriel.core.database.schemas.rooms.ChatBubblesSchema

class UrielChatBubblesManager {

    private val _chatBubbles = mutableMapOf<Int, ChatBubble>()
    val chatBubbles: List<ChatBubble>
        get() = _chatBubbles.values.toList()

    init {
        Database.sequenceOf(ChatBubblesSchema)
            .forEach { _chatBubbles[it.id] = it }
    }

    fun getChatBubbleById(id: Int): ChatBubble? = _chatBubbles[id]

    fun getChatBubbleByName(name: String): ChatBubble? =
        _chatBubbles.values.firstOrNull { it.name == name }

    fun getChatBubbleByNitroStyleId(nitroStyleId: Int): ChatBubble? =
        _chatBubbles.values.firstOrNull { it.nitroStyleId == nitroStyleId }

}