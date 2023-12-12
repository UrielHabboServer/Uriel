package org.urielserv.uriel.core.database.schemas

import org.ktorm.schema.Table
import org.ktorm.schema.varchar
import org.urielserv.uriel.core.locale.LocalText

object LocalTextsSchema : Table<LocalText>("uriel_texts") {
    val key = varchar("key").bindTo { it.key }
    val value = varchar("value").bindTo { it.value }
}