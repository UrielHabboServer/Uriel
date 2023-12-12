package org.urielserv.uriel.game.currencies

import org.ktorm.entity.forEach
import org.urielserv.uriel.Database
import org.urielserv.uriel.database.schemas.currencies.CurrenciesSchema
import org.urielserv.uriel.game.habbos.Habbo
import kotlinx.coroutines.runBlocking
import org.urielserv.uriel.packets.outgoing.users.currencies.UserCreditsPacket
import org.urielserv.uriel.packets.outgoing.users.currencies.UserCurrencyPacket
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


    /**
     * Checks the currencys timers based on the habbos tick amount
     *
     * @param habbo The Habbo you're querying
     * @param ticks Amount of ticks to reference
     * @return void
     */
    fun checkGiveHabbo(habbo: Habbo, ticks: Int) {
        for (currency in getCurrencies()) {
            if(currency.autoTimer > 0 && ticks % currency.autoTimer == 0) {
                modCurrency(habbo, currency.id, currency.toGive)
            }
        }
    }

    /**
     * Modifys a given currency for said habbo
     *
     * @param habbo The habbo you're modifying
     * @param type Currency ID
     * @return void
     */
    fun modCurrency(habbo: Habbo, type: Int, amount: Int) {
        var currency = habbo.currencies.getById(type)

        if(currency == null) return

        val oldAmount = currency.amount
        currency.amount += amount

        runBlocking {
            if(currency.currency.nitroId == -1) 
                UserCreditsPacket(habbo).send(habbo)
            else 
                ActivityPointNotificationMessagePacket(oldAmount, amount, currency.currency.nitroId).send(habbo)
        }
    }

}