package org.urielserv.uriel.game.habbos

import org.ktorm.entity.Entity
import org.urielserv.uriel.HabboManager
import org.urielserv.uriel.game.permissions.ranks.Rank

interface HabboInfo : Entity<HabboInfo> {

    val id: Int

    var username: String
    var password: String

    var ssoTicket: String

    var email: String
    var isEmailVerified: Boolean

    var motto: String

    var look: String
    var gender: HabboGender

    var rank: Rank

    var accountCreation: Int

    var lastLogin: Int
    var lastOnline: Int
    var isOnline: Boolean

    var registrationIp: String
    var currentIp: String

    var homeRoomId: Int

    val habbo: Habbo?
        get() = HabboManager.getConnectedHabboById(id)

}