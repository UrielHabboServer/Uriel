package org.urielserv.uriel.core.database.schemas.users

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.urielserv.uriel.core.database.schemas.currencies.CurrenciesSchema
import org.urielserv.uriel.game.habbos.currencies.HabboCurrency

object UserCurrenciesSchema : Table<HabboCurrency>("user_currencies") {

    val id = int("id").primaryKey().bindTo { it.id }
    val userId = int("user_id").references(UsersSchema) { it.habboInfo }
    val currencyId = int("currency_id").references(CurrenciesSchema) { it.currency }

    val amount = int("amount").bindTo { it.amount }

}