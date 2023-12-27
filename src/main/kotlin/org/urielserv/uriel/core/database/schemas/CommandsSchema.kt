package org.urielserv.uriel.core.database.schemas

import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.int
import org.ktorm.schema.text
import org.urielserv.uriel.game.command_system.CommandInfo

object CommandsSchema : Table<CommandInfo>("commands") {

    val id = int("id").primaryKey().bindTo { it.id }

    val name = text("name").bindTo { it.name }
    val description = text("description").bindTo { it.description }

    val permission = text("permission").bindTo { it.permission }
    val enabled = boolean("enabled").bindTo { it.enabled }

    val invokers = text("invokers").bindTo { it.rawInvokers }

}