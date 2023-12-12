package org.urielserv.uriel.core.database.schemas.ranks

import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import org.urielserv.uriel.game.permissions.Permission

object RankPermissionsSchema : Table<Permission>("rank_permissions") {

    val id = int("id").primaryKey().bindTo { it.id }
    val rankId = int("rank_id").bindTo { it.entityId }

    val permission = varchar("permission").bindTo { it.permission }
    val allow = boolean("allow").bindTo { it.allow }

}