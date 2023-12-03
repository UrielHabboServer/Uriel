package org.urielserv.uriel.database.schemas.users

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.urielserv.uriel.database.schemas.currencies.CurrenciesSchema
import org.urielserv.uriel.database.schemas.users.navigator.UserNavigatorSavedSearchesSchema.bindTo
import org.urielserv.uriel.game.habbos.currencies.HabboCurrency

object UserCurrenciesSchema : Table<HabboCurrency>("user_currencies") {

    val id = int("id").primaryKey().bindTo { it.id }
    val currencyId = int("currency_id").references(CurrenciesSchema) { it.currency }
    val userId = int("user_id").references(UsersSchema) { it.habboInfo }
    val amount = int("amount").bindTo { it.amount }

}