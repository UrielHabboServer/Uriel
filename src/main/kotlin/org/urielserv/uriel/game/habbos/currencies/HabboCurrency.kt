package org.urielserv.uriel.game.habbos.currencies

import org.ktorm.entity.Entity
import org.urielserv.uriel.game.currencies.UrielCurrency
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.habbos.HabboInfo

interface HabboCurrency : Entity<HabboCurrency> {

    companion object : Entity.Factory<HabboCurrency>()

    val id: Int
    var habboInfo: HabboInfo
    val habbo: Habbo?
        get() = habboInfo.habbo

    var currency: UrielCurrency
    var amount: Int

}