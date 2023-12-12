package org.urielserv.uriel.game.habbos.currencies

import org.ktorm.entity.Entity
import org.urielserv.uriel.game.currencies.UrielCurrency
import org.urielserv.uriel.game.habbos.Habbo
import org.urielserv.uriel.game.habbos.HabboInfo
import org.urielserv.uriel.packets.outgoing.notifications.ActivityPointNotificationMessagePacket
import org.urielserv.uriel.packets.outgoing.users.currencies.UserCreditsPacket

interface HabboCurrency : Entity<HabboCurrency> {

    companion object : Entity.Factory<HabboCurrency>()

    val id: Int
    var habboInfo: HabboInfo
    val habbo: Habbo?
        get() = habboInfo.habbo

    var currency: UrielCurrency
    var amount: Int

    suspend fun modifyBy(amount: Int) {
        val oldAmount = this.amount

        this.amount += amount

        if (habbo == null || amount == 0) return

        if (currency.nitroId == -1)
            UserCreditsPacket(habbo!!).send(habbo!!)
        else
            ActivityPointNotificationMessagePacket(oldAmount, amount, currency.nitroId).send(habbo!!)
    }

}