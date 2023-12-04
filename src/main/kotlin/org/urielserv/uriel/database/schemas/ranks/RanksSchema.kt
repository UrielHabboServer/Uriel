package org.urielserv.uriel.database.schemas.ranks

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import org.urielserv.uriel.game.permissions.ranks.Rank

object RanksSchema : Table<Rank>("ranks") {

    val id = int("id").primaryKey().bindTo { it.id }
    val name = varchar("name").bindTo { it.name }

    val weight = int("weight").bindTo { it.weight }
    val parent = int("parent").references(RanksSchema) { it.parent }

    val badge = varchar("badge").bindTo { it.badge }

    val prefix = varchar("prefix").bindTo { it.prefix }
    val prefixColor = varchar("prefix_color").bindTo { it.prefixColor }

}