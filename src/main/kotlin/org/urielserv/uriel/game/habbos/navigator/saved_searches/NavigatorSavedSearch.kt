package org.urielserv.uriel.game.habbos.navigator.saved_searches

import org.ktorm.entity.Entity
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.habbos.HabboInfo

interface NavigatorSavedSearch : Entity<NavigatorSavedSearch> {

    companion object : Entity.Factory<NavigatorSavedSearch>()

    val id: Int
    var habboInfo: HabboInfo
    val habbo: Habbo?
        get() = habboInfo.habbo

    var searchCode: String
    var filter: String

}