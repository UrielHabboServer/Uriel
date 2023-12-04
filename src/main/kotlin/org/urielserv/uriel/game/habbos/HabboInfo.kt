package org.urielserv.uriel.game.habbos

import org.ktorm.entity.Entity
import org.urielserv.uriel.HabboManager
import org.urielserv.uriel.game.permissions.ranks.Rank

interface HabboInfo : Entity<HabboInfo> {

    val id: Int
    var username: String

    var motto: String

    var look: String
    var gender: HabboGender

    var rank: Rank

    var isOnline: Boolean

    var homeRoomId: Int

    val habbo: Habbo?
        get() = HabboManager.getHabboById(id)

}