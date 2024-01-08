package org.urielserv.uriel.game.currencies

import org.ktorm.entity.forEach
import org.urielserv.uriel.Database
import org.urielserv.uriel.core.database.schemas.currencies.CurrenciesSchema

class UrielCurrencyManager {

    private val _currencies = mutableMapOf<Int, UrielCurrency>()
    val currencies: Map<Int, UrielCurrency>
        get() = _currencies.toMap()

    init {
        Database.sequenceOf(CurrenciesSchema)
            .forEach { _currencies[it.id] = it }
    }

}