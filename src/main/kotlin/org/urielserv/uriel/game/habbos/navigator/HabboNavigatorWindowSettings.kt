package org.urielserv.uriel.game.habbos.navigator

import org.ktorm.entity.Entity
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.habbos.HabboInfo

interface HabboNavigatorWindowSettings : Entity<HabboNavigatorWindowSettings> {

    companion object : Entity.Factory<HabboNavigatorWindowSettings>()

    val id: Int
    var habboInfo: HabboInfo
    val habbo: Habbo?
        get() = habboInfo.habbo

    var x: Int
    var y: Int
    var width: Int
    var height: Int

    var isLeftPanelOpen: Boolean
    var resultsMode: Int

}