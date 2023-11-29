package org.urielserv.uriel.game.habbos

import org.ktorm.entity.Entity
import org.urielserv.uriel.HabboManager

interface HabboInfo : Entity<HabboInfo> {

    val id: Int
    var username: String

    var motto: String

    var look: String
    var gender: HabboGender

    var isOnline: Boolean

    var credits: Int
    var pixels: Int
    var points: Int

    var homeRoomId: Int

    val habbo: Habbo?
        get() = HabboManager.getHabboById(id)

}