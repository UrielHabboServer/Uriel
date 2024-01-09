package org.urielserv.uriel.core.database.schemas.users

import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import org.urielserv.uriel.game.habbos.inventory.badges.Badge

object UserBadgesSchema : Table<Badge>("user_badges") {

    val id = int("id").primaryKey().bindTo { it.id }
    val userId = int("user_id").references(UsersSchema) { it.habboInfo }

    val badge = varchar("badge").bindTo { it.code }

    val isActive = boolean("is_active").bindTo { it.isActive }
    val slotId = int("slot_id").bindTo { it.slotId }

}