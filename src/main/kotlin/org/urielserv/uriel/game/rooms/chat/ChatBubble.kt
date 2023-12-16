package org.urielserv.uriel.game.rooms.chat

import org.ktorm.entity.Entity

interface ChatBubble : Entity<ChatBubble> {

    val id: Int
    val nitroStyleId: Int

    val name: String

    val isSystemBubble: Boolean
    val isClubOnly: Boolean
    val isAmbassadorOnly: Boolean

    val canBeOverridden: Boolean
    val canTriggerTalkingFurniture: Boolean

}