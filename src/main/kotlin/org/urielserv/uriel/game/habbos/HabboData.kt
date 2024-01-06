package org.urielserv.uriel.game.habbos

import org.ktorm.entity.Entity
import org.urielserv.uriel.HabboManager
import org.urielserv.uriel.game.rooms.chat.ChatBubble

interface HabboData : Entity<HabboData> {

    companion object : Entity.Factory<HabboData>()

    val id: Int
    var habboInfo: HabboInfo
    val habbo: Habbo?
        get() = HabboManager.getConnectedHabbo(habboInfo.id)

    val chatBubble: ChatBubble

    val isAmbassador: Boolean

}