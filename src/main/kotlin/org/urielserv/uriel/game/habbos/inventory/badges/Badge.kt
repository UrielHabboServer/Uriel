package org.urielserv.uriel.game.habbos.inventory.badges

import org.ktorm.entity.Entity
import org.urielserv.uriel.game.habbos.HabboInfo

interface Badge : Entity<Badge> {

    companion object : Entity.Factory<Badge>()

    val id: Int
    var habboInfo: HabboInfo

    var code: String

    var isActive: Boolean
    var slotId: Int

}