package org.urielserv.uriel.game.currencies

import org.ktorm.entity.forEach
import org.urielserv.uriel.Database
import org.urielserv.uriel.database.schemas.currencies.CurrenciesSchema

class UrielCurrencyManager {

    private val currencies = mutableMapOf<Int, UrielCurrency>()

    init {
        Database.sequenceOf(CurrenciesSchema)
            .forEach { currencies[it.id] = it }
    }

    fun getCurrencies(): List<UrielCurrency> {
        return currencies.values.toList()
    }

}