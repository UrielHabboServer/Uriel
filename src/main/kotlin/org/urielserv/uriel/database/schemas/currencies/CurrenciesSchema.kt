package org.urielserv.uriel.database.schemas.currencies

import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import org.urielserv.uriel.game.currencies.UrielCurrency

object CurrenciesSchema : Table<UrielCurrency>("currencies") {

    val id = int("id").primaryKey().bindTo { it.id }
    val nitroId = int("nitro_id").bindTo { it.nitroId }

    val name = varchar("name").bindTo { it.name }
    val isSeasonal = boolean("is_seasonal").bindTo { it.isSeasonal }
    val default = int("default_amount").bindTo { it.default }

    val autoTimer = int("auto_timer").bindTo { it.autoTimer }
    val toGive = int("to_give").bindTo { it.toGive }

}