package org.urielserv.uriel.game.habbos.currencies

import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.find
import org.urielserv.uriel.CurrencyManager
import org.urielserv.uriel.Database
import org.urielserv.uriel.database.schemas.users.UserCurrenciesSchema
import org.urielserv.uriel.game.currencies.UrielCurrency
import org.urielserv.uriel.game.habbos.Habbo

class HabboCurrencies(
    val habbo: Habbo
) {

    private val currencies = mutableMapOf<Int, HabboCurrency>()

    init {
        for (currency in CurrencyManager.getCurrencies()) {
            val habboCurrency = Database.sequenceOf(UserCurrenciesSchema)
                .find { (it.userId eq habbo.info.id) and (it.currencyId eq currency.id) }

            if (habboCurrency != null) {
                currencies[currency.id] = habboCurrency
            } else {
                val newCurrency = HabboCurrency {
                    this.currency = currency
                    this.habboInfo = this@HabboCurrencies.habbo.info
                    this.amount = currency.default
                }

                Database.sequenceOf(UserCurrenciesSchema).add(newCurrency)
                currencies[currency.id] = newCurrency
            }
        }
    }

}