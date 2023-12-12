package org.urielserv.uriel.game.currencies

import org.ktorm.entity.forEach
import org.urielserv.uriel.Database
import org.urielserv.uriel.database.schemas.currencies.CurrenciesSchema
import org.urielserv.uriel.game.habbos.Habbo
import kotlinx.coroutines.runBlocking
import org.urielserv.uriel.packets.outgoing.users.currencies.UserCreditsPacket
import org.urielserv.uriel.packets.outgoing.notifications.ActivityPointNotificationMessagePacket

class UrielCurrencyManager {

    private val currencies = mutableMapOf<Int, UrielCurrency>()

    init {
        Database.sequenceOf(CurrenciesSchema)
            .forEach { currencies[it.id] = it }
    }

    /**
     * Get the currencies in the hotel
     *
     * @return List of Currencies
     */
    fun getCurrencies(): List<UrielCurrency> {
        return currencies.values.toList()
    }

}