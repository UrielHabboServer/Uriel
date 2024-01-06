package org.urielserv.uriel.game.habbos.messenger.interfaces

import org.ktorm.entity.Entity
import org.urielserv.uriel.game.habbos.HabboInfo

interface OfflineMessage : Entity<OfflineMessage> {

    companion object : Entity.Factory<OfflineMessage>()

    val id: Int

    var senderHabboInfo: HabboInfo
    var receiverHabboInfo: HabboInfo

    var timestamp: Int

    var message: String

}