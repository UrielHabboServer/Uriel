package org.urielserv.uriel.database.schemas.users

import org.ktorm.schema.Table
import org.ktorm.schema.enum
import org.ktorm.schema.int
import org.ktorm.schema.text
import org.urielserv.uriel.database.schemas.users.UserSubscriptionsSchema.bindTo
import org.urielserv.uriel.database.schemas.users.UserSubscriptionsSchema.references
import org.urielserv.uriel.game.habbos.HabboGender
import org.urielserv.uriel.game.habbos.inventory.looks.SavedLook

object UserSavedLooksSchema : Table<SavedLook>("user_looks") {

    val id = int("id").primaryKey().bindTo { it.id }
    val userId = int("user_id").references(UsersSchema) { it.habboInfo }

    val slotId = int("slot_id").bindTo { it.slotId }
    val look = text("look").bindTo { it.look }
    val gender = enum<HabboGender>("gender").bindTo { it.gender }

}