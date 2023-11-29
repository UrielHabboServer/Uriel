package org.urielserv.uriel.game.habbos.inventory.looks

import org.ktorm.entity.Entity
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.habbos.HabboGender
import org.urielserv.uriel.game.habbos.HabboInfo

interface SavedLook : Entity<SavedLook> {

    companion object : Entity.Factory<SavedLook>()

    val id: Int
    val habboInfo: HabboInfo
    val habbo: Habbo?
        get() = habboInfo.habbo

    val slotId: Int
    var look: String
    var gender: HabboGender

    fun apply() = habbo?.let { apply(it) }

    fun apply(habbo: Habbo) {
        habbo.info.look = look
        habbo.info.gender = gender
        habbo.info.flushChanges()
    }

}