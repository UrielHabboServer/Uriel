package org.urielserv.uriel.core.database.schemas.users

import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import org.urielserv.uriel.game.permissions.Permission

object UserPermissionsSchema : Table<Permission>("user_permissions") {

    val id = int("id").primaryKey().bindTo { it.id }
    val userId = int("user_id").bindTo { it.entityId }

    val permission = varchar("permission").bindTo { it.permission }
    val allow = boolean("allow").bindTo { it.allow }

}