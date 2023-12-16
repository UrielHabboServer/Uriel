package org.urielserv.uriel.game.rooms.chat

import org.urielserv.uriel.ChatBubblesManager
import org.urielserv.uriel.extensions.hasPermission
import org.urielserv.uriel.game.habbos.Habbo

class RoomChatMessage(
    val habbo: Habbo,
    var message: String,
    var bubble: ChatBubble,
    val type: ChatType
) {

    companion object {

        private val happinessEmoticons = listOf(":-)", ":)", ":]")
        private val madnessEmoticons = listOf(">:(", ":@")
        private val surpriseEmoticons = listOf(":O", ":o", ":0", "o.o", "O.o", "o.O", "O.O")
        private val sadnessEmoticons = listOf(":-(", ":(", ":[")

    }

    var whisperTarget: Habbo? = null
        private set

    var emotionId = 0
        private set

    init {
        if (type == ChatType.WHISPER) {
            val split = message.split(" ")

            if (split.size > 1) {
                val username = split[0]
                whisperTarget = habbo.room!!.getHabboByUsername(username)
            } else {
                whisperTarget = null
            }

            message = message.substringAfter(" ")
        }

        if (!checkIfBubbleCanBeUsed()) {
            bubble = ChatBubblesManager.getChatBubbleById(1)!!
        }

        emotionId = findEmotionId()

        println("MESSAGE: [$type] (E;$emotionId | B;${bubble.nitroStyleId} | T;$whisperTarget) ${habbo.info.username}: $message")
    }

    private fun findEmotionId(): Int {
        for (emoticon in happinessEmoticons) {
            if (emoticon in message) return 1
        }

        for (emoticon in madnessEmoticons) {
            if (emoticon in message) return 2
        }

        for (emoticon in surpriseEmoticons) {
            if (emoticon in message) return 3
        }

        for (emoticon in sadnessEmoticons) {
            if (emoticon in message) return 4
        }

        return 0
    }

    private fun checkIfBubbleCanBeUsed(): Boolean {
        if (!habbo.hasPermission("uriel.chat_bubbles.${bubble.id}")) {
            return false
        }

        if (bubble.isAmbassadorOnly && !habbo.data.isAmbassador) {
            return false
        }

        if (bubble.isClubOnly && !habbo.subscriptions.hasActiveHabboClubMembership()) {
            return false
        }

        return true
    }

    enum class ChatType {
        TALK,
        SHOUT,
        WHISPER
    }

}