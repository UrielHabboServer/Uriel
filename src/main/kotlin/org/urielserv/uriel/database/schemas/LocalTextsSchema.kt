package org.urielserv.uriel.database.schemas

import org.ktorm.schema.Table
import org.ktorm.schema.varchar
import org.urielserv.uriel.locale.LocalText

object LocalTextsSchema : Table<LocalText>("uriel_texts") {
    val key = varchar("key").bindTo { it.key }
    val value = varchar("value").bindTo { it.value }
}