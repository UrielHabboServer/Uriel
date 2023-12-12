package org.urielserv.uriel.database.schemas.core

import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import org.urielserv.uriel.game.navigator.NavigatorFlatCategory
import org.urielserv.uriel.core.texts.LocalText

object LocalTextsSchema : Table<LocalText>("uriel_texts") {
    val key = varchar("key").bindTo { it.key }
    val value = varchar("value").bindTo { it.value }
}